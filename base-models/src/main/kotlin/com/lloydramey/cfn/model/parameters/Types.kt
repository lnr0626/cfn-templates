package com.lloydramey.cfn.model.parameters

import com.lloydramey.cfn.model.ParameterType

object Types {
    val Str = ParameterType.Single("String")
    val Number = ParameterType.Single("Number")
    val CommaDelimitedList = ParameterType.Single("CommaDelimitedList")
}