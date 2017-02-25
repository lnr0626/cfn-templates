package com.lloydramey.cfn.gradle.internal

import com.lloydramey.cfn.gradle.plugin.CloudifySourceSet
import com.lloydramey.cfn.gradle.plugin.CloudifySourceSetProvider
import groovy.lang.Closure
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.internal.file.DefaultSourceDirectorySet
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.internal.file.collections.DefaultDirectoryFileTreeFactory
import org.gradle.util.ConfigureUtil

internal class CloudifySourceSetProviderImpl constructor(private val fileResolver: FileResolver) : CloudifySourceSetProvider {
    override fun create(displayName: String): CloudifySourceSet =
        CloudifySourceSetImpl(displayName, fileResolver)
}

private class CloudifySourceSetImpl(displayName: String, resolver: FileResolver) : CloudifySourceSet {
    override val cloudify: SourceDirectorySet =
        createDefaultSourceDirectorySet(displayName + " Kotlin source", resolver)

    init {
        cloudify.filter?.include("**/*.kt", "**/*.template.kts")
    }

    override fun kotlin(configureClosure: Closure<Any?>?): CloudifySourceSet {
        ConfigureUtil.configure(configureClosure, cloudify)
        return this
    }
}

private fun createDefaultSourceDirectorySet(name: String, resolver: FileResolver): SourceDirectorySet {
    val defaultFileTreeFactory = DefaultDirectoryFileTreeFactory()
    return DefaultSourceDirectorySet(name, resolver, defaultFileTreeFactory)
}
