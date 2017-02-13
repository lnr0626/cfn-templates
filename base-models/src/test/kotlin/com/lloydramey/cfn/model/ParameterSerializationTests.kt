package com.lloydramey.cfn.model

import com.lloydramey.cfn.model.parameters.Types
import net.javacrumbs.jsonunit.JsonMatchers.jsonEquals
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class ParameterSerializationTests {
    @Test
    fun parameters() {
        val param = Parameter(
                type = Types.Str,
                id = "EnvType",
                allowedValues = listOf("test", "prod"),
                description = "Environment Type",
                default = "test",
                constraintDescription = "must specify prod or test."
        )

        assertThat(
                TestHelper.mapper.writeValueAsString(param),
                jsonEquals("{'Type': 'String', " +
                        "'AllowedValues': ['test', 'prod'], " +
                        "'Description': 'Environment Type', " +
                        "'Default': 'test'," +
                        " 'ConstraintDescription': 'must specify prod or test.'}")
        )
    }
}