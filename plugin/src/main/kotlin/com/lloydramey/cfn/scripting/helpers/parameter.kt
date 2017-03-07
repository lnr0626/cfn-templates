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

import com.lloydramey.cfn.model.Parameter
import com.lloydramey.cfn.model.ParameterType
import com.lloydramey.cfn.model.parameters.Types.Number
import com.lloydramey.cfn.scripting.CfnTemplateScript
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ParameterDelegate(type: ParameterType, init: ParameterDefinition.() -> Unit) : ReadOnlyProperty<CfnTemplateScript, Parameter> {
    val param = ParameterDefinition(type = type)

    init {
        param.init()
    }

    override fun getValue(thisRef: CfnTemplateScript, property: KProperty<*>) = Parameter(
        id = property.name,
        type = param.type,
        allowedPattern = param.allowedPattern,
        allowedValues = param.allowedValues,
        constraintDescription = param.constraintDescription,
        default = param.default,
        description = param.description,
        noEcho = param.noEcho?.toString(),
        maxLength = param.maxLength?.toString(),
        maxValue = param.maxValue?.toString(),
        minLength = param.minLength?.toString(),
        minValue = param.minValue?.toString()
    )

}

class ParameterDefinition(val type: ParameterType) {
    var allowedValues: List<String> = listOf()
    var constraintDescription: String = ""
    var description: String = ""
    var noEcho: Boolean? = null
    var allowedPattern: String? by NotAllowedIfNumberParam()
    var default: String? by MustBeValidNumberIfNumberParam()
    var maxLength: Number? by NotAllowedIfNumberParam()
    var minLength: Number? by NotAllowedIfNumberParam()
    var maxValue: Number? by OnlyAllowedInNumberParameters()
    var minValue: Number? by OnlyAllowedInNumberParameters()

    internal fun isNumberParameter() = type == Number || type == ParameterType.List(Number)
    internal fun isNotNumberParameter() = !isNumberParameter()

}

class MustBeValidNumberIfNumberParam : ReadWriteProperty<ParameterDefinition, String?> {
    var value: String? = null

    override fun setValue(thisRef: ParameterDefinition, property: KProperty<*>, value: String?) {
        if (thisRef.isNumberParameter() && value?.toLongOrNull() == null) {
            throw IllegalArgumentException("${property.name} must be a valid number for a Number Parameter")
        }
        this.value = value
    }

    override fun getValue(thisRef: ParameterDefinition, property: KProperty<*>): String? = value
}

class NotAllowedIfNumberParam<T> : ReadWriteProperty<ParameterDefinition, T?> {
    var value: T? = null

    override fun setValue(thisRef: ParameterDefinition, property: KProperty<*>, value: T?) {
        if (thisRef.isNumberParameter()) {
            throw IllegalArgumentException("You cannot specify ${property.name} for a Number Parameter")
        }
        this.value = value
    }

    override fun getValue(thisRef: ParameterDefinition, property: KProperty<*>): T? = value
}

class OnlyAllowedInNumberParameters<T> : ReadWriteProperty<ParameterDefinition, T?> {
    var value: T? = null

    override fun setValue(thisRef: ParameterDefinition, property: KProperty<*>, value: T?) {
        if (thisRef.isNotNumberParameter()) {
            throw IllegalArgumentException("You cannot specify ${property.name} for a non Number Parameter")
        }
        this.value = value
    }

    override fun getValue(thisRef: ParameterDefinition, property: KProperty<*>): T? = value
}