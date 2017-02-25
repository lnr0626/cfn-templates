/*
 * Copyright 2017 Lloyd Ramey <lnr0626@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lloydramey.cfn.scripting

import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.com.intellij.openapi.project.Project
import org.jetbrains.kotlin.script.KotlinScriptDefinition
import org.jetbrains.kotlin.script.KotlinScriptExternalDependencies
import java.io.File
import java.net.URI
import java.net.URLClassLoader
import kotlin.reflect.KClass

class KotlinScriptExternalDependenciesWithImports(
    val implicitImports: List<String> = emptyList(),
    val clazzpath: List<File> = emptyList()
) : KotlinScriptExternalDependencies {
    override val imports: Iterable<String>
        get() = implicitImports

    override val classpath: Iterable<File>
        get() = clazzpath
}

internal fun scriptDefinitionFor(
    clazz: KClass<out Any> = CfnTemplateScript::class,
    classpath: List<File> = emptyList()
) = object : KotlinScriptDefinition(clazz) {
    override fun <TF : Any> getDependenciesFor(
        file: TF,
        project: Project,
        previousDependencies: KotlinScriptExternalDependencies?
    ): KotlinScriptExternalDependencies? =
        KotlinScriptExternalDependenciesWithImports(ImplicitImports.list, classpath)
}

internal fun compileScript(
    outputDir: File,
    script: File,
    additionalSources: List<File> = emptyList(),
    classpath: List<File> = emptyList(),
    classLoader: ClassLoader,
    messageCollector: MessageCollector
): Class<*> {
    val urls = classpath.map(File::toURI).map(URI::toURL).toTypedArray()
    val loader = URLClassLoader(urls, classLoader)

    return compileKotlinScriptToDirectory(
        outputDir,
        script,
        scriptDefinitionFor(CfnTemplateScript::class, classpath),
        additionalSources,
        classpath,
        loader,
        messageCollector
    )
}
