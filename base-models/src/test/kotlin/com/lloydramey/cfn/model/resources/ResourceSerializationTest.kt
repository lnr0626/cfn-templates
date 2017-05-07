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
package com.lloydramey.cfn.model.resources

import com.lloydramey.cfn.model.TestHelper
import com.lloydramey.cfn.model.resources.attributes.AutoScalingCreationPolicy
import com.lloydramey.cfn.model.resources.attributes.AutoScalingReplacingUpdate
import com.lloydramey.cfn.model.resources.attributes.AutoScalingRollingUpdate
import com.lloydramey.cfn.model.resources.attributes.AutoScalingScheduledAction
import com.lloydramey.cfn.model.resources.attributes.CreationPolicy
import com.lloydramey.cfn.model.resources.attributes.DeletionPolicy
import com.lloydramey.cfn.model.resources.attributes.DependsOn
import com.lloydramey.cfn.model.resources.attributes.MetadataAttr
import com.lloydramey.cfn.model.resources.attributes.ResourceSignal
import com.lloydramey.cfn.model.resources.attributes.UpdatePolicy
import com.lloydramey.cfn.model.resources.attributes.minutes
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