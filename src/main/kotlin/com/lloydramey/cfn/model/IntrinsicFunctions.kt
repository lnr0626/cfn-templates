package com.lloydramey.cfn.model

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonIgnore

interface IntrinsicFunction

abstract class Fn(@JsonIgnore val name: String, @JsonIgnore val value: Any) {
    @JsonAnyGetter
    fun json() = mapOf(name to value)
}
