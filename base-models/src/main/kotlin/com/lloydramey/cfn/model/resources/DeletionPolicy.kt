package com.lloydramey.cfn.model.resources

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer

@JsonSerialize(using = ToStringSerializer::class)
sealed class DeletionPolicy(val value: String) : ResourceAttribute("DeletionPolicy") {
    object Retain : DeletionPolicy("Retain")
    object Delete : DeletionPolicy("Delete")
    object Snapshot : DeletionPolicy("Snapshot")

    override fun toString(): String {
        return this.value
    }
}
