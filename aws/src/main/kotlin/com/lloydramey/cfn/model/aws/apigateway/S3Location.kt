package com.lloydramey.cfn.model.aws.apigateway

data class S3Location(
    val bucket: String? = null,
    val eTag: String? = null,
    val key: String? = null,
    val version: String? = null
)