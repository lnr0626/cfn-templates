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
import com.lloydramey.cfn.scripting.Slf4jMessageCollector
import com.lloydramey.cfn.scripting.compileScriptToDirectory
import com.lloydramey.cfn.scripting.withFullPaths
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.compile.AbstractCompile

/**
 * First MVI(?) for the compiler is to have a valid cfn template output into the build directory
 *
 * Second goal will be to output a jar containing resources that can be published
 */
@OpenForGradle
class CfnTemplateCompile : AbstractCompile() {
    override fun compile() {
        assert(false, { "Unexpected call to compile -> should be calling compileIncremental" })
    }

    @TaskAction
    fun runCompiler() {
        destinationDir.deleteRecursively()


        val files = getSource().files

        val success = compileScriptToDirectory(
            destinationDir,
            files,
            Slf4jMessageCollector(logger, withFullPaths),
            classpath.files
        )

        if (!success) {
            throw GradleException("Compilation failed, see log for details")
        }
    }

}
