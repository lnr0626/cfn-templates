package com.lloydramey.cfn.gradle.tasks

import com.lloydramey.cfn.gradle.internal.OpenForGradle
import com.lloydramey.cfn.scripting.CfnTemplateScript
import org.gradle.api.tasks.compile.AbstractCompile
import org.jetbrains.kotlin.gradle.plugin.ParentLastURLClassLoader
import org.reflections.Reflections
import java.io.File
import java.io.FileWriter
import java.net.URI
import kotlin.concurrent.thread

@OpenForGradle
class CfnTemplateToJson : AbstractCompile() {
    override fun compile() {
        val files = (getSource().files + classpath.files)
            .map(File::toURI)
            .map(URI::toURL)

        val classloader = ParentLastURLClassLoader(files, CfnTemplateToJson::class.java.classLoader)

        val t = thread(start = true, isDaemon = false, name = "Cloudify Template to JSON", contextClassLoader = classloader) {
            val reflections = Reflections("")
            val templates = reflections.getSubTypesOf(CfnTemplateScript::class.java)

            templates
                .map { it.canonicalName to it.newInstance() }
                .map { (name, script) -> name to script.toTemplate() }
                .map { (name, template) -> name to template.toString() }
                .forEach { (name, json) ->
                    val file = File(destinationDir, canonicalNameToFileName(name))
                    FileWriter(file).use { writer ->
                        writer.write(json)
                    }
                }
        }

        t.join()
    }

    private fun canonicalNameToFileName(name: String) = name.replace(Regex("(_template)?$"), ".template")

}
