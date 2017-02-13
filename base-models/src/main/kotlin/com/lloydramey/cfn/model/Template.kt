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

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.lloydramey.cfn.model.functions.ConditionFunction
import com.lloydramey.cfn.model.resources.Resource
import com.lloydramey.cfn.model.resources.ResourceProperties

internal object Jackson {
    val mapper = jacksonObjectMapper()

    init {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        mapper.propertyNamingStrategy = PropertyNamingStrategy.UPPER_CAMEL_CASE
        mapper.enable(SerializationFeature.INDENT_OUTPUT)
    }
}

@JsonPropertyOrder("AWSTemplateFormatVersion", "Description", "Metadata", "Parameters", "Mappings", "Conditions", "Resources", "Outputs")
data class Template(
        var description: String = "",
        val metadata: MutableMap<String, Any> = mutableMapOf(),
        val parameters: MutableMap<String, Parameter> = mutableMapOf(),
        val mappings: MutableMap<String, Mapping> = mutableMapOf(),
        val conditions: MutableMap<String, ConditionFunction> = mutableMapOf(),
        val resources: MutableMap<String, Resource<ResourceProperties>> = mutableMapOf(),
        val outputs: MutableMap<String, Output> = mutableMapOf()
) {
    @JsonProperty("AWSTemplateFormatVersion")
    val version = "2010-09-09"

    override fun toString(): String {
        return Jackson.mapper.writeValueAsString(this)
    }
}