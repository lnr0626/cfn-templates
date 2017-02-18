package com.lloydramey.cfn.model.aws.apigateway

import com.lloydramey.cfn.model.functions.AwsTemplateValue

data class MethodSetting(
        val cacheDataEncrypted: AwsTemplateValue? = null,
        val cacheTtlInSeconds: AwsTemplateValue? = null,
        val cachingEnabled: AwsTemplateValue? = null,
        val dataTraceEnabled: AwsTemplateValue? = null,
        val httpMethod: AwsTemplateValue? = null,
        val loggingLevel: AwsTemplateValue? = null,
        val metricsEnabled: AwsTemplateValue? = null,
        val resourcePath: AwsTemplateValue? = null,
        val throttlingBurstLimit: AwsTemplateValue? = null,
        val throttlingRateLimit: AwsTemplateValue? = null
)