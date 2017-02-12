package com.lloydramey.cfn.model

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.StdSerializer

@JsonSerialize(using = ParameterTypeSerializer::class)
sealed class ParameterType(internal val serialization: String) {
    data class Single(val type: String) : ParameterType(type)
    data class List(val contents: Single) : ParameterType("List<${contents.type}>")
}

object Types {
    val Str = ParameterType.Single("String")
    val Number = ParameterType.Single("Number")
    val CommaDelimitedList = ParameterType.Single("CommaDelimitedList")
}


class ParameterTypeSerializer : StdSerializer<ParameterType>(ParameterType::class.java) {
    override fun serialize(value: ParameterType?, gen: JsonGenerator?, provider: SerializerProvider?) {
        gen?.writeString(value?.serialization)
    }

}