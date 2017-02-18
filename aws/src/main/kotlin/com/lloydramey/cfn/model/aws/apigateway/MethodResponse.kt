package com.lloydramey.cfn.model.aws.apigateway

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.lloydramey.cfn.model.aws.ApiGateway
import com.lloydramey.cfn.model.aws.StringToResourceMapSerializer
import com.lloydramey.cfn.model.resources.Resource

data class MethodResponse(
        @JsonSerialize(using = StringToResourceMapSerializer::class) val responseModels: Map<String, Resource<ApiGateway.Model>> = mapOf(),
        val responseParameters: Map<String, Boolean> = mapOf(),
        val statusCode: String
)