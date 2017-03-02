import org.gradle.plugin.devel.GradlePluginDevelopmentExtension
import org.jetbrains.kotlin.allopen.gradle.AllOpenGradleSubplugin
import org.jetbrains.kotlin.allopen.gradle.AllOpenExtension

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
    plugin("kotlin-allopen")
}

configure<GradlePluginDevelopmentExtension> {
    plugins {
        create("cfnPlugin") {
            id = "com.lloydramey.cfn"
            implementationClass = "com.lloydramey.cfn.gradle.plugin.CfnPlugin"
        }
    }
}

(extensions["allOpen"] as AllOpenExtension).annotation("com.lloydramey.cfn.gradle.internal.OpenForGradle")

dependencies {
    compile(gradleApi())
    compile(project(":base-models"))
    compile(kotlinModule("compiler-embeddable"))
    compile(kotlinModule("gradle-plugin"))
    compile("io.github.microutils:kotlin-logging:1.4.3")
    compile("org.slf4j:slf4j-api:1.7.23")

    testCompile(project(":aws"))
    testCompile(gradleTestKit())
    testCompile("junit:junit:4.12")
    testCompile("org.hamcrest:hamcrest-library:1.3")
}
