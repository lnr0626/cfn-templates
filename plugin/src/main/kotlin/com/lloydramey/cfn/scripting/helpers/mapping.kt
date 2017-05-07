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
package com.lloydramey.cfn.scripting.helpers

import com.lloydramey.cfn.model.Mapping
import com.lloydramey.cfn.scripting.CfnTemplateScript
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.memberProperties

class MappingDelegate(val init: MappingDefinition.() -> Unit) : ReadOnlyProperty<CfnTemplateScript, Mapping> {
    val mapping: MappingDefinition by lazy {
        val def = MappingDefinition()
        def.init()
        def
    }

    override fun getValue(thisRef: CfnTemplateScript, property: KProperty<*>): Mapping = mapping.toMapping(property.name)
}

class MappingDefinition {
    internal val mappings = mutableMapOf<String, Map<String, String>>()

    fun key(key: String, value: Any) {
        val map = value::class.memberProperties.associate { it.name to it.call(value).toString() }
        key(key, map)
    }

    fun key(key: String, vararg values: Pair<String, String>) {
        key(key, values.toMap())
    }

    fun key(key: String, values: Map<String, String>) {
        if (key in mappings) {
            throw IllegalArgumentException("Duplicate key ($key) found for mapping")
        }
        mappings[key] = values.toMap()
    }

    internal fun toMapping(id: String) = Mapping(id, mappings)
}
