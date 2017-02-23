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

import com.lloydramey.cfn.model.Template
import com.lloydramey.cfn.model.aws.parameters.AwsParameters
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer
import org.jetbrains.kotlin.cli.common.messages.PrintingMessageCollector
import org.jetbrains.kotlin.utils.PathUtil
import org.junit.Test
import org.slf4j.LoggerFactory
import java.io.File
import kotlin.reflect.full.memberProperties

class Test {

    @Test
    fun `test compiling script to build dir`() {
        val logger = LoggerFactory.getLogger("main")

        val classpath = (getClassPath() + PathUtil.getJdkClassesRoots())

        val script = compileScript(
            File("build"),
            File("src/test/resources/test.kts"),
            listOf(File("src/test/resources/another.kt")),
            classpath,
            Test::class.java.classLoader,
            PrintingMessageCollector(System.err, MessageRenderer.PLAIN_FULL_PATHS, false)
        )

        println("Constructing")

        val obj = script.newInstance()

        println(obj::class.memberProperties.associate { it.name to it.call(obj) })
    }

    private fun getClassPath(): List<File> = listOf(
        PathUtil.getResourcePathForClass(kotlin.properties.Delegates::class.java),
        PathUtil.getResourcePathForClass(Template::class.java),
        PathUtil.getResourcePathForClass(AwsParameters::class.java),
        PathUtil.getResourcePathForClass(CfnTemplateScript::class.java)
    ).distinct()
}