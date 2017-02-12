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