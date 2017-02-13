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

import com.lloydramey.cfn.model.TestHelper
import net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class ConditionFunctionTests {
    val test = Condition("Test", Equals("left", "right"))
    val blah = Condition("Blah", Equals("left", "right"))
    val another = Condition("Another", Equals("left", "right"))

    @Test
    fun andJsonIsValid() {
        assertThat(TestHelper.mapper.writeValueAsString(And(ConditionReference(blah), ConditionReference(test))),
                jsonEquals("{'Fn::And': [{'Condition':'Blah'},{'Condition':'Test'}]}"))
    }

    @Test
    fun conditionFormatting() {
        assertThat(TestHelper.mapper.writeValueAsString(And(Equals(Val("left"), Val("right")), !ConditionReference(blah), ConditionReference(another))),
                jsonEquals("{'Fn::And': [{'Fn::Equals': ['left', 'right']}, {'Fn::Not': {'Condition': 'Blah'}}, {'Condition': 'Another'}]}"))
    }

    @Test
    fun notJsonIsValid() {
        assertThat(TestHelper.mapper.writeValueAsString(!Equals(Val("left"), Val("right"))), jsonEquals("{'Fn::Not': {'Fn::Equals': ['left', 'right']}}"))
    }

    @Test
    fun equalsJsonIsValid() {
        assertThat(TestHelper.mapper.writeValueAsString(Equals("left", "right")), jsonEquals("{'Fn::Equals': ['left', 'right']}"))
    }

    @Test
    fun orJsonIsValid() {
        assertThat(TestHelper.mapper.writeValueAsString(Or(ConditionReference(test), ConditionReference(blah))),
                jsonEquals("{'Fn::Or': [{'Condition':'Test'}, {'Condition':'Blah'}]}"))
    }

    @Test
    fun conditionReferenceJsonIsValid() {
        assertThat(TestHelper.mapper.writeValueAsString(ConditionReference(test)), jsonEquals("{'Condition':'Test'}"))
    }

    @Test
    fun ifJsonIsValid() {
        assertThat(TestHelper.mapper.writeValueAsString(If(test, "true", "false")), jsonEquals("{'Fn::If': ['Test', 'true', 'false']}"))
    }
}