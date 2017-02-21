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

import com.fasterxml.jackson.annotation.JsonIgnore
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
