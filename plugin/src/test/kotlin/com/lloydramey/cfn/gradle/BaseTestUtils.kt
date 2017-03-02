/*
 * Copyright 2017 Lloyd Ramey <lnr0626@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lloydramey.cfn.gradle

import org.gradle.testkit.runner.internal.PluginUnderTestMetadataReading
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.io.File

@RunWith(Parameterized::class)
abstract class MultiVersionGradleTest {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Using Gradle v{0}")
        fun gradleVersions() = listOf("3.0")

        val classpath: List<File> by lazy {
            PluginUnderTestMetadataReading.readImplementationClasspath()
        }

        val classpathString: String by lazy {
            classpath.map { "'${escape(it.absolutePath)}'" }.joinToString(", ")
        }

        private fun escape(str: String) = str.replace("\\", "\\\\")
    }
}

abstract class FolderBasedTest : MultiVersionGradleTest() {
    @JvmField
    @Rule val tempFolder = TemporaryFolder()

    fun withFolders(folders: FoldersDslExpression) =
        tempFolder.root.withFolders(folders)

    fun folder(path: String) =
        existing(path).apply {
            assert(isDirectory)
        }

    fun file(path: String) =
        existing(path).apply {
            assert(isFile)
        }

    private fun existing(path: String): File =
        File(tempFolder.root, path).canonicalFile.apply {
            assert(exists())
        }
}

typealias FoldersDslExpression = FoldersDsl.() -> Unit

fun File.withFolders(folders: FoldersDslExpression) =
    apply { FoldersDsl(this).folders() }

class FoldersDsl(val root: File) {

    operator fun String.invoke(subFolders: FoldersDslExpression): File =
        (+this).withFolders(subFolders)

    operator fun String.unaryPlus(): File =
        asCanonicalFile().apply { mkdirs() }

    fun withFile(fileName: String, content: String = "") =
        fileName.asCanonicalFile().apply { parentFile.mkdirs() }.writeText(content)

    private fun String.asCanonicalFile(): File =
        File(root, this).canonicalFile

}