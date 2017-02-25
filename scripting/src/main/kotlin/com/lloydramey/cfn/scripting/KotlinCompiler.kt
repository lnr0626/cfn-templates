/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lloydramey.cfn.scripting

import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.cli.common.messages.MessageUtil
import org.jetbrains.kotlin.cli.common.messages.OutputMessageUtil
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinToJVMBytecodeCompiler.compileBunchOfSources
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinToJVMBytecodeCompiler.compileScript
import org.jetbrains.kotlin.cli.jvm.config.addJvmClasspathRoot
import org.jetbrains.kotlin.cli.jvm.config.addJvmClasspathRoots
import org.jetbrains.kotlin.codegen.CompilationException
import org.jetbrains.kotlin.com.intellij.openapi.Disposable
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer.dispose
import org.jetbrains.kotlin.com.intellij.openapi.util.Disposer.newDisposable
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.CompilerConfigurationKey
import org.jetbrains.kotlin.config.JVMConfigurationKeys.OUTPUT_DIRECTORY
import org.jetbrains.kotlin.config.JVMConfigurationKeys.RETAIN_OUTPUT_IN_MEMORY
import org.jetbrains.kotlin.config.JVMConfigurationKeys.SCRIPT_DEFINITIONS
import org.jetbrains.kotlin.config.addKotlinSourceRoots
import org.jetbrains.kotlin.script.KotlinScriptDefinition
import org.jetbrains.kotlin.utils.PathUtil
import org.jetbrains.kotlin.utils.addToStdlib.singletonList
import java.io.File

internal
fun compileKotlinScriptToDirectory(
    outputDirectory: File,
    scriptFile: File,
    scriptDef: KotlinScriptDefinition,
    additionalSourceFiles: List<File>,
    classPath: List<File>,
    classLoader: ClassLoader,
    messageCollector: MessageCollector): Class<*> {

    withRootDisposable { rootDisposable ->
        withMessageCollector(messageCollector) { messageCollector ->
            val files = scriptFile.singletonList() + additionalSourceFiles
            val configuration = compilerConfigurationFor(messageCollector, files).apply {
                put(RETAIN_OUTPUT_IN_MEMORY, true)
                put(OUTPUT_DIRECTORY, outputDirectory)
                setModuleName(scriptFile.nameWithoutExtension)
                addScriptDefinition(scriptDef)
                classPath.forEach { addJvmClasspathRoot(it) }
            }
            val environment = kotlinCoreEnvironmentFor(configuration, rootDisposable)
            return compileScript(environment, classLoader)
                ?: throw IllegalStateException("Internal error: unable to compile script, see log for details")
        }
    }
}

fun compileToDirectory(
    outputDirectory: File,
    sourceFiles: Iterable<File>,
    messageCollector: MessageCollector,
    classPath: Iterable<File> = emptyList()): Boolean =

    compileTo(OUTPUT_DIRECTORY, outputDirectory, sourceFiles, messageCollector, classPath)

private
fun compileTo(
    outputConfigurationKey: CompilerConfigurationKey<File>,
    output: File,
    sourceFiles: Iterable<File>,
    messageCollector: MessageCollector,
    classPath: Iterable<File>): Boolean {

    withRootDisposable { disposable ->
        withMessageCollector(messageCollector) { messageCollector ->
            val configuration = compilerConfigurationFor(messageCollector, sourceFiles).apply {
                put(outputConfigurationKey, output)
                setModuleName(output.nameWithoutExtension)
                classPath.forEach { addJvmClasspathRoot(it) }
                addJvmClasspathRoot(kotlinStdlibJar)
            }
            val environment = kotlinCoreEnvironmentFor(configuration, disposable)
            return compileBunchOfSources(environment)
        }
    }
}

private
val kotlinStdlibJar: File
    get() = PathUtil.getResourcePathForClass(Unit::class.java)

private
inline fun <T> withRootDisposable(action: (Disposable) -> T): T {
    val rootDisposable = newDisposable()
    try {
        return action(rootDisposable)
    } finally {
        dispose(rootDisposable)
    }
}

private
inline fun <T> withMessageCollector(messageCollector: MessageCollector, action: (MessageCollector) -> T): T {
    try {
        return action(messageCollector)
    } catch (ex: CompilationException) {
        messageCollector.report(
            CompilerMessageSeverity.EXCEPTION,
            OutputMessageUtil.renderException(ex),
            MessageUtil.psiElementToMessageLocation(ex.element))

        throw IllegalStateException("Internal error: ${OutputMessageUtil.renderException(ex)}", ex)
    }
}

private
fun compilerConfigurationFor(messageCollector: MessageCollector, sourceFile: File) =
    compilerConfigurationFor(messageCollector, listOf(sourceFile))

private
fun compilerConfigurationFor(messageCollector: MessageCollector, sourceFiles: Iterable<File>): CompilerConfiguration =
    CompilerConfiguration().apply {
        addKotlinSourceRoots(sourceFiles.map { it.canonicalPath })
        addJvmClasspathRoots(PathUtil.getJdkClassesRoots())
        put<MessageCollector>(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, messageCollector)
    }

private
fun CompilerConfiguration.setModuleName(name: String) {
    put(CommonConfigurationKeys.MODULE_NAME, name)
}

private
fun CompilerConfiguration.addScriptDefinition(scriptDef: KotlinScriptDefinition) {
    add(SCRIPT_DEFINITIONS, scriptDef)
}

private
fun kotlinCoreEnvironmentFor(configuration: CompilerConfiguration, rootDisposable: Disposable) =
    KotlinCoreEnvironment.createForProduction(rootDisposable, configuration, EnvironmentConfigFiles.JVM_CONFIG_FILES)
