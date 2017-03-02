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

import org.gradle.api.file.FileCollection
import org.gradle.api.internal.HasConvention
import org.gradle.api.plugins.Convention
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.PluginManager
import org.gradle.api.tasks.compile.AbstractCompile
import kotlin.reflect.KClass

internal fun PluginManager.apply(clazz: KClass<*>) = apply(clazz.java)

internal fun <T : Any> Convention.getPlugin(clazz: KClass<T>) = getPlugin(clazz.java)

internal inline fun <reified T : Any> Any.addConvention(name: String, plugin: T) {
    (this as HasConvention).convention.plugins[name] = plugin
}

internal inline fun <reified T : Any> Any.addExtension(name: String, extension: T) =
    (this as ExtensionAware).extensions.add(name, extension)

internal fun Any.getConvention(name: String): Any? =
    (this as HasConvention).convention.plugins[name]

internal fun AbstractCompile.mapClasspath(fn: () -> FileCollection) {
    conventionMapping.map("classpath", fn)
}

annotation class OpenForGradle