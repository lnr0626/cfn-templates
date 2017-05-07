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
package com.lloydramey.cfn.gradle.internal

import com.lloydramey.cfn.scripting.CfnTemplateScript
import java.io.File
import java.io.FileWriter

fun convertToJson(clazz: Class<out CfnTemplateScript>, destinationDir: File) {
    val filename = templateClassNameToTemplateName(clazz.simpleName)

    val script = clazz.newInstance()
    val template = script.toTemplate()
    val json = template.toString()

    val file = File(destinationDir, filename)
    file.parentFile.mkdirs()

    FileWriter(file).use { writer ->
        writer.write(json)
    }
}

private fun templateClassNameToTemplateName(name: String) = name.replace(Regex("(_template)?$"), "") + ".template"
