package com.lloydramey.cfn.model

import com.fasterxml.jackson.annotation.JsonAnyGetter

data class Mapping(
        val id: String,
        val mapping: MutableMap<String, MutableMap<String, String>> = mutableMapOf()
) {
    @JsonAnyGetter
    fun json() = mapping
}