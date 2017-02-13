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
package com.lloydramey.cfn.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.lloydramey.cfn.model.functions.Referencable
import com.lloydramey.cfn.model.parameters.Types

@JsonIgnoreProperties("Id")
class Parameter(
        val allowedPattern: String = "",
        val allowedValues: List<String> = listOf(),
        val constraintDescription: String = "",
        val default: String = "",
        val description: String = "",
        val maxLength: Number? = null,
        val maxValue: Number? = null,
        val minLength: Number? = null,
        val minValue: Number? = null,
        val noEcho: String = "",
        val type: ParameterType,
        id: String
) : Referencable(id) {
    init {
        if (isNumberParameter() && areStringOnlyFieldsPopulated()) {
            throw IllegalArgumentException("You cannot specify MinLength, MaxLength, or AllowedPattern for a Number Parameter ($id)")
        } else if(isNotNumberParameter() && areNumberOnlyFieldsPopulated()) {
            throw IllegalArgumentException("You cannot specify MinValue or MaxValue for a Number Parameter ($id)")
        }
    }

    private fun isNumberParameter() = type == Types.Number || type == ParameterType.List(Types.Number)
    private fun isNotNumberParameter() = !isNumberParameter()
    private fun areStringOnlyFieldsPopulated() = minLength != null || maxLength != null || allowedPattern != ""
    private fun areNumberOnlyFieldsPopulated() = minValue != null || minLength != null
}