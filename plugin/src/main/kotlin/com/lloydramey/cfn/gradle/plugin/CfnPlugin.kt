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

import com.lloydramey.cfn.gradle.internal.*
import com.lloydramey.cfn.gradle.tasks.CfnTemplateCompile
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.internal.tasks.DefaultSourceSet
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import javax.inject.Inject

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

            sourceSet.addConvention("cloudify", cfnSourceSet)
            sourceSet.resources.filter?.exclude {
                cfnSourceSet.cloudify.contains(it.file)
            }
            sourceSet.allSource.source(cfnSourceSet.cloudify)

            val compileTaskName = sourceSet.getCompileTaskName("cloudify")
            val compile = p.tasks.create(compileTaskName, CfnTemplateCompile::class.java)

            compile.description = "Compile the ${sourceSet.name} Cloudify source."
            compile.setSource(cfnSourceSet.cloudify)

            p.tasks.getByName(sourceSet.classesTaskName).dependsOn(compileTaskName)
        }
    }
}
