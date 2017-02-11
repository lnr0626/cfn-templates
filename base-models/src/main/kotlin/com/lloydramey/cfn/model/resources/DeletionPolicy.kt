package com.lloydramey.cfn.model.resources

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.StdSerializer

@JsonSerialize(using = DeletionPolicySerializer::class)
sealed class DeletionPolicy(val value: String) : ResourceAttribute("DeletionPolicy") {
    object Retain : DeletionPolicy("Retain")
    object Delete : DeletionPolicy("Delete")
    object Snapshot : DeletionPolicy("Snapshot")
}

class DeletionPolicySerializer : StdSerializer<DeletionPolicy>(DeletionPolicy::class.java) {
    override fun serialize(value: DeletionPolicy?, gen: JsonGenerator?, provider: SerializerProvider?) {
        gen?.writeString(value?.value)
    }
}
