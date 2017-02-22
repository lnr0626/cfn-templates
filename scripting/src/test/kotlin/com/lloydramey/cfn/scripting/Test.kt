package com.lloydramey.cfn.scripting

import org.jetbrains.kotlin.com.intellij.openapi.project.Project
import org.jetbrains.kotlin.script.KotlinScriptDefinition
import org.jetbrains.kotlin.script.KotlinScriptExternalDependencies
import org.jetbrains.kotlin.utils.PathUtil
import org.junit.Test
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URLClassLoader
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

class Test {

    fun scriptDefinitionFor(clazz: KClass<out Any>) = object : KotlinScriptDefinition(clazz) {
        override fun <TF : Any> getDependenciesFor(
            file: TF,
            project: Project,
            previousDependencies: KotlinScriptExternalDependencies?): KotlinScriptExternalDependencies? =

            object : KotlinScriptExternalDependencies {
                override val imports: Iterable<String>
                    get() = ImplicitImports.list
                override val classpath: Iterable<File>
                    get() = getClassPath()
            }
    }

    @Test
    fun `test compiling script to build dir`() {
        val logger = LoggerFactory.getLogger("main")
        val classLoader = Test::class.java.classLoader

        val classPath = getClassPath()

        val urls = (classPath + PathUtil.getJdkClassesRoots()).map(File::toURL).toTypedArray()

        val loader = URLClassLoader(urls, classLoader)

        val script = compileKotlinScriptToDirectory(
            File("build"),
            File("src/test/resources/test.kts"),
            scriptDefinitionFor(ScriptWithNoArgs::class),
            emptyList(),
            classPath,
            loader,
            logger
        )

        println("Constructing")

        val obj = script.newInstance()

        println(obj::class.memberProperties.associate { it.name to it.call(obj) })
    }

    private fun getClassPath(): List<File> {
        val root = File("build/libs")
        val classPath = root.list().map { File(root, it) }.toList()// + PathUtil.getJdkClassesRoots()

        //        println(classPath.map { it.absolutePath }.joinToString("\n"))
        return classPath
    }
}