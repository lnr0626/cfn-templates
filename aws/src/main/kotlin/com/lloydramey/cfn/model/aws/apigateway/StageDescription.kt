package com.lloydramey.cfn.model.aws

import com.lloydramey.cfn.model.aws.apigateway.MethodSetting
import com.lloydramey.cfn.model.functions.AwsTemplateValue

data class StageDescription(
        val cacheClusterEnabled: AwsTemplateValue? = null,
        val cacheClusterSize: AwsTemplateValue? = null,
        val cacheDataEncrypted: AwsTemplateValue? = null,
        val cacheTtlInSeconds: AwsTemplateValue? = null,
        val cachingEnabled: AwsTemplateValue? = null,
        val clientCertificateId: AwsTemplateValue? = null,
        val dataTraceEnabled: AwsTemplateValue? = null,
        val description: AwsTemplateValue? = null,
        val loggingLevel: AwsTemplateValue? = null,
        val methodSettings: List<MethodSetting> = emptyList(),
        val metricsEnabled: AwsTemplateValue? = null,
        val stageName: AwsTemplateValue? = null,
        val throttlingBurstLimit: AwsTemplateValue? = null,
        val throttlingRateLimit: AwsTemplateValue? = null,
        val variables: Map<String, AwsTemplateValue> = mapOf()
)