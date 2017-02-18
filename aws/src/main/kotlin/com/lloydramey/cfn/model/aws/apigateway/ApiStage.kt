package com.lloydramey.cfn.model.aws.apigateway

import com.lloydramey.cfn.model.functions.AwsTemplateValue

data class ApiStage(val apiId: AwsTemplateValue? = null, val stage: AwsTemplateValue? = null)