package com.lloydramey.cfn.model.resources

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.StdSerializer

@JsonSerialize(using = DependsOnSerializer::class)
data class DependsOn(val res: Resource<ResourceProperties>, val resources: List<Resource<ResourceProperties>>) : ResourceDefinitionAttribute("DependsOn") {
    constructor(res: Resource<ResourceProperties>, vararg resources: Resource<ResourceProperties>) : this(res, resources.asList())
}

class DependsOnSerializer : StdSerializer<DependsOn>(DependsOn::class.java) {
    override fun serialize(value: DependsOn?, gen: JsonGenerator?, provider: SerializerProvider?) {
        val dependsOn = value ?: return

        if(dependsOn.resources.isEmpty()) {
            gen?.writeString(dependsOn.res.id)
        } else {
            gen?.writeStartArray()
            gen?.writeString(dependsOn.res.id)
            dependsOn.resources.forEach {
                gen?.writeString(it.id)
            }
            gen?.writeEndArray()
        }
    }
}
