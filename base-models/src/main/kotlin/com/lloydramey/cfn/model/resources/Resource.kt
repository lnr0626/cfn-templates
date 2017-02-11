package com.lloydramey.cfn.model.resources

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonIgnore

data class Resource<out T : ResourceProperties>(
        @JsonIgnore val id: String,
        val properties: T,
        @JsonIgnore val attributes: List<ResourceAttribute> = emptyList()
) {
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

class Blah(var blah: String = "") : ResourceProperties("Test")

object Test {
    val t = resource<Blah>("Test") {
        blah = "asdf"
    }
}
