package com.lloydramey.cfn.model.aws

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.lloydramey.cfn.model.resources.Resource
import com.lloydramey.cfn.model.resources.ResourceProperties

class StringToResourceMapSerializer(type: JavaType) : StdSerializer<Map<String, Resource<ResourceProperties>>>(type) {
    override fun serialize(value: Map<String, Resource<ResourceProperties>>, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()
        value.forEach { key, value -> gen.writeObjectField(key, value.id) }
        gen.writeEndObject()
    }
}
