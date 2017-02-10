package com.lloydramey.cfn.model

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

private object Jackson {
    val mapper = jacksonObjectMapper()

    init {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        mapper.propertyNamingStrategy = PropertyNamingStrategy.UPPER_CAMEL_CASE
        mapper.enable(SerializationFeature.INDENT_OUTPUT)
    }
}

@JsonPropertyOrder("AWSTemplateFormatVersion", "Description", "Parameters", "Mappings", "Conditions", "Resources", "Outputs")
class Template(
        var description: String,
        var parameters: Map<String, Any>,
        var mappings: Map<String, Any>,
        var conditions: Map<String, Any>,
        var resources: Map<String, Any>,
        var outputs: Map<String, Any>
) {
    @JsonProperty("AWSTemplateFormatVersion")
    val version = "2010-09-09"


    override fun toString(): String {
        return Jackson.mapper.writeValueAsString(this)
    }
}