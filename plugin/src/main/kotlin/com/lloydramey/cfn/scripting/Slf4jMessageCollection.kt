package com.lloydramey.cfn.scripting;

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

class Slf4jMessageCollection(val logger: Logger, val messageRenderer: MessageRenderer) : MessageCollector {
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
