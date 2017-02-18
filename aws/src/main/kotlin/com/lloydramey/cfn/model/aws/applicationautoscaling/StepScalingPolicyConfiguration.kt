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
package com.lloydramey.cfn.model.aws.applicationautoscaling

data class StepScalingPolicyConfiguration(
    val adjustmentType: AdjustmentType? = null,
    val cooldown: Int? = null,
    val metricAggregationType: MetricAggregationType? = null,
    val minAdjustmentMagnitude: Int? = null,
    val stepAdjustments: List<StepAdjustment>? = null
)