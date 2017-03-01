package com.lloydramey.cfn.gradle.tasks

import com.lloydramey.cfn.gradle.FolderBasedTest
import org.gradle.testkit.runner.GradleRunner
import org.junit.Before
import org.junit.Test

class SingleTemplateProjectWithDefaultSourceSet(val version: String) : FolderBasedTest() {
    @Before
    fun setup() {
        withFolders {
            "root" {
                withFile("build.gradle", """
apply plugin: 'com.lloydramey.cfn'
""")
                "src" {
                    "main" {
                        "cfn" {
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
        evaluateProjectWithArguments()
    }

    private fun evaluateProjectWithArguments(vararg args: String) {
        GradleRunner.create()
            .withProjectDir(folder("root"))
            .withPluginClasspath()
            .withArguments(*args)
            .withGradleVersion(version)
            .build()
    }
}