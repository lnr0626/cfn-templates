package com.lloydramey.cfn.gradle.tasks

import com.lloydramey.cfn.gradle.FolderBasedTest
import org.gradle.testkit.runner.GradleRunner
import org.junit.Test

class CfnTemplateCompileTest(val version: String) : FolderBasedTest() {
    @Test
    fun `project with single template file`() {
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

        val buildResult = GradleRunner.create()
            .withProjectDir(folder("root"))
            .withPluginClasspath()
            .withGradleVersion(version)
            .build()

        println(buildResult.output)
    }
}