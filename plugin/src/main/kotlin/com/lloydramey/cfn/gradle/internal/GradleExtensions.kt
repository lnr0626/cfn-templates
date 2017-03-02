package com.lloydramey.cfn.gradle.internal

import org.gradle.api.internal.HasConvention
import org.gradle.api.plugins.Convention
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.PluginManager
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

annotation class OpenForGradle