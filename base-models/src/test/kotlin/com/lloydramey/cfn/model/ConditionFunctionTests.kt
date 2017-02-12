package com.lloydramey.cfn.model

import com.lloydramey.cfn.model.functions.*
import net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class ConditionFunctionTests {
    val test = Condition("Test", Equals("left", "right"))
    val blah = Condition("Blah", Equals("left", "right"))
    val another = Condition("Another", Equals("left", "right"))

    @Test
    fun andJsonIsValid() {
        assertThat(Jackson.mapper.writeValueAsString(And(ConditionReference(blah), ConditionReference(test))),
                jsonEquals("{'Fn::And': [{'Condition':'Blah'},{'Condition':'Test'}]}"))
    }

    @Test
    fun conditionFormatting() {
        assertThat(Jackson.mapper.writeValueAsString(And(Equals(Val("left"), Val("right")), !ConditionReference(blah), ConditionReference(another))),
                jsonEquals("{'Fn::And': [{'Fn::Equals': ['left', 'right']}, {'Fn::Not': {'Condition': 'Blah'}}, {'Condition': 'Another'}]}"))
    }

    @Test
    fun notJsonIsValid() {
        assertThat(Jackson.mapper.writeValueAsString(!Equals(Val("left"), Val("right"))), jsonEquals("{'Fn::Not': {'Fn::Equals': ['left', 'right']}}"))
    }

    @Test
    fun equalsJsonIsValid() {
        assertThat(Jackson.mapper.writeValueAsString(Equals("left", "right")), jsonEquals("{'Fn::Equals': ['left', 'right']}"))
    }

    @Test
    fun orJsonIsValid() {
        assertThat(Jackson.mapper.writeValueAsString(Or(ConditionReference(test), ConditionReference(blah))),
                jsonEquals("{'Fn::Or': [{'Condition':'Test'}, {'Condition':'Blah'}]}"))
    }

    @Test
    fun conditionReferenceJsonIsValid() {
        assertThat(Jackson.mapper.writeValueAsString(ConditionReference(test)), jsonEquals("{'Condition':'Test'}"))
    }

    @Test
    fun ifJsonIsValid() {
        assertThat(Jackson.mapper.writeValueAsString(If(test, "true", "false")), jsonEquals("{'Fn::If': ['Test', 'true', 'false']}"))
    }
}