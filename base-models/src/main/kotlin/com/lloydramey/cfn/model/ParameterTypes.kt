package com.lloydramey.cfn.model

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer

@JsonSerialize(using = ToStringSerializer::class)
sealed class ParameterType(private val serialization: String) {
    data class Single(val type: String) : ParameterType(type)
    data class List(val contents: Single) : ParameterType("List<${contents.type}>")

    override fun toString(): String {
        return serialization
    }
}

object Types {
    val Str = ParameterType.Single("String")
    val Number = ParameterType.Single("Number")
    val CommaDelimitedList = ParameterType.Single("CommaDelimitedList")
}
