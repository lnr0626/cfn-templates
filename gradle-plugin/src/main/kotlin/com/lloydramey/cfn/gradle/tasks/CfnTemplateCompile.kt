package com.lloydramey.cfn.gradle.tasks

import org.gradle.api.tasks.compile.AbstractCompile

/**
 * First MVI(?) for the compiler is to have a valid cfn template output into the build directory
 *
 * Second goal will be to output a jar containing resources that can be published
 */
class CfnTemplateCompile : AbstractCompile() {
    override fun compile() {

    }
}