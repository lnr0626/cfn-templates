/*
 * Copyright 2017 Lloyd Ramey <lnr0626@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lloydramey.cfn.model.resources

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer

@JsonSerialize(using=ToStringSerializer::class)
data class ISO8601Duration(val hours: Int = 0, val minutes: Int = 0, val seconds: Int = 0) {
    operator fun plus(other: ISO8601Duration) = ISO8601Duration(this.hours + other.hours, this.minutes + other.minutes, this.seconds + other.seconds)

    override fun toString(): String {
        val hours = if (hours != 0) "${hours}H" else ""
        val minutes = if (minutes != 0) "${minutes}M" else ""
        val seconds = if (seconds != 0) "${seconds}S" else ""
        return "PT$hours$minutes$seconds"
    }
}

fun Int.hours() = ISO8601Duration(hours = this)
fun Int.minutes() = ISO8601Duration(minutes = this)
fun Int.seconds() = ISO8601Duration(seconds = this)

data class AutoScalingCreationPolicy(
        @JsonSerialize(using=ToStringSerializer::class) val minSuccessfulInstancesPercent: Int?
)

data class ResourceSignal(
        @JsonSerialize(using= ToStringSerializer::class) val count: Int?,
        val timeout: ISO8601Duration?
)

data class CreationPolicy(
        val autoScalingCreationPolicy: AutoScalingCreationPolicy?,
        val resourceSignal: ResourceSignal?) : ResourceDefinitionAttribute("CreationPolicy")
