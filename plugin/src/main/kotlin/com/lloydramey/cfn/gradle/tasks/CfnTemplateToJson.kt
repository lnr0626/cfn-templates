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
package com.lloydramey.cfn.gradle.tasks

import com.lloydramey.cfn.gradle.internal.OpenForGradle
import com.lloydramey.cfn.gradle.internal.convertToJson
import com.lloydramey.cfn.scripting.CfnTemplateScript
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.compile.AbstractCompile
import java.io.File
import java.net.URI
import java.net.URLClassLoader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.concurrent.thread
import kotlin.streams.toList

@OpenForGradle
class CfnTemplateToJson : AbstractCompile() {

    private fun list(path: Path) =
        Files.list(path).map { it.toAbsolutePath() }.toList()


    @TaskAction
    override fun compile() {
        val files = (classpath.files)
            .map(File::toURI)
            .map(URI::toURL)

        val templateClassNames = files
            .map { Paths.get(it.toURI()).toAbsolutePath() }
            .filter { Files.isDirectory(it) }
            .flatMap { classpathFolder ->
                list(classpathFolder)
                    .map { it.toString() }
                    .filter { it.endsWith("_template.class") }
                    .map { it.removePrefix("$classpathFolder/").removeSuffix(".class") }
            }

        val classloader = URLClassLoader(files.toTypedArray(), CfnTemplateToJson::class.java.classLoader)

        @Suppress("UNCHECKED_CAST")
        val t = thread(start = true, isDaemon = false, name = "Cloudify Template to JSON", contextClassLoader = classloader) {
            val templates = templateClassNames
                .map { Class.forName(it, true, classloader) }
                .filter { CfnTemplateScript::class.java.isAssignableFrom(it) }
                .map { it as Class<CfnTemplateScript> }

            templates
                .forEach { convertToJson(it, destinationDir) }
        }

        t.join()
    }

}
