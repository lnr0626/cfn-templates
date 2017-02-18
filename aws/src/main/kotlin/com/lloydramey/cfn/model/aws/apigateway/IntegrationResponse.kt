package com.lloydramey.cfn.model.aws.apigateway

class IntegrationResponse(
        val responseParameters: Map<String, String> = mapOf(),
        val responseTemplates: Map<String, String> = mapOf(),
        val selectionPattern: String? = null,
        val statusCode: String? = null
)