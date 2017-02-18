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
package com.lloydramey.cfn.model.aws

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

