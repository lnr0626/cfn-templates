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
import org.slf4j.Logger

class Slf4jMessageCollector(val logger: Logger, val messageRenderer: MessageRenderer) : MessageCollector {
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
