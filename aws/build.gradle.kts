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
    compile(project(":base-models"))

    testCompile("org.reflections:reflections:0.9.10")
    testCompile("junit:junit:4.12")
    testCompile("org.hamcrest:hamcrest-library:1.3")
    testCompile("net.javacrumbs.json-unit:json-unit:1.18.0")
}
