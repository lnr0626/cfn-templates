package com.lloydramey.cfn.model


data class Parameter(
        val allowedPattern: String = "",
        val allowedValues: String = "",
        val constraintDescription: String = "",
        val default: String = "",
        val description: String = "",
        val maxLength: Number? = null,
        val maxValue: Number? = null,
        val minLength: Number? = null,
        val minValue: Number? = null,
        val noEcho: String = "",
        val type: ParameterType,
        val id: String
) {
    init {
        if (isNumberParameter() && areStringOnlyFieldsPopulated()) {
            throw IllegalArgumentException("You cannot specify MinLength, MaxLength, or AllowedPattern for a Number Parameter ($id)")
        } else if(isNotNumberParameter() && areNumberOnlyFieldsPopulated()) {
            throw IllegalArgumentException("You cannot specify MinValue or MaxValue for a Number Parameter ($id)")
        }
    }

    private fun isNumberParameter() = type == Types.Number || type == ParameterType.List(Types.Number)
    private fun isNotNumberParameter() = !isNumberParameter()
    private fun areStringOnlyFieldsPopulated() = minLength != null || maxLength != null || allowedPattern != ""
    private fun areNumberOnlyFieldsPopulated() = minValue != null || minLength != null
}