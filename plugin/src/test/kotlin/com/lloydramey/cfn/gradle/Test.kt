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
package com.lloydramey.cfn.gradle

import com.lloydramey.cfn.gradle.internal.MessageRenderer
import com.lloydramey.cfn.gradle.internal.withFullPaths
import com.lloydramey.cfn.model.Template
import com.lloydramey.cfn.model.aws.parameters.AwsParameters
import com.lloydramey.cfn.scripting.CfnTemplateScript
import com.lloydramey.cfn.scripting.compileScriptToDirectory
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageLocation
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity.ERROR
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity.EXCEPTION
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity.INFO
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity.LOGGING
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity.OUTPUT
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity.STRONG_WARNING
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity.WARNING
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.utils.PathUtil
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.test.assertTrue

class LoggerMessageCollection(val logger: Logger, val messageRenderer: MessageRenderer) : MessageCollector {
    private var hasErrors: Boolean = false

    override fun clear() {
        // Do nothing, messages are already reported
    }

    override fun report(
        severity: CompilerMessageSeverity,
        message: String,
        location: CompilerMessageLocation
    ) {
        hasErrors = hasErrors or severity.isError

        val renderedMessage = messageRenderer.render(severity, message, location)
        when (severity) {
            EXCEPTION, ERROR -> logger.error(renderedMessage)
            STRONG_WARNING, WARNING -> logger.warn(renderedMessage)
            INFO -> logger.info(renderedMessage)
            LOGGING -> logger.debug(renderedMessage)
            OUTPUT -> logger.trace(renderedMessage)
        }
    }

    override fun hasErrors(): Boolean {
        return hasErrors
    }

}

class Test {
    @Test
    fun thingsWork() {

        val classpath = (getClassPath() + PathUtil.getJdkClassesRoots())

        assertTrue {
            compileScriptToDirectory(
                File("build/test-classes"),
                File("src/test/resources/").listRecursively(),
                LoggerMessageCollection(LoggerFactory.getLogger(Test::class.java), withFullPaths),
                classpath
            )
        }

    }

    private fun getClassPath(): List<File> = listOf(
        PathUtil.getResourcePathForClass(Unit::class.java),
        PathUtil.getResourcePathForClass(Template::class.java),
        PathUtil.getResourcePathForClass(AwsParameters::class.java),
        PathUtil.getResourcePathForClass(CfnTemplateScript::class.java)
    ).distinct()

    private fun File.listRecursively(): List<File> {
        return listFiles().flatMap { if (it.isFile) listOf(it) else it.listRecursively() }
    }
}