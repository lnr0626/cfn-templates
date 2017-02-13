package com.lloydramey.cfn.model.resources

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.lloydramey.cfn.model.functions.ReferencableWithAttributes

abstract class ResourceDefinitionAttribute(@JsonIgnore val name: String)
abstract class ResourceProperties(@JsonIgnore val resourceType: String) {
    open fun validate() = {}
}

@JsonIgnoreProperties("Id", "Attributes")
class Resource<out T : ResourceProperties>(
        id: String,
        @JsonIgnore val attributes: List<ResourceDefinitionAttribute> = emptyList(),
        val properties: T
) : ReferencableWithAttributes(id) {
    @Suppress("unused")
    val type = properties.resourceType

    @Suppress("unused")
    @JsonAnyGetter
    fun json() = attributes.associateBy { it.name }

}

inline fun <reified T : ResourceProperties> resource(id: String, vararg attributes: ResourceDefinitionAttribute, init: T.() -> Unit): Resource<T> {
    val properties = T::class.java.newInstance()
    properties.init()
    properties.validate()
    return Resource(id = id, attributes = attributes.asList(), properties = properties)
}


