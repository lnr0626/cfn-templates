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
import com.lloydramey.cfn.scripting.helpers.ConditionDelegate
import com.lloydramey.cfn.scripting.helpers.MappingDefinition
import com.lloydramey.cfn.scripting.helpers.MappingDelegate
import com.lloydramey.cfn.scripting.helpers.MetadataDelegate
import com.lloydramey.cfn.scripting.helpers.OutputDefinition
import com.lloydramey.cfn.scripting.helpers.OutputDelegate
import com.lloydramey.cfn.scripting.helpers.ParameterDefinition
import com.lloydramey.cfn.scripting.helpers.ParameterDelegate
import com.lloydramey.cfn.scripting.helpers.ResourceDelegate
import com.lloydramey.cfn.scripting.helpers.TemplateMetadata
import org.jetbrains.kotlin.script.ScriptTemplateDefinition
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubtypeOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType

typealias MappingInitializer = MappingDefinition.() -> Unit
typealias ParameterInitializer = ParameterDefinition.() -> Unit
typealias LazyCondition = () -> ConditionFunction
typealias PropertyInitializer<T> = T.() -> Unit
typealias OutputInitializer = OutputDefinition.() -> Unit

@Suppress("unused")
@ScriptTemplateDefinition(
    resolver = CfnTemplateDependencyResolver::class,
    scriptFilePattern = ".*\\.template\\.kts"
)
abstract class CfnTemplateScript {
    protected var description: String = ""

    protected fun metadata(value: Any) = MetadataDelegate(value)

    protected fun mapping(init: MappingInitializer) = MappingDelegate(init)

    protected fun parameter(type: ParameterType, init: ParameterInitializer) = ParameterDelegate(type, init)

    protected fun condition(func: LazyCondition) = ConditionDelegate(func)

    protected inline fun <reified T : ResourceProperties> resource(
        vararg attributes: ResourceDefinitionAttribute,
        init: PropertyInitializer<T>
    ): ResourceDelegate<T> {

        val clazz = T::class
        requireDefaultNoArgConstructor(clazz)
        val properties = clazz.primaryConstructor!!.call()
        properties.init()
        properties.validate()
        return ResourceDelegate(properties, attributes.asList())
    }

    protected fun output(condition: ConditionalOn? = null, init: OutputInitializer) = OutputDelegate(condition, init)

    internal fun toTemplate() = Template(
        description = description,
        parameters = parameters,
        metadata = metadata,
        mappings = mappings,
        conditions = conditions,
        resources = resources,
        outputs = outputs
    )

    private val outputs: Map<String, Output>
        get() = getPropertiesOfAType<Output>().associateBy { it.id }

    private val resources: Map<String, Resource<ResourceProperties>>
        get() = getPropertiesOfAType<Resource<ResourceProperties>>().associateBy { it.id }

    private val conditions: Map<String, ConditionFunction>
        get() = getPropertiesOfAType<Condition>().associate { it.id to it.condition }

    private val parameters: Map<String, Parameter>
        get() = getPropertiesOfAType<Parameter>().associateBy { it.id }

    private val metadata: Map<String, Any>
        get() = getPropertiesOfAType<TemplateMetadata>().associate { it.name to it.value }

    private val mappings: Map<String, Mapping>
        get() = getPropertiesOfAType<Mapping>().associateBy { it.id }

    private inline fun <reified T : Any> getPropertiesOfAType(): List<T> {
        return this::class.memberProperties
            .filter { it.returnType.isSubtypeOf(T::class.starProjectedType) }
            .flatMap { listOf(it.call(this) as T?).filterNotNull() }
    }
}

fun requireDefaultNoArgConstructor(clazz: KClass<out ResourceProperties>) {
    if (clazz.primaryConstructor == null) {
        throw IllegalStateException("${clazz.qualifiedName} must declare a primary constructor")
    }

    if (!clazz.primaryConstructor!!.parameters.all { it.isOptional }) {
        throw IllegalStateException("${clazz.qualifiedName} must not have any required parameters in it's primary constructor")
    }
}