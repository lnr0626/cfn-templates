package com.lloydramey.cfn.model.aws.apigateway

data class ThrottleSettings(val limit: Int? = null, val offset: Int? = null, val period: Period? = null)