package com.lloydramey.cfn.gradle.tasks

import com.lloydramey.cfn.gradle.internal.OpenForGradle
import com.lloydramey.cfn.gradle.internal.convertToJson
import com.lloydramey.cfn.scripting.CfnTemplateScript
import org.gradle.api.tasks.compile.AbstractCompile
import org.jetbrains.kotlin.gradle.plugin.ParentLastURLClassLoader
import org.reflections.Reflections
import java.io.File
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
            val reflections = Reflections("", classloader)
            val templates = reflections.getSubTypesOf(CfnTemplateScript::class.java)

            templates
                .forEach { convertToJson(it, destinationDir) }
        }

        t.join()
    }

}
