
import org.gradle.api.Project
import org.gradle.plugin.devel.GradlePluginDevelopmentExtension
import org.gradle.script.lang.kotlin.compile
import org.gradle.script.lang.kotlin.configure
import org.gradle.script.lang.kotlin.dependencies
import org.gradle.script.lang.kotlin.getByName
import org.gradle.script.lang.kotlin.gradleScriptKotlin
import org.gradle.script.lang.kotlin.kotlinModule
import org.gradle.script.lang.kotlin.plugin
import org.gradle.script.lang.kotlin.project
import org.gradle.script.lang.kotlin.repositories
import org.gradle.script.lang.kotlin.testCompile
import org.jetbrains.kotlin.allopen.gradle.AllOpenExtension
import org.jetbrains.kotlin.allopen.gradle.AllOpenGradleSubplugin

buildscript {
    repositories {
        maven {
            setUrl("https://plugins.gradle.org/m2/")
        }
        gradleScriptKotlin()
    }
    dependencies {
        classpath(kotlinModule("gradle-plugin"))
        classpath(kotlinModule("allopen"))
        classpath("gradle.plugin.nl.javadude.gradle.plugins:license-gradle-plugin:0.13.1")
    }
}

plugins {
    id("java-gradle-plugin")
}

apply {
    plugin<AllOpenGradleSubplugin>()
}

configure<GradlePluginDevelopmentExtension> {
    plugins {
        create("cfnPlugin") {
            id = "com.lloydramey.cfn"
            implementationClass = "com.lloydramey.cfn.gradle.plugin.CfnPlugin"
        }
    }
}

configure<AllOpenExtension> {
    annotation("com.lloydramey.cfn.gradle.internal.OpenForGradle")
}

dependencies {
    compile(gradleApi())
    compile(project(":base-models"))
    compile(kotlinModule("compiler-embeddable"))
    compile(kotlinModule("gradle-plugin"))
    compile("io.github.microutils:kotlin-logging:1.4.3")
    compile("org.slf4j:slf4j-api:1.7.23")
    compile("org.reflections:reflections:0.9.10")

    testCompile(project(":aws"))
    testCompile(gradleTestKit())
    testCompile("junit:junit:4.12")
    testCompile("org.hamcrest:hamcrest-library:1.3")
}

/**
 * Retrieves or configures the [gradlePlugin][org.gradle.plugin.devel.GradlePluginDevelopmentExtension] project extension.
 */
fun Project.`gradlePlugin`(configure: org.gradle.plugin.devel.GradlePluginDevelopmentExtension.() -> Unit = {}) =
    extensions.getByName<org.gradle.plugin.devel.GradlePluginDevelopmentExtension>("gradlePlugin").apply { configure() }
