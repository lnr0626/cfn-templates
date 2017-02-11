package com.lloydramey.cfn.model.resources

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.lloydramey.cfn.model.Condition

abstract class ResourceAttribute(val name: String)

@JsonSerialize(using = DeletionPolicySerializer::class)
sealed class DeletionPolicy(val value: String) : ResourceAttribute("DeletionPolicy") {
    object Retain : DeletionPolicy("Retain")
    object Delete : DeletionPolicy("Delete")
    object Snapshot : DeletionPolicy("Snapshot")
}

class DeletionPolicySerializer : StdSerializer<DeletionPolicy>(DeletionPolicy::class.java) {
    override fun serialize(value: DeletionPolicy?, gen: JsonGenerator?, provider: SerializerProvider?) {
        gen?.writeString(value?.value)
    }
}

@JsonSerialize(using = ConditionAttributeSerializer::class)
data class ConditionAttribute(val condition: Condition) : ResourceAttribute("Condition")

class ConditionAttributeSerializer : StdSerializer<ConditionAttribute>(ConditionAttribute::class.java) {
    override fun serialize(value: ConditionAttribute?, gen: JsonGenerator?, provider: SerializerProvider?) {
        gen?.writeString(value?.condition?.id)
    }
}

@JsonSerialize(using = ISO8601DurationSerializer::class)
data class ISO8601Duration(val hours: Int = 0, val minutes: Int = 0, val seconds: Int = 0) {
    operator fun plus(other: ISO8601Duration) = ISO8601Duration(this.hours + other.hours, this.minutes + other.minutes, this.seconds + other.seconds)

    override fun toString(): String {
        val hours = if (hours != 0) "${hours}H" else ""
        val minutes = if (minutes != 0) "${minutes}M" else ""
        val seconds = if (seconds != 0) "${seconds}S" else ""
        return "PT$hours$minutes$seconds"
    }
}

class ISO8601DurationSerializer : StdSerializer<ISO8601Duration>(ISO8601Duration::class.java) {
    override fun serialize(value: ISO8601Duration?, gen: JsonGenerator?, provider: SerializerProvider?) {
        gen?.writeString(value?.toString())
    }

}

fun Int.hours() = ISO8601Duration(hours = this)
fun Int.minutes() = ISO8601Duration(minutes = this)
fun Int.seconds() = ISO8601Duration(seconds = this)

data class AutoScalingCreationPolicy(val minSuccessfulInstancesPercent: Int?)
data class ResourceSignal(val count: Int?, val timeout: ISO8601Duration?)
data class CreationPolicy(val autoScalingCreationPolicy: AutoScalingCreationPolicy?, val resourceSignal: ResourceSignal?)
