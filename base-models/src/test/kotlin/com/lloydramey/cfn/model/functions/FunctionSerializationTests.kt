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
package com.lloydramey.cfn.model.functions

import com.lloydramey.cfn.model.Mapping
import com.lloydramey.cfn.model.Parameter
import com.lloydramey.cfn.model.TestHelper
import com.lloydramey.cfn.model.parameters.Types
import com.lloydramey.cfn.model.resources.Resource
import com.lloydramey.cfn.model.resources.TestResource
import net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class FunctionSerializationTests {
    val mapping = Mapping("Id", mapOf(
            "Test" to mapOf("Key" to "value")
    ))
    val properties = TestResource()
    val res = Resource("Test", properties = properties)

    init {
        properties.attribute = "value"
    }

    val param = Parameter(id = "Param", type = Types.Str)

    @Test
    fun valSerialization() {
        assertThat(json(Val("string")), jsonEquals("string"))
    }

    @Test
    fun refSerialization() {
        assertThat(json(Ref(res)), jsonEquals("{'Ref': 'Test'}"))
        assertThat(json(Ref(param)), jsonEquals("{'Ref': 'Param'}"))
    }

    @Test
    fun findInMap() {
        assertThat(
                json(FindInMap(mapping, Val("Test"), Val("Key"))),
                jsonEquals("{'Fn::FindInMap': ['Id', 'Test', 'Key']}")
        )
    }

    @Test
    fun bas64() {
        assertThat(
                json(Base64(Val("test"))),
                jsonEquals("{'Fn::Base64': 'test'}")
        )
    }

    @Test
    fun getAtt() {
        assertThat(
                json(GetAtt(res, "Test")),
                jsonEquals("{'Fn::GetAtt': ['Test', 'Test']}")
        )
    }

    @Test
    fun getAzs() {
        assertThat(
                json(GetAZs("Test")),
                jsonEquals("{'Fn::GetAZs': 'Test'}")
        )
    }

    @Test
    fun importValue() {
        assertThat(
                json(ImportValue(Val("Test"))),
                jsonEquals("{'Fn::ImportValue': 'Test'}")
        )
    }

    @Test
    fun join() {
        assertThat(
                json(Join(", ", Val("Test"), Val("Another"))),
                jsonEquals("{'Fn::Join': [', ', ['Test', 'Another']]}")
        )
    }

    @Test
    fun select() {
        assertThat(
                json(Select(0, Val("Another"))),
                jsonEquals("{'Fn::Select': ['0', 'Another']}")
        )
        assertThat(
                json(Select(0, Val("Another"), Val("Test"))),
                jsonEquals("{'Fn::Select': ['0', ['Another', 'Test']]}")
        )
    }

    @Test
    fun split() {
        assertThat(
                json(Split(",", Val("Another"))),
                jsonEquals("{'Fn::Split': [',', 'Another']}")
        )
    }

    @Test
    fun sub() {
        assertThat(
                json(Sub(",")),
                jsonEquals("{'Fn::Sub': ','}")
        )
        assertThat(
                json(Sub(",", mapOf("Test" to Val("Another")))),
                jsonEquals("{'Fn::Sub': [',', {'Test': 'Another'}]}")
        )
    }

    private fun json(v: AwsTemplateValue) = TestHelper.mapper.writeValueAsString(v)
}