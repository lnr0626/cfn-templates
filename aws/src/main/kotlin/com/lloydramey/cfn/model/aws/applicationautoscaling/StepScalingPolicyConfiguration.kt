package com.lloydramey.cfn.model.aws.applicationautoscaling

data class StepScalingPolicyConfiguration(
    val adjustmentType: AdjustmentType? = null,
    val cooldown: Int? = null,
    val metricAggregationType: MetricAggregationType? = null,
    val minAdjustmentMagnitude: Int? = null,
    val stepAdjustments: List<StepAdjustment>? = null
)