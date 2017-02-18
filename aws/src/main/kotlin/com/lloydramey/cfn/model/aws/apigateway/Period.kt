package com.lloydramey.cfn.model.aws.apigateway

import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize(using = ToStringSerializer::class)
sealed class Period(val period: String) {
    object Day : Period("DAY")
    object Week : Period("WEEK")
    object Month : Period("MONTH")

    override fun toString() = period
}