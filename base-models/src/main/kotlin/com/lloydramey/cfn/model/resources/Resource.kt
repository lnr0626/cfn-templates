package com.lloydramey.cfn.model.resources

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties("Id", "Attributes")
data class Resource<out T : ResourceProperties>(
        val id: String,
        val attributes: List<ResourceAttribute> = emptyList(),
        val properties: T
) {
    @Suppress("unused")
    val type = properties.type

    @Suppress("unused")
    @JsonAnyGetter
    fun json() = attributes.associateBy { it.name }
}

inline fun <reified T : ResourceProperties> resource(id: String, vararg attributes: ResourceAttribute, init: T.() -> Unit): Resource<T> {
    val properties = T::class.java.newInstance()
    properties.init()
    return Resource(id = id, attributes = attributes.asList(), properties = properties)
}

abstract class ResourceAttribute(val name: String)
abstract class ResourceProperties(@JsonIgnore val type: String)
