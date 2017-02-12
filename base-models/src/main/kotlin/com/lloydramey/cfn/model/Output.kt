package com.lloydramey.cfn.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.lloydramey.cfn.model.functions.AwsTemplateValue
import com.lloydramey.cfn.model.functions.Val

data class Export(val name: AwsTemplateValue) {
    constructor(name: String) : this(Val(name))
}

class Output(@JsonIgnore val name: String, val value: AwsTemplateValue, val description: String? = null, val export: Export? = null)
