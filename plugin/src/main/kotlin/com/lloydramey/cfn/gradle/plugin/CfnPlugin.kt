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
package com.lloydramey.cfn.gradle.plugin

import com.lloydramey.cfn.gradle.internal.CloudifySourceSetProviderImpl
import com.lloydramey.cfn.gradle.internal.OpenForGradle
import com.lloydramey.cfn.gradle.internal.addConvention
import com.lloydramey.cfn.gradle.internal.apply
import com.lloydramey.cfn.gradle.internal.getPlugin
import com.lloydramey.cfn.gradle.internal.mapClasspath
import com.lloydramey.cfn.gradle.tasks.CfnTemplateCompile
import com.lloydramey.cfn.gradle.tasks.CfnTemplateToJson
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.internal.tasks.DefaultSourceSet
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import java.io.File
import javax.inject.Inject

@Suppress("unused")
@OpenForGradle
class CfnPlugin @Inject constructor(val fileResolver: FileResolver) : Plugin<Project> {
    override fun apply(p: Project) {
        p.pluginManager.apply(JavaPlugin::class)
        p.pluginManager.apply(KotlinPluginWrapper::class)

        val provider = CloudifySourceSetProviderImpl(fileResolver)
        val java = p.convention.getPlugin(JavaPluginConvention::class)

        configureSourceSetDefaults(p, java, provider)
    }

    private fun configureSourceSetDefaults(p: Project, java: JavaPluginConvention, provider: CloudifySourceSetProvider) {
        java.sourceSets?.all { sourceSet ->
            val cfnSourceSet = provider.create((sourceSet as DefaultSourceSet).displayName)
            cfnSourceSet.cloudify.srcDir("src/${sourceSet.name}/cloudify")

            sourceSet.addConvention("cloudify", cfnSourceSet)
            sourceSet.resources.filter?.exclude { it.file in cfnSourceSet.cloudify }
            sourceSet.allSource.source(cfnSourceSet.cloudify)

            val compileTaskName = sourceSet.getCompileTaskName("cloudify")
            val compile = p.tasks.create(compileTaskName, CfnTemplateCompile::class.java)

            compile.description = "Compile the ${sourceSet.name} Cloudify source."
            compile.setSource(cfnSourceSet.cloudify)
            compile.destinationDir = sourceSet.output.classesDir
            compile.mapClasspath { sourceSet.compileClasspath }

            val toJsonName = sourceSet.getCompileTaskName("cloudifyToJson")
            val toJson = p.tasks.create(toJsonName, CfnTemplateToJson::class.java)

            toJson.description = "Convert the compiled ${sourceSet.name} cloudify templates into json"
            toJson.setSource(cfnSourceSet.allCloudify)
            toJson.destinationDir = File(p.buildDir, "cloudify-templates")
            toJson.mapClasspath { sourceSet.runtimeClasspath }
            toJson.dependsOn(compile)

            p.tasks.getByName(sourceSet.classesTaskName).dependsOn(compileTaskName)
        }
    }
}

