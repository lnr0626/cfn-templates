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