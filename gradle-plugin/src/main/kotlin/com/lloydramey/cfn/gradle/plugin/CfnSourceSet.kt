package com.lloydramey.cfn.gradle.plugin

import groovy.lang.Closure
import org.gradle.api.file.SourceDirectorySet

interface CfnSourceSet {
    val cfn: SourceDirectorySet

    fun kotlin(configureClosure: Closure<Any?>?): CfnSourceSet
}
