package com.lloydramey.cfn.model

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonIgnore

data class Mapping(
        @JsonIgnore val id: String,
        @JsonIgnore val mapping: Map<String, Map<String, String>> = mutableMapOf()
) {
    @JsonAnyGetter
    fun json() = mapping
}