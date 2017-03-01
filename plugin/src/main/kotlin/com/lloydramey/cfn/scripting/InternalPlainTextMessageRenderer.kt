/*
 * Copyright 2010-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
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

internal val withFullPaths = object : InternalPlainTextMessageRenderer() {
    override fun getPath(location: CompilerMessageLocation): String? {
        return location.path
    }
}

interface MessageRenderer {
    fun render(
        severity: CompilerMessageSeverity, message: String, location: CompilerMessageLocation
    ): String
}

internal abstract class InternalPlainTextMessageRenderer : MessageRenderer {
    override fun render(
        severity: CompilerMessageSeverity, message: String, location: CompilerMessageLocation
    ): String {
        val line = location.line
        val column = location.column
        val lineContent = location.lineContent

        val path = if (line > 0) {
            getPath(location)?.let { path -> "$path:$line: " } ?: ""
        } else {
            getPath(location) ?: ""
        }

        val result = if (lineContent != null && column >= 1 && column <= lineContent.length + 1) {
            """$path${severity.presentableName}: ${decapitalizeIfNeeded(message)}
              |$lineContent
              |${" ".repeat(column - 1)}^""".trimMargin("|")
        } else {
            "$path: ${severity.presentableName}: ${decapitalizeIfNeeded(message)}"
        }

        return result
    }

    protected abstract fun getPath(location: CompilerMessageLocation): String?

    private fun decapitalizeIfNeeded(message: String): String {
        // TODO: invent something more clever
        // An ad-hoc heuristic to prevent decapitalization of some names
        if (message.startsWith("Java") || message.startsWith("Kotlin")) {
            return message
        }

        // For abbreviations and capitalized text
        if (message.length >= 2 && Character.isUpperCase(message[0]) && Character.isUpperCase(message[1])) {
            return message
        }

        return message.decapitalize()
    }
}
