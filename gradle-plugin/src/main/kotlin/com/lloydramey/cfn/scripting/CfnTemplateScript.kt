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
package com.lloydramey.cfn.scripting

import com.lloydramey.cfn.model.Mapping
import com.lloydramey.cfn.model.Output
import com.lloydramey.cfn.model.Parameter
import com.lloydramey.cfn.model.ParameterType
import com.lloydramey.cfn.model.Template
import com.lloydramey.cfn.model.functions.Condition
import com.lloydramey.cfn.model.functions.ConditionFunction
import com.lloydramey.cfn.model.resources.Resource
import com.lloydramey.cfn.model.resources.ResourceProperties
import com.lloydramey.cfn.model.resources.attributes.ConditionalOn
import com.lloydramey.cfn.model.resources.attributes.ResourceDefinitionAttribute
import org.jetbrains.kotlin.script.ScriptTemplateDefinition
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

@DslMarker
annotation class TemplateClassMarker

@ScriptTemplateDefinition(
    scriptFilePattern = ".*\\.template\\.kts"
)
@TemplateClassMarker
abstract class CfnTemplateScript {
    var description: String = ""
    val metadata = mutableMapOf<String, Any>()
    val parameters = mutableListOf<Parameter>()
    val mappings = mutableListOf<Mapping>()
    val conditions = mutableListOf<Condition>()
    val resources = mutableListOf<Resource<ResourceProperties>>()
    val outputs = mutableListOf<Output>()

    fun metadata(id: String, value: Any) {
        if (id in metadata) {
            throw IllegalArgumentException("Duplicate id found for Metadata: $id")
        }
        metadata[id] = value
    }

    fun mapping(id: String, init: MappingDefinition.() -> Unit): Mapping {
        if (id in mappings.map { it.id }) {
            throw IllegalArgumentException("Duplicate Mapping id name $id")
        }

        val def = MappingDefinition(id)
        def.init()
        val mapping = def.toMapping()
        mappings.add(mapping)
        return mapping
    }

    fun parameter(id: String, type: ParameterType, init: Parameter.() -> Unit): Parameter {
        if (id in parameters.map { it.id }) {
            throw IllegalArgumentException("Duplicate Parameter named $id")
        }
        val param = Parameter(id, type)
        param.init()
        parameters.add(param)
        return param
    }

    fun condition(id: String, func: ConditionFunction): Condition {
        if (id in conditions.map { it.id }) {
            throw IllegalArgumentException("Duplicate Condition named $id")
        }
        val cond = Condition(id, func)
        conditions.add(cond)
        return cond
    }

    inline fun <reified T : ResourceProperties> resource(id: String, vararg attributes: ResourceDefinitionAttribute, init: T.() -> Unit): Resource<T> {
        if (id in resources.map { it.id }) {
            throw IllegalArgumentException("Duplicate Resource named $id")
        }
        val clazz = T::class
        requireDefaultNoArgConstructor(clazz)
        val properties = clazz.primaryConstructor!!.call()
        properties.init()
        properties.validate()
        val res = Resource(id = id, attributes = attributes.asList(), properties = properties)
        resources.add(res)
        return res
    }

    fun output(id: String, condition: ConditionalOn? = null, init: Output.() -> Unit): Output {
        if (id in outputs.map { it.id }) {
            throw IllegalArgumentException("Duplicate Output named $id")
        }
        val output = Output(id, condition)
        output.init()

        if (output.value == null) {
            throw IllegalArgumentException("You must initialize the Value attribute for the Output named $id")
        }

        outputs.add(output)
        return output
    }

    internal fun toTemplate() = Template(
        description = description,
        parameters = parameters.associateBy { it.id },
        metadata = metadata,
        mappings = mappings.associateBy { it.id },
        conditions = conditions.associate { it.id to it.condition },
        resources = resources.associateBy { it.id },
        outputs = outputs.associateBy { it.id }
    )
}

class MappingDefinition(internal val id: String) {
    internal val mappings = mutableMapOf<String, Map<String, String>>()
    fun key(key: String, vararg values: Pair<String, String>) {
        if (key in mappings) {
            throw IllegalArgumentException("Duplicate Key ($key) found for mapping $id")
        }

        mappings[key] = values.toMap()
    }

    internal fun toMapping() = Mapping(id, mappings)
}

fun requireDefaultNoArgConstructor(clazz: KClass<out ResourceProperties>) {
    if (clazz.primaryConstructor == null) {
        throw IllegalStateException("${clazz.qualifiedName} must declare a primary constructor")
    }

    if (!clazz.primaryConstructor!!.parameters.all { it.isOptional }) {
        throw IllegalStateException("${clazz.qualifiedName} must not have any required parameters in it's primary constructor")
    }
}