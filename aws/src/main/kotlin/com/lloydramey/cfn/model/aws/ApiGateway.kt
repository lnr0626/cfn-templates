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
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.lloydramey.cfn.model.aws.apigateway.ApiStage
import com.lloydramey.cfn.model.aws.apigateway.Integration
import com.lloydramey.cfn.model.aws.apigateway.MethodResponse
import com.lloydramey.cfn.model.aws.apigateway.MethodSetting
import com.lloydramey.cfn.model.aws.apigateway.QuotaSettings
import com.lloydramey.cfn.model.aws.apigateway.S3Location
import com.lloydramey.cfn.model.aws.apigateway.ThrottleSettings
import com.lloydramey.cfn.model.functions.AwsTemplateValue
import com.lloydramey.cfn.model.resources.Required
import com.lloydramey.cfn.model.resources.ResourceProperties

@Suppress("unused")
class ApiGateway {
    class Account : ResourceProperties("AWS::ApiGateway::Account") {
        var cloudWatchRoleArn: AwsTemplateValue? = null
    }

    class ApiKey : ResourceProperties("AWS::ApiGateway::ApiKey") {
        var description: AwsTemplateValue? = null
        var enabled: AwsTemplateValue? = null
        var name: AwsTemplateValue? = null
        var stageKeys: MutableList<StageKey> = mutableListOf()
    }

    class Authorizer : ResourceProperties("AWS::ApiGateway::Authorizer") {
        @Required var identitySource: AwsTemplateValue? = null
        @Required var name: AwsTemplateValue? = null
        @Required var type: AwsTemplateValue? = null

        var authorizerCredentials: AwsTemplateValue? = null
        var authorizerResultTtlInSeconds: AwsTemplateValue? = null
        var authorizerUri: AwsTemplateValue? = null
        var identityValidationExpression: AwsTemplateValue? = null
        var providerARNs: MutableList<AwsTemplateValue> = mutableListOf()
        var restApiId: AwsTemplateValue? = null
    }

    class BasePathMapping : ResourceProperties("AWS::ApiGateway::BasePathMapping") {
        @Required var domainName: AwsTemplateValue? = null
        @Required var restApiId: AwsTemplateValue? = null

        var basePath: AwsTemplateValue? = null
        var stage: AwsTemplateValue? = null
    }

    class ClientCertificate : ResourceProperties("AWS::ApiGateway::ClientCertificate") {
        var description: AwsTemplateValue? = null
    }

    class Deployment : ResourceProperties("AWS::ApiGateway::Deployment") {
        @Required var restApiId: AwsTemplateValue? = null

        var description: AwsTemplateValue? = null
        var stageName: AwsTemplateValue? = null
        var stageDescription: StageDescription? = null
    }

    class Method : ResourceProperties("AWS::ApiGateway::Method") {
        @Required var authorizationType: AwsTemplateValue? = null
        @Required var httpMethod: AwsTemplateValue? = null
        @Required var resourceId: AwsTemplateValue? = null
        @Required var restApiId: AwsTemplateValue? = null

        var apiKeyRequired: AwsTemplateValue? = null
        var authorizerId: AwsTemplateValue? = null
        var integration: Integration? = null
        var methodResponses: MutableList<MethodResponse> = mutableListOf()
        @JsonSerialize(using = StringToResourceMapSerializer::class) var requestModels: MutableMap<String, com.lloydramey.cfn.model.resources.Resource<Model>> = mutableMapOf()
        var requestParameters: MutableMap<String, Boolean> = mutableMapOf()

    }

    class Model : ResourceProperties("AWS::ApiGateway::Model") {
        @Required var restApiId: AwsTemplateValue? = null
        //TODO: Allow specifying json file, or create schema representation in kotlin?
        @Required @JsonInclude var schema: MutableMap<String, Any>? = null

        var contentType: AwsTemplateValue? = null
        var description: AwsTemplateValue? = null
        var name: AwsTemplateValue? = null
    }

    class Resource : ResourceProperties("AWS::ApiGateway::Resource") {
        @Required var parentId: AwsTemplateValue? = null
        @Required var pathPart: AwsTemplateValue? = null
        @Required var restApiId: AwsTemplateValue? = null
    }

    class RestApi : ResourceProperties("AWS::ApiGateway::RestApi") {
        //TODO: Allow specifying a file containing the json/yaml representation
        var body: AwsTemplateValue? = null
        var bodyS3Location: S3Location? = null
        var cloneFrom: AwsTemplateValue? = null
        var description: AwsTemplateValue? = null
        var failOnWarnings: AwsTemplateValue? = null
        var name: AwsTemplateValue? = null
        var parameters: MutableList<AwsTemplateValue> = mutableListOf()
    }

    class Stage : ResourceProperties("AWS::ApiGateway::Stage") {
        @Required var deploymentId: AwsTemplateValue? = null
        @Required var restApiId: AwsTemplateValue? = null
        @Required var stageName: AwsTemplateValue? = null

        var cacheClusterEnabled: AwsTemplateValue? = null
        var cacheClusterSize: AwsTemplateValue? = null
        var clientCertificateId: AwsTemplateValue? = null
        var description: AwsTemplateValue? = null
        var methodSettings: MutableList<MethodSetting> = mutableListOf()
        var variables: MutableMap<String, String> = mutableMapOf()
    }

    class UsagePlan : ResourceProperties("AWS::ApiGateway::UsagePlan") {
        var ApiStages: MutableList<ApiStage> = mutableListOf()
        var Description: AwsTemplateValue? = null
        var Quota: QuotaSettings? = null
        var Throttle: ThrottleSettings? = null
        var UsagePlanName: AwsTemplateValue? = null
    }

}

