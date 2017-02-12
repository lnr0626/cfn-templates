package com.lloydramey.cfn.model.resources

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer

data class AutoScalingReplacingUpdate(@JsonSerialize(using=ToStringSerializer::class) val willReplace: Boolean? = null)

sealed class ScalingProcess(val name: String) {
    object Launch : ScalingProcess("Launch")
    object Terminate : ScalingProcess("Terminate")
    object HealthCheck : ScalingProcess("HealthCheck")
    object ReplaceUnhealthy : ScalingProcess("ReplaceUnhealthy")
    object AZRebalance : ScalingProcess("AZRebalance")
    object AlarmNotification : ScalingProcess("AlarmNotification")
    object ScheduledActions : ScalingProcess("ScheduledActions")
    object AddToLoadBalancer : ScalingProcess("AddToLoadBalancer")
}

data class AutoScalingRollingUpdate(
        @JsonSerialize(using=ToStringSerializer::class) val maxBatchSize: Int? = null,
        @JsonSerialize(using=ToStringSerializer::class) val minInstancesInService: Int? = null,
        @JsonSerialize(using=ToStringSerializer::class) val minSuccessfulInstancesPercent: Int? = null,
        @JsonSerialize(using=ToStringSerializer::class) val pauseTime: ISO8601Duration? = null,
        val suspendProcesses: List<ScalingProcess> = emptyList(),
        @JsonSerialize(using=ToStringSerializer::class) val waitOnResourceSignals: Boolean? = null
)

data class AutoScalingScheduledAction(
        @JsonSerialize(using = ToStringSerializer::class) val ignoreUnmodifiedGroupSizeProperties: Boolean? = null
)

class UpdatePolicy(
        val autoScalingReplacingUpdate: AutoScalingReplacingUpdate? = null,
        val autoScalingRollingUpdate: AutoScalingRollingUpdate? = null,
        val autoScalingScheduledAction: AutoScalingScheduledAction? = null
) : ResourceAttribute("UpdatePolicy")