package com.lloydramey.cfn.model.resources

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.lloydramey.cfn.model.functions.ReferencableWithAttributes

abstract class ResourceDefinitionAttribute(@JsonIgnore val name: String)
abstract class ResourceProperties(@JsonIgnore val type: String)

@JsonIgnoreProperties("Id", "Attributes")
class Resource<out T : ResourceProperties>(
        id: String,
        @JsonIgnore val attributes: List<ResourceDefinitionAttribute> = emptyList(),
        val properties: T
) : ReferencableWithAttributes(id) {
    @Suppress("unused")
    val type = properties.type

    @Suppress("unused")
    @JsonAnyGetter
    fun json() = attributes.associateBy { it.name }

}

inline fun <reified T : ResourceProperties> resource(id: String, vararg attributes: ResourceDefinitionAttribute, init: T.() -> Unit): Resource<T> {
    val properties = T::class.java.newInstance()
    properties.init()
    return Resource(id = id, attributes = attributes.asList(), properties = properties)
}


