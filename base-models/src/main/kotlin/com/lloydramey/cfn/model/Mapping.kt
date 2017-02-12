package com.lloydramey.cfn.model

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonIgnore

data class Mapping(
        @JsonIgnore val id: String,
        @JsonIgnore val mapping: MutableMap<String, MutableMap<String, String>> = mutableMapOf()
) {
    @JsonAnyGetter
    fun json() = mapping
}