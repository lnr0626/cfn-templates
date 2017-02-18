package com.lloydramey.cfn.model.aws.applicationautoscaling

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer

@JsonSerialize(using = ToStringSerializer::class)
sealed class AdjustmentType(val type: String) {
    object ChangeInCapacity : AdjustmentType("ChangeInCapacity")
    object PercentChangeInCapacity : AdjustmentType("PercentChangeInCapacity")
    object ExactCapacity : AdjustmentType("ExactCapacity")

    override fun toString() = type
}