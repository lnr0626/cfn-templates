import nl.javadude.gradle.plugins.license.LicensePlugin
import org.gradle.api.publish.maven.plugins.MavenPublishPlugin
import org.gradle.api.tasks.JavaExec
import org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper
import java.util.Calendar

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
}

subprojects {
    apply {
        plugin<MavenPublishPlugin>()
        plugin<KotlinPluginWrapper>()
        plugin<LicensePlugin>()
    }

    group = "com.lloydramey.cfn"

    license {
        header = rootProject.file("APACHE_HEADER")
        mapping("kt", "SLASHSTAR_STYLE")
        mapping("kts", "SLASHSTAR_STYLE")
        mapping("java", "SLASHSTAR_STYLE")

        strictCheck = false
        skipExistingHeaders = true

        var year: Int by extra
        var name: String by extra
        var email: String by extra

        year = Calendar.getInstance().get(Calendar.YEAR)
        name = "Lloyd Ramey"
        email = "lnr0626@gmail.com"
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
            args("-F", "src/**/*.kt")
        }

        getByName("check").dependsOn(ktlint)
    }

}
