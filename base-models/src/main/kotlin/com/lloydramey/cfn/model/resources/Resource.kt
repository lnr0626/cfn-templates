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
package com.lloydramey.cfn.model.resources

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.lloydramey.cfn.model.functions.ReferencableWithAttributes
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

class RequiredAttributeMissingException(vararg attributeName: String) :
    IllegalStateException("Missing required parameters: ${attributeName.joinToString(", ")}")

@Retention(AnnotationRetention.RUNTIME)
annotation class Required

abstract class ResourceProperties(@JsonIgnore val resourceType: String) {
    open fun validate() {
        val missingRequiredProperties = this::class.memberProperties
            .filter { it.isLateinit || hasAnnotation(it, Required::class) }
            .filter { it.getter.call() == null }
            .map { it.name }
            .toTypedArray()

        if (missingRequiredProperties.isNotEmpty()) {
            throw RequiredAttributeMissingException(*missingRequiredProperties)
        }
    }

    private fun hasAnnotation(property: KProperty1<out ResourceProperties, Any?>, annotation: KClass<out Annotation>): Boolean {
        return property.annotations.any { annotation.isInstance(it) }
    }
}
abstract class ResourceDefinitionAttribute(@JsonIgnore val name: String)

@Suppress("unused")
@JsonIgnoreProperties("Id", "Attributes")
class Resource<out T : ResourceProperties>(
    id: String,
    @JsonIgnore val attributes: List<ResourceDefinitionAttribute> = emptyList(),
    val properties: T
) : ReferencableWithAttributes(id) {
    val type = properties.resourceType

    @JsonAnyGetter
    fun json() = attributes.associateBy { it.name }

}

inline fun <reified T : ResourceProperties> resource(id: String, vararg attributes: ResourceDefinitionAttribute, init: T.() -> Unit): Resource<T> {
    val properties = T::class.java.newInstance()
    properties.init()
    properties.validate()
    return Resource(id = id, attributes = attributes.asList(), properties = properties)
}

