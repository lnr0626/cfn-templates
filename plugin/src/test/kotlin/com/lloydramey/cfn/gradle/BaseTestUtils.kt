package com.lloydramey.cfn.gradle

import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.io.File

@RunWith(Parameterized::class)
abstract class MultiVersionGradleTest {
    companion object {
        @JvmStatic
        @Parameterized.Parameters(name="{index}: Using Gradle v{0}")
        fun gradleVersions() = listOf("3.0", "3.1", "3.2", "3.3", "3.4")
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