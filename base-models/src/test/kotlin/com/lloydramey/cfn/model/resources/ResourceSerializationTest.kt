package com.lloydramey.cfn.model.resources

import com.lloydramey.cfn.model.TestHelper
import net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class TestResource : ResourceProperties("Test") {
    lateinit var attribute: String
}

class ResourceSerializationTest {
    val props = TestResource()
    val res = Resource("Id", listOf(DeletionPolicy.Retain), props)

    init {
        props.attribute = "value"
    }

    @Test
    fun deletionPolicies() {
        assertThat(TestHelper.mapper.writeValueAsString(DeletionPolicy.Delete), jsonEquals("Delete"))
        assertThat(TestHelper.mapper.writeValueAsString(DeletionPolicy.Snapshot), jsonEquals("Snapshot"))
        assertThat(TestHelper.mapper.writeValueAsString(DeletionPolicy.Retain), jsonEquals("Retain"))
    }

    @Test
    fun creationPolicies() {
        assertThat(
                TestHelper.mapper.writeValueAsString(CreationPolicy(AutoScalingCreationPolicy(2), ResourceSignal(33, 15.minutes()))),
                jsonEquals("{'AutoScalingCreationPolicy': {'MinSuccessfulInstancesPercent': '2'}, 'ResourceSignal':{'Count': '33', 'Timeout': 'PT15M'}}")
        )
    }

    @Test
    fun metadata() {
        assertThat(
                TestHelper.mapper.writeValueAsString(MetadataAttr(mapOf("test" to "34", "key" to "value"))),
                jsonEquals("{'test': '34', 'key': 'value'}") // Metadata attributes maintain case?
        )
    }

    @Test
    fun dependsOn() {
        assertThat(
                TestHelper.mapper.writeValueAsString(DependsOn(res)),
                jsonEquals("Id")
        )

        assertThat(
                TestHelper.mapper.writeValueAsString(DependsOn(res, res)),
                jsonEquals("['Id', 'Id']")
        )
    }

    @Test
    fun updatePolicy() {
        assertThat(
                TestHelper.mapper.writeValueAsString(UpdatePolicy(AutoScalingReplacingUpdate(true))),
                jsonEquals("{'AutoScalingReplacingUpdate': {'WillReplace': 'true'}}")
        )
        assertThat(
                TestHelper.mapper.writeValueAsString(UpdatePolicy(autoScalingRollingUpdate = AutoScalingRollingUpdate(1, 0))),
                jsonEquals("{'AutoScalingRollingUpdate': {'MaxBatchSize': '1', 'MinInstancesInService': '0'}}")
        )
        assertThat(
                TestHelper.mapper.writeValueAsString(UpdatePolicy(autoScalingScheduledAction = AutoScalingScheduledAction(true))),
                jsonEquals("{'AutoScalingScheduledAction': {'IgnoreUnmodifiedGroupSizeProperties': 'true'}}")
        )
    }

    @Test
    fun resource() {
        assertThat(
                TestHelper.mapper.writeValueAsString(res),
                jsonEquals("{'Properties': {'Attribute': 'value'}, 'Type': 'Test', 'DeletionPolicy': 'Retain'}")
        )
    }
}