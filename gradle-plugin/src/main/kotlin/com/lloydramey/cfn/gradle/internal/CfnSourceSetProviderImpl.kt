package com.lloydramey.cfn.gradle.internal

import com.lloydramey.cfn.gradle.plugin.CfnSourceSet
import com.lloydramey.cfn.gradle.plugin.CfnSourceSetProvider
import groovy.lang.Closure
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.internal.file.DefaultSourceDirectorySet
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.internal.file.collections.DefaultDirectoryFileTreeFactory
import org.gradle.util.ConfigureUtil

internal class CfnSourceSetProviderImpl constructor(private val fileResolver: FileResolver) : CfnSourceSetProvider {
    override fun create(displayName: String): CfnSourceSet =
        CfnSourceSetImpl(displayName, fileResolver)
}

private class CfnSourceSetImpl(displayName: String, resolver: FileResolver) : CfnSourceSet {
    override val cfn: SourceDirectorySet =
        createDefaultSourceDirectorySet(displayName + " Kotlin source", resolver)

    init {
        cfn.filter?.include("**/*.java", "**/*.kt", "**/*.template.kts")
    }

    override fun kotlin(configureClosure: Closure<Any?>?): CfnSourceSet {
        ConfigureUtil.configure(configureClosure, cfn)
        return this
    }
}

private fun createDefaultSourceDirectorySet(name: String, resolver: FileResolver): SourceDirectorySet {
    val defaultFileTreeFactory = DefaultDirectoryFileTreeFactory()
    return DefaultSourceDirectorySet(name, resolver, defaultFileTreeFactory)
}
