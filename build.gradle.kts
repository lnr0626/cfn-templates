import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.api.Project
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.JavaExec
import org.gradle.script.lang.kotlin.`maven-publish`
import org.gradle.script.lang.kotlin.apply
import org.gradle.script.lang.kotlin.create
import org.gradle.script.lang.kotlin.dependencies
import org.gradle.script.lang.kotlin.get
import org.gradle.script.lang.kotlin.getByName
import org.gradle.script.lang.kotlin.getPluginByName
import org.gradle.script.lang.kotlin.gradleScriptKotlin
import org.gradle.script.lang.kotlin.invoke
import org.gradle.script.lang.kotlin.java
import org.gradle.script.lang.kotlin.kotlinModule
import org.gradle.script.lang.kotlin.repositories
import org.gradle.script.lang.kotlin.version

buildscript {
    repositories {
        gradleScriptKotlin()
        jcenter()
    }
    dependencies {
        classpath(kotlinModule("gradle-plugin"))
    }
}

plugins {
    java
    `maven-publish`
    id("com.github.hierynomus.license") version "0.13.1"
    id("com.jfrog.bintray") version ("1.7.3") apply (false)
    id("nebula.kotlin") version ("1.1.2") apply (false)
    id("nebula.release") version ("4.2.0")
}

subprojects {
    apply {
        plugin("maven-publish")
        plugin("nebula.kotlin")
        plugin("com.github.hierynomus.license")
        plugin("com.jfrog.bintray")
    }

    rootProject.tasks["release"].dependsOn(tasks["check"])
    rootProject.tasks["postRelease"].dependsOn(tasks["bintrayUpload"])

    group = "com.lloydramey.cfn"

    license {
        header = rootProject.file("APACHE_HEADER")
        mapping("kt", "SLASHSTAR_STYLE")
        mapping("kts", "SLASHSTAR_STYLE")
        mapping("java", "SLASHSTAR_STYLE")

        strictCheck = false
        skipExistingHeaders = true
    }

    bintray {
        user = project.findProperty("bintrayUser") as String? ?: System.getenv("BINTRAY_USER")
        key = project.findProperty("bintrayApiKey") as String? ?: System.getenv("BINTRAY_API_KEY")
        setPublications("MavenPublication")

        pkg {
            repo = "repo"
            name = "cloudify"
            setLicenses("Apache-2.0")
            websiteUrl = "http://github.com/lnr0626/jdbc-named-parameters"
            vcsUrl = "http://github.com/lnr0626/jdbc-named-parameters.git"
            issueTrackerUrl = "http://github.com/lnr0626/jdbc-named-parameters/issues"
            setLabels("cloudformation", "aws-cloudformation", "aws")
            attributes = emptyMap<String, String>()
            override = true
            publish = true
            githubRepo = "lnr0626/cfn-templates"
            version {
                name = project.version.toString()
                vcsTag = project.version.toString()
                attributes = emptyMap<String, String>()
            }
        }
    }

    configurations.create("ktlint")

    repositories {
        mavenCentral()
        gradleScriptKotlin()
    }

    dependencies {
        "ktlint"("com.github.shyiko:ktlint:0.4.0")
    }

    publishing {
        publications.create<MavenPublication>("MavenPublication") {
            from(components["java"])
        }
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

fun BintrayExtension.PackageConfig.version(configure: BintrayExtension.VersionConfig.() -> Unit = {}) =
    this.version.apply(configure)

fun BintrayExtension.pkg(configure: BintrayExtension.PackageConfig.() -> Unit = {}) =
    this.pkg.apply(configure)

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


/**
 * Retrieves or configures the [java][org.gradle.api.plugins.JavaPluginConvention] project convention.
 */
fun Project.`java`(configure: org.gradle.api.plugins.JavaPluginConvention.() -> Unit = {}) =
    convention.getPluginByName<org.gradle.api.plugins.JavaPluginConvention>("java").apply { configure() }

/**
 * Retrieves or configures the [publishing][org.gradle.api.publish.PublishingExtension] project extension.
 */
fun Project.`publishing`(configure: org.gradle.api.publish.PublishingExtension.() -> Unit = {}) =
    extensions.getByName<org.gradle.api.publish.PublishingExtension>("publishing").apply { configure() }

