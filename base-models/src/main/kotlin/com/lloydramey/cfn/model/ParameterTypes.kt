package com.lloydramey.cfn.model

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer

@JsonSerialize(using = ToStringSerializer::class)
sealed class ParameterType(protected val serialization: String) {
    class Single(type: String) : ParameterType(type)
    class List(contents: Single) : ParameterType("List<${contents.serialization}>")

    override fun toString(): String {
        return serialization
    }
}
