package com.lloydramey.cfn.model.resources

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.lloydramey.cfn.model.Condition

@JsonSerialize(using = MetadataAttributeSerializer::class)
class MetadataAttr(@JsonIgnore val properties: Map<String, Any>) : ResourceAttribute("Metadata")

class MetadataAttributeSerializer : StdSerializer<MetadataAttr>(MetadataAttr::class.java) {
    override fun serialize(value: MetadataAttr?, gen: JsonGenerator?, provider: SerializerProvider?) {
        gen?.writeStartObject()
        value?.properties?.forEach { (key, value) ->
            gen?.writeObjectField(key, value)
        }
        gen?.writeEndObject()
    }

}
