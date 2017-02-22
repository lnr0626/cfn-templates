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
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.lloydramey.cfn.model.functions.Referencable
import com.lloydramey.cfn.model.parameters.Types

@JsonIgnoreProperties("Id")
class Parameter(
    id: String,
    val type: ParameterType
) : Referencable(id) {
    var allowedValues: List<String> = listOf()
    var constraintDescription: String = ""
    var description: String = ""
    @JsonSerialize(using = ToStringSerializer::class) var noEcho: Boolean? = null
    var allowedPattern: String = ""
        set(value) {
            throwIfIsNumber("AllowedPattern")
            field = value
        }
    var default: String = ""
        set(value) {
            if(isNumberParameter() && value.toLongOrNull() == null) {
                throw IllegalArgumentException("Default must be a valid number for Number Parameters")
            }
        }
    @JsonSerialize(using = ToStringSerializer::class) var maxLength: Number? = null
        set(value) {
            throwIfIsNumber("MaxLength")
            field = value
        }
    @JsonSerialize(using = ToStringSerializer::class) var minLength: Number? = null
        set(value) {
            throwIfIsNumber("MinLength")
            field = value
        }
    @JsonSerialize(using = ToStringSerializer::class) var maxValue: Number? = null
        set(value) {
            throwIfIsNotNumber("MaxValue")
            field = value
        }
    @JsonSerialize(using = ToStringSerializer::class) var minValue: Number? = null
        set(value) {
            throwIfIsNotNumber("MinValue")
            field = value
        }

    private fun isNumberParameter() = type == Types.Number || type == ParameterType.List(Types.Number)
    private fun isNotNumberParameter() = !isNumberParameter()

    private fun throwIfIsNotNumber(fieldName: String) {
        if (isNotNumberParameter()) {
            throw IllegalArgumentException("You cannot specify $fieldName for a Number Parameter")
        }
    }
    private fun throwIfIsNumber(fieldName: String) {
        if (isNumberParameter()) {
            throw IllegalArgumentException("You cannot specify $fieldName for a Number Parameter")
        }
    }
}