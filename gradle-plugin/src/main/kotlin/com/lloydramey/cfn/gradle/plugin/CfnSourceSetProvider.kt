package com.lloydramey.cfn.gradle.plugin

internal interface CfnSourceSetProvider {
    fun create(displayName: String): CfnSourceSet
}