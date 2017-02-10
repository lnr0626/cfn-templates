package com.lloydramey.cfn.model

import net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class ConditionFunctionTests {
    @Test
    fun andJsonIsValid() {
        assertThat(Jackson.mapper.writeValueAsString(And(ConditionReference("Blah"), ConditionReference("Test"))),
                jsonEquals("{'Fn::And': [{'Condition':'Blah'},{'Condition':'Test'}]}"))
    }

    @Test
    fun notJsonIsValid() {
        assertThat(Jackson.mapper.writeValueAsString(!ConditionReference("Test")), jsonEquals("{'Fn::Not': {'Condition':'Test'}}"))
    }

    @Test
    fun equalsJsonIsValid() {
        assertThat(Jackson.mapper.writeValueAsString(Equals("left", "right")), jsonEquals("{'Fn::Equals': ['left', 'right']}"))
    }

    @Test
    fun orJsonIsValid() {
        assertThat(Jackson.mapper.writeValueAsString(Or(ConditionReference("Test"), ConditionReference("Blah"))),
                jsonEquals("{'Fn::Or': [{'Condition':'Test'}, {'Condition':'Blah'}]}"))
    }

    @Test
    fun conditionReferenceJsonIsValid() {
        assertThat(Jackson.mapper.writeValueAsString(ConditionReference("Test")), jsonEquals("{'Condition':'Test'}"))
    }

    @Test
    fun ifJsonIsValid() {
        assertThat(Jackson.mapper.writeValueAsString(If("Test", "true", "false")), jsonEquals("{'Fn::If': ['Test', 'true', 'false']}"))
    }
}