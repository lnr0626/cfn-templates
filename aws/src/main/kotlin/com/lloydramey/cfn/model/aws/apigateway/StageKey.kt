package com.lloydramey.cfn.model.aws

import com.lloydramey.cfn.model.functions.AwsTemplateValue

class StageKey(
    val restApiId: AwsTemplateValue? = null,
    val stageName: AwsTemplateValue? = null
)