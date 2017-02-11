package com.lloydramey.cfn.model.resources

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.lloydramey.cfn.model.Condition

@JsonSerialize(using = ConditionAttributeSerializer::class)
data class ConditionAttr(val condition: Condition) : ResourceAttribute("Condition")

class ConditionAttributeSerializer : StdSerializer<ConditionAttr>(ConditionAttr::class.java) {
    override fun serialize(value: ConditionAttr?, gen: JsonGenerator?, provider: SerializerProvider?) {
        gen?.writeString(value?.condition?.id)
    }
}

