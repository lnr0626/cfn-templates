buildscript {
    repositories {
        maven {
            setUrl("https://plugins.gradle.org/m2/")
        }
        gradleScriptKotlin()
    }
    dependencies {
        classpath(kotlinModule("gradle-plugin"))
        classpath("gradle.plugin.nl.javadude.gradle.plugins:license-gradle-plugin:0.13.1")
    }
}

dependencies {
    compile(kotlinModule("stdlib-jre7"))
    compile(kotlinModule("reflect"))
    compile("com.fasterxml.jackson.core:jackson-core:2.8.6")
    compile("com.fasterxml.jackson.core:jackson-annotations:2.8.6")
    compile("com.fasterxml.jackson.core:jackson-databind:2.8.6")
    compile("com.fasterxml.jackson.module:jackson-module-kotlin:2.8.6")

    testCompile("junit:junit:4.12")
    testCompile("org.hamcrest:hamcrest-library:1.3")
    testCompile("net.javacrumbs.json-unit:json-unit:1.18.0")
}
