package com.lloydramey.cfn.model.aws

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.lloydramey.cfn.model.aws.applicationautoscaling.AdjustmentType
import com.lloydramey.cfn.model.aws.applicationautoscaling.MetricAggregationType
import com.lloydramey.cfn.model.aws.applicationautoscaling.StepScalingPolicyConfiguration
import com.lloydramey.cfn.model.functions.AwsTemplateValue
import com.lloydramey.cfn.model.resources.Required
import com.lloydramey.cfn.model.resources.ResourceProperties

class ApplicationAutoScaling {
    class ScalableTarget : ResourceProperties("AWS::ApplicationAutoScaling::ScalableTarget") {
        @Required var maxCapacity: AwsTemplateValue? = null
        @Required var minCapacity: AwsTemplateValue? = null
        @Required var resourceId: AwsTemplateValue? = null
        @Required var roleARN: AwsTemplateValue? = null
        @Required var scalableDimension: AwsTemplateValue? = null
        @Required var serviceNamespace: AwsTemplateValue? = null
    }

    class ScalingPolicy : ResourceProperties("AWS::ApplicationAutoScaling::ScalingPolicy") {
        @Required var policyName: AwsTemplateValue? = null
        @Required var policyType: AwsTemplateValue? = null

        var resourceId: AwsTemplateValue? = null
        var scalableDimension: AwsTemplateValue? = null
        var scalingTargetId: AwsTemplateValue? = null
        var serviceNamespace: AwsTemplateValue? = null
        var stepScalingPolicyConfiguration: StepScalingPolicyConfiguration? = null
    }
}

