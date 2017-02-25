package com.lloydramey.cfn.gradle.plugin

import groovy.lang.Closure
import org.gradle.api.file.SourceDirectorySet

interface CloudifySourceSet {
    val cloudify: SourceDirectorySet

    fun kotlin(configureClosure: Closure<Any?>?): CloudifySourceSet
}
