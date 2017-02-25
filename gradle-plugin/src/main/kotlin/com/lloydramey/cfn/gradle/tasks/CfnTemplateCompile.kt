package com.lloydramey.cfn.gradle.tasks

import com.lloydramey.cfn.scripting.compileToDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.compile.AbstractCompile
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer
import org.jetbrains.kotlin.cli.common.messages.PrintingMessageCollector
import java.io.File

/**
 * First MVI(?) for the compiler is to have a valid cfn template output into the build directory
 *
 * Second goal will be to output a jar containing resources that can be published
 */
class CfnTemplateCompile(var verbose: Boolean = false) : AbstractCompile() {
    override fun compile() {
        assert(false, { "Unexpected call to compile -> should be calling compileIncremental" })
    }

    @TaskAction
    fun runCompiler() {
        val source = getSource()
        val nonScriptSources = source.files.filterNot(this::isCfnTemplateScript)

        compileToDirectory(
            destinationDir,
            nonScriptSources,
            PrintingMessageCollector(System.err, MessageRenderer.PLAIN_FULL_PATHS, verbose),
            classpath.files
        )

        val scripts = source.files.filter(this::isCfnTemplateScript)
    }

    private fun isCfnTemplateScript(it: File) = it.name.matches(Regex(".*\\.template\\.kts"))

}