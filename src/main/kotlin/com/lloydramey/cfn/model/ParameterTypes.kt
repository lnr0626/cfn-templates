package com.lloydramey.cfn.model

sealed class ParameterType(val type: String) {
    class Single(type: String) : ParameterType(type)
    class List(contents: Single): ParameterType("List<${contents.type}>")
}

object AWS {
    object EC2 {
        val AvailabilityZone = ParameterType.Single("AWS::EC2::AvailabilityZone::Name")
    }
}

object Types {
    val Str = ParameterType.Single("String")
    val Number = ParameterType.Single("Number")
    val CommaDelimitedList = ParameterType.Single("CommaDelimitedList")
}