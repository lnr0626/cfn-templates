package com.lloydramey.cfn.gradle.internal

import com.lloydramey.cfn.scripting.CfnTemplateScript
import java.io.File
import java.io.FileWriter

fun convertToJson(clazz: Class<out CfnTemplateScript>, destinationDir: File) {
    val filename = canonicalNameToFileName(clazz.canonicalName)

    val script = clazz.newInstance()
    val template = script.toTemplate()
    val json = template.toString()

    val file = File(destinationDir, filename)
    file.parentFile.mkdirs()

    FileWriter(file).use { writer ->
        writer.write(json)
    }
}

private fun canonicalNameToFileName(name: String) = name.replace(Regex("(_template)?$"), "") + ".template"
