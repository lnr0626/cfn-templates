package com.lloydramey.cfn.gradle.plugin

internal interface CloudifySourceSetProvider {
    fun create(displayName: String): CloudifySourceSet
}