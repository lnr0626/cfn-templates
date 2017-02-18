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

import com.lloydramey.cfn.model.aws.autoscaling.AdjustmentType
import com.lloydramey.cfn.model.aws.autoscaling.BlockDeviceMapping
import com.lloydramey.cfn.model.aws.autoscaling.MetricsCollection
import com.lloydramey.cfn.model.aws.autoscaling.NotificationConfigurations
import com.lloydramey.cfn.model.aws.autoscaling.StepAdjustment
import com.lloydramey.cfn.model.aws.autoscaling.Tag
import com.lloydramey.cfn.model.functions.AwsTemplateValue
import com.lloydramey.cfn.model.resources.Required
import com.lloydramey.cfn.model.resources.ResourceProperties

@Suppress("unused")
class AutoScaling {
    class AutoScalingGroup : ResourceProperties("AWS::AutoScaling::AutoScalingGroup") {
        @Required var maxSize: AwsTemplateValue? = null
        @Required var minSize: AwsTemplateValue? = null

        var availabilityZones: MutableList<AwsTemplateValue> = mutableListOf()
        var cooldown: AwsTemplateValue? = null
        var desiredCapacity: AwsTemplateValue? = null
        var healthCheckGracePeriod: AwsTemplateValue? = null
        var healthCheckType: AwsTemplateValue? = null
        var instanceId: AwsTemplateValue? = null
        var launchConfigurationName: AwsTemplateValue? = null
        var loadBalancerNames: MutableList<AwsTemplateValue> = mutableListOf()
        var metricsCollection: MutableList<MetricsCollection> = mutableListOf()
        var notificationConfigurations: MutableList<NotificationConfigurations> = mutableListOf()
        var placementGroup: AwsTemplateValue? = null
        var tags: MutableList<Tag> = mutableListOf()
        var targetGroupARNs: MutableList<AwsTemplateValue> = mutableListOf()
        var terminationPolicies: MutableList<AwsTemplateValue> = mutableListOf()
        var vPCZoneIdentifier: MutableList<AwsTemplateValue> = mutableListOf()
    }

    class LaunchConfiguration : ResourceProperties("AWS::AutoScaling::LaunchConfiguration") {
        var associatePublicIpAddress: AwsTemplateValue? = null
        var blockDeviceMappings: MutableList<BlockDeviceMapping>? = mutableListOf()
        var classicLinkVPCId: AwsTemplateValue? = null
        var classicLinkVPCSecurityGroups: MutableList<AwsTemplateValue> = mutableListOf()
        var ebsOptimized: AwsTemplateValue? = null
        var iamInstanceProfile: AwsTemplateValue? = null
        @Required var imageId: AwsTemplateValue? = null
        var instanceId: AwsTemplateValue? = null
        var instanceMonitoring: AwsTemplateValue? = null
        @Required var instanceType: AwsTemplateValue? = null
        var kernelId: AwsTemplateValue? = null
        var keyName: AwsTemplateValue? = null
        var placementTenancy: AwsTemplateValue? = null
        var ramDiskId: AwsTemplateValue? = null
        var securityGroups: AwsTemplateValue? = null
        var spotPrice: AwsTemplateValue? = null
        var userData: AwsTemplateValue? = null
    }

    class LifecycleHook : ResourceProperties("AWS::AutoScaling::LifecycleHook") {
       @Required var autoScalingGroupName: AwsTemplateValue? = null
       var defaultResult: AwsTemplateValue? = null
       var heartbeatTimeout: AwsTemplateValue? = null
       @Required var lifecycleTransition: AwsTemplateValue? = null
       var notificationMetadata: AwsTemplateValue? = null
       @Required var notificationTargetARN: AwsTemplateValue? = null
       @Required var roleARN: AwsTemplateValue? = null
    }

    class ScalingPolicy : ResourceProperties("AWS::AutoScaling::ScalingPolicy") {
        @Required var adjustmentType: AdjustmentType? = null
        @Required var autoScalingGroupName: AwsTemplateValue? = null
        var cooldown: AwsTemplateValue? = null
        var estimatedInstanceWarmup: AwsTemplateValue? = null
        var metricAggregationType: AwsTemplateValue? = null
        var minAdjustmentMagnitude: AwsTemplateValue? = null
        var policyType: AwsTemplateValue? = null
        var scalingAdjustment: AwsTemplateValue? = null
        var stepAdjustments: MutableList<StepAdjustment> = mutableListOf()
    }

    class ScheduledAction : ResourceProperties("AWS::AutoScaling::ScheduledAction") {
        @Required var autoScalingGroupName: AwsTemplateValue? = null
        var desiredCapacity: AwsTemplateValue? = null
        var endTime: AwsTemplateValue? = null
        var maxSize: AwsTemplateValue? = null
        var minSize: AwsTemplateValue? = null
        var recurrence: AwsTemplateValue? = null
        var startTime: AwsTemplateValue? = null
    }

}

