import org.gradle.api.tasks.JavaExec
import org.gradle.script.lang.kotlin.apply
import org.gradle.script.lang.kotlin.dependencies
import org.gradle.script.lang.kotlin.get
import org.gradle.script.lang.kotlin.gradleScriptKotlin
import org.gradle.script.lang.kotlin.invoke
import org.gradle.script.lang.kotlin.kotlinModule
import org.gradle.script.lang.kotlin.repositories
import org.gradle.script.lang.kotlin.version

buildscript {
    repositories {
        gradleScriptKotlin()
    }
    dependencies {
        classpath(kotlinModule("gradle-plugin"))
    }
}

plugins {
    id("com.github.hierynomus.license") version "0.13.1"
    id("com.jfrog.bintray") version ("1.7.3") apply (false)
    id("nebula.kotlin") version ("1.1.2") apply (false)
}

subprojects {
    apply {
        plugin("nebula.kotlin")
        plugin("com.github.hierynomus.license")
        plugin("com.jfrog.bintray")
    }

    group = "com.lloydramey.cfn"

    license {
        header = rootProject.file("APACHE_HEADER")
        mapping("kt", "SLASHSTAR_STYLE")
        mapping("kts", "SLASHSTAR_STYLE")
        mapping("java", "SLASHSTAR_STYLE")

        strictCheck = false
        skipExistingHeaders = true
    }

    configurations.create("ktlint")

    repositories {
        mavenCentral()
        gradleScriptKotlin()
    }

    dependencies {
        "ktlint"("com.github.shyiko:ktlint:0.4.0")
    }

    tasks {
        val ktlint by creating(JavaExec::class) {
            main = "com.github.shyiko.ktlint.Main"
            classpath = configurations["ktlint"]
            args("src/**/*.kt")
        }

        val ktlintFormat by creating(JavaExec::class) {
            main = "com.github.shyiko.ktlint.Main"
            classpath = configurations["ktlint"]
            args("-F", "src/**/*.kt")
        }

        getByName("check").dependsOn(ktlint)
    }

}

/**
 * Retrieves or configures the [license][nl.javadude.gradle.plugins.license.LicenseExtension] project extension.
 */
fun Project.`license`(configure: nl.javadude.gradle.plugins.license.LicenseExtension.() -> Unit = {}) =
    extensions.getByName<nl.javadude.gradle.plugins.license.LicenseExtension>("license").apply { configure() }


/**
 * Retrieves or configures the [downloadLicenses][nl.javadude.gradle.plugins.license.DownloadLicensesExtension] project extension.
 */
fun Project.`downloadLicenses`(configure: nl.javadude.gradle.plugins.license.DownloadLicensesExtension.() -> Unit = {}) =
    extensions.getByName<nl.javadude.gradle.plugins.license.DownloadLicensesExtension>("downloadLicenses").apply { configure() }


/**
 * Retrieves or configures the [bintray][com.jfrog.bintray.gradle.BintrayExtension] project extension.
 */
fun Project.`bintray`(configure: com.jfrog.bintray.gradle.BintrayExtension.() -> Unit = {}) =
    extensions.getByName<com.jfrog.bintray.gradle.BintrayExtension>("bintray").apply { configure() }


