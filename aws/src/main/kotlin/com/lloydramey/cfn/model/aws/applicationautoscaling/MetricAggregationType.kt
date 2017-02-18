package com.lloydramey.cfn.model.aws.applicationautoscaling

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer

@JsonSerialize(using = ToStringSerializer::class)
sealed class MetricAggregationType(val type: String) {
    object Minimum : MetricAggregationType("Minimum")
    object Maximum : MetricAggregationType("Maximum")
    object Average : MetricAggregationType("Average")

    override fun toString() = type
}