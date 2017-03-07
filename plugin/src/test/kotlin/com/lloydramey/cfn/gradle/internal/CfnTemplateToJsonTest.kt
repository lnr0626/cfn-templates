package com.lloydramey.cfn.gradle.internal

import com.lloydramey.cfn.model.parameters.Types.Str
import com.lloydramey.cfn.scripting.CfnTemplateScript
import org.junit.Test
import java.io.File

class CfnTemplateToJsonTest {
    @Test
    fun `Converts template class into json with appropriate name`() {
        convertToJson(Test_template::class.java, File("./build/template-tests/output"))
    }
}

class Test_template : CfnTemplateScript() {
    val EnvType by parameter(Str) {
        default = "blah"
    }
}
