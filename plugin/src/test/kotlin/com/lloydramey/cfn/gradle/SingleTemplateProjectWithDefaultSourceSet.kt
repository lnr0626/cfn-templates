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

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.gradle.testkit.runner.TaskOutcome.SUCCESS
import org.junit.Before
import org.junit.Test

class SingleTemplateProjectWithDefaultSourceSet(val version: String) : FolderBasedTest() {
    @Before
    fun setup() {
        withFolders {
            "root" {
                withFile("build.gradle", """
buildscript {
    dependencies {
        classpath files($classpathString)
    }
}
apply plugin: 'com.lloydramey.cfn'
""")
                "src" {
                    "main" {
                        "cloudify" {
                            withFile("Account.template.kts", """
import com.lloydramey.cfn.model.aws.ApiGateway
import com.lloydramey.cfn.model.parameters.Types.*
import com.lloydramey.cfn.model.functions.*

val CloudWatchArn = parameter("CloudWatchArn", Str) {
    description = "ARN for cloudwatch"
    default = "test"
}

resource<ApiGateway.Account>("Account") {
    cloudWatchArn = Ref(EnvType)
}
""")
                        }
                    }
                }
            }
        }
    }

    @Test
    fun `project is evaluated and configured correctly`() {
        evaluateProjectWithArguments().build()
    }

    @Test
    fun `compile cloudify executes successfully`() {
        val result = evaluateProjectWithArguments("compileCloudify").build()
        val task = result.task(":compileCloudify")
        assert(task.outcome == SUCCESS)
    }

    private fun evaluateProjectWithArguments(vararg args: String) = GradleRunner.create()
        .withProjectDir(folder("root"))
        .withPluginClasspath()
        .withArguments(*args, "--stacktrace")
        .withGradleVersion(version)
}