package com.lloydramey.cfn.model.resources

import com.lloydramey.cfn.model.Jackson
import net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class ResourceSerializationTest {
    @Test
    fun deletionPolicies() {
        assertThat(Jackson.mapper.writeValueAsString(DeletionPolicy.Delete), jsonEquals("Delete"))
        assertThat(Jackson.mapper.writeValueAsString(DeletionPolicy.Snapshot), jsonEquals("Snapshot"))
        assertThat(Jackson.mapper.writeValueAsString(DeletionPolicy.Retain), jsonEquals("Retain"))
    }

    @Test
    fun creationPolicies() {
        assertThat(
                Jackson.mapper.writeValueAsString(CreationPolicy(AutoScalingCreationPolicy(2), ResourceSignal(33, 15.minutes()))),
                jsonEquals("{'AutoScalingCreationPolicy': {'MinSuccessfulInstancesPercent': 2}, 'ResourceSignal':{'Count': 33, 'Timeout': 'PT15M'}}")
        )
    }
}