package com.lloydramey.cfn.model.aws.apigateway

class Integration(
    val cacheKeyParameters: List<String> = emptyList(),
    val cacheNamespace: String? = null,
    val integrationHttpMethod: String? = null,
    val integrationResponses: List<IntegrationResponse> = emptyList(),
    val passthroughBehaviour: String? = null,
    val requestParameters: Map<String, String> = mapOf(),
    val requestTemplates: Map<String, String> = mapOf(),
    val uri: String? = null,
    val type: String
)