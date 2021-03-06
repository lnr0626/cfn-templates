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
package com.lloydramey.cfn.gradle.internal

import com.lloydramey.cfn.gradle.plugin.CloudifySourceSet
import com.lloydramey.cfn.gradle.plugin.CloudifySourceSetProvider
import groovy.lang.Closure
import mu.KLogging
import org.gradle.api.Action
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
    companion object : KLogging()

    override val cloudify: SourceDirectorySet =
        createDefaultSourceDirectorySet(displayName + " Cloud Formation Template source", resolver)

    override val allCloudify: SourceDirectorySet =
        createDefaultSourceDirectorySet(displayName + " Cloud Formation Template source", resolver)

    init {
        cloudify.filter?.include("**/*.kt", "**/*.template.kts")
        allCloudify.source(cloudify)
        allCloudify.filter?.include("**/*.template.kts")

        logger.trace { "Creating source set for $displayName" }
    }

    override fun cloudify(configureClosure: Closure<Any?>?): CloudifySourceSet {
        ConfigureUtil.configure(configureClosure, cloudify)
        return this
    }

    override fun cloudify(configureAction: Action<SourceDirectorySet>?): CloudifySourceSet {
        configureAction?.execute(this.cloudify)
        return this
    }
}

private fun createDefaultSourceDirectorySet(name: String, resolver: FileResolver): SourceDirectorySet {
    val defaultFileTreeFactory = DefaultDirectoryFileTreeFactory()
    return DefaultSourceDirectorySet(name, resolver, defaultFileTreeFactory)
}
