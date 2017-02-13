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

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.lloydramey.cfn.model.functions.AwsTemplateValue
import com.lloydramey.cfn.model.resources.Resource
import com.lloydramey.cfn.model.resources.ResourceProperties

class ApiGateway {
    class Account : ResourceProperties("AWS::ApiGateway::Account") {
        var cloudWatchRoleArn: AwsTemplateValue? = null
    }

    class ApiKey : ResourceProperties("AWS::ApiGateway::ApiKey") {
        var description: AwsTemplateValue? = null
        var enabled: AwsTemplateValue? = null
        var name: AwsTemplateValue? = null
        var stageKeys: MutableList<StageKey> = mutableListOf()

        class StageKey(val restApiId: AwsTemplateValue? = null, val stageName: AwsTemplateValue? = null)
    }

    class Authorizer : ResourceProperties("AWS::ApiGateway::Authorizer") {
        var authorizerCredentials: AwsTemplateValue? = null
        var authorizerResultTtlInSeconds: AwsTemplateValue? = null
        var authorizerUri: AwsTemplateValue? = null
        var identitySource: AwsTemplateValue? = null
        var identityValidationExpression: AwsTemplateValue? = null
        var name: AwsTemplateValue? = null
        var providerARNs: MutableList<AwsTemplateValue> = mutableListOf()
        var restApiId: AwsTemplateValue? = null
        var type: AwsTemplateValue? = null
    }

    class BasePathMapping : ResourceProperties("AWS::ApiGateway::BasePathMapping") {
        lateinit var domainName: AwsTemplateValue
        lateinit var restApiId: AwsTemplateValue
        var basePath: AwsTemplateValue? = null
        var stage: AwsTemplateValue? = null
    }

    class ClientCertificate : ResourceProperties("AWS::ApiGateway::ClientCertificate") {
        var description: AwsTemplateValue? = null
    }

    class Deployment : ResourceProperties("AWS::ApiGateway::Deployment") {
        lateinit var restApiId: AwsTemplateValue
        var description: AwsTemplateValue? = null
        var stageName: AwsTemplateValue? = null
        var stageDescription: AwsTemplateValue? = null

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

    }

    class Method : ResourceProperties("AWS::ApiGateway::Method") {
        var apiKeyRequired: AwsTemplateValue? = null
        var authorizationType: AwsTemplateValue? = null
        var authorizerId: AwsTemplateValue? = null
        var httpMethod: AwsTemplateValue? = null
        var integration: Integration? = null
        var methodResponses: MutableList<MethodResponse> = mutableListOf()
        @JsonSerialize(using = MapKeyToResourceSerializer::class) var requestModels: MutableMap<String, Resource<Model>> = mutableMapOf()
        var requestParameters: MutableMap<String, Boolean> = mutableMapOf()
        var resourceId: AwsTemplateValue? = null
        var restApiId: AwsTemplateValue? = null

        class MethodResponse(
                @JsonSerialize(using = MapKeyToResourceSerializer::class) val responseModels: Map<String, Resource<Model>> = mapOf(),
                val responseParameters: Map<String, Boolean> = mapOf(),
                val statusCode: String? = null
        )

        class IntegrationResponse(
                val responseParameters: Map<String, String> = mapOf(),
                val responseTemplates: Map<String, String> = mapOf(),
                val selectionPattern: String? = null,
                val statusCode: String? = null
        )

        class Integration(
                val cacheKeyParameters: List<String> = emptyList(),
                val cacheNamespace: String? = null,
                val integrationHttpMethod: String? = null,
                val integrationResponses: List<IntegrationResponse> = emptyList(),
                val passthroughBehaviour: String? = null,
                val requestParameters: Map<String, String> = mapOf(),
                val requestTemplates: Map<String, String> = mapOf(),
                val Type: String? = null,
                val uri: String? = null
        )


        class MapKeyToResourceSerializer : StdSerializer<Map<String, Resource<ResourceProperties>>>(emptyMap<String, Resource<ResourceProperties>>().javaClass) {
            override fun serialize(value: Map<String, Resource<ResourceProperties>>?, gen: JsonGenerator?, provider: SerializerProvider?) {
                if (value != null && gen != null) {
                    gen.writeStartObject()
                    value.forEach { key, value ->
                        gen.writeObjectField(key, value)
                    }
                    gen.writeEndObject()
                }
            }
        }
    }

    class Model : ResourceProperties("AWS::ApiGateway::Model") {
        var contentType: AwsTemplateValue? = null
        var description: AwsTemplateValue? = null
        var name: AwsTemplateValue? = null
        var restApiId: AwsTemplateValue? = null
        @JsonInclude var schema: MutableMap<String, Any> = mutableMapOf()
    }
}
