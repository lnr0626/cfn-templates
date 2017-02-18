package com.lloydramey.cfn.model.aws.applicationautoscaling

data class StepAdjustment(
    val metricIntevalLowerBound: Number? = null,
    val metricIntevalUpperBound: Number? = null,
    val scalingAdjustment: Int
)