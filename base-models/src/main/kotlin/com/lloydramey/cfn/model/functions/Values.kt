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

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.lloydramey.cfn.model.Mapping

interface IntrinsicFunction
interface AwsTemplateValue
abstract class Referencable(@JsonIgnore val id: String)
abstract class ReferencableWithAttributes(id: String) : Referencable(id) {
    operator fun get(attrId: String) = GetAtt(this, attrId)
}

@JsonSerialize(using = ToStringSerializer::class)
class Val(val value: String) : AwsTemplateValue, AllowedInConditionFunction, AllowedInIfValues {
    constructor(value: Boolean) : this(value.toString())
    constructor(value: Number) : this(value.toString())

    override fun toString(): String = value
}

abstract class Fn(@JsonIgnore val name: String, @JsonIgnore val value: Any) {
    @JsonAnyGetter
    fun json() = mapOf(name to value)
}

class Ref(ref: Referencable) : Fn("Ref", ref.id), AwsTemplateValue, IntrinsicFunction, AllowedInConditionFunction, AllowedInIfValues
class Base64(toEncode: AwsTemplateValue) : Fn("Fn::Base64", toEncode), AwsTemplateValue, IntrinsicFunction, AllowedInIfValues
class FindInMap(map: Mapping, topLevel: AwsTemplateValue, secondLevel: AwsTemplateValue) : Fn("Fn::FindInMap", listOf(map.id, topLevel, secondLevel)), AwsTemplateValue, IntrinsicFunction, AllowedInConditionFunction
class GetAtt(res: ReferencableWithAttributes, attr: String) : Fn("Fn::GetAtt", listOf(res.id, attr)), AwsTemplateValue, IntrinsicFunction, AllowedInIfValues
class GetAZs(region: String) : Fn("Fn::GetAZs", region), AwsTemplateValue, IntrinsicFunction, AllowedInIfValues
class ImportValue(v: AwsTemplateValue) : Fn("Fn::ImportValue", v), AwsTemplateValue, IntrinsicFunction
class Join(delimiter: String, values: List<AwsTemplateValue>) : Fn("Fn::Join", listOf(delimiter, values)), AwsTemplateValue, IntrinsicFunction, AllowedInIfValues {
    constructor(delimiter: String, vararg values: AwsTemplateValue) : this(delimiter, values.asList())
}
class Select(index: Int, vararg values: AwsTemplateValue) : Fn("Fn::Select", listOf(index.toString(), if (values.size > 1) values.asList() else values[0])), AwsTemplateValue, IntrinsicFunction, AllowedInIfValues
class Split(delimiter: String, source: AwsTemplateValue) : Fn("Fn::Split", listOf(delimiter, source)), AwsTemplateValue, IntrinsicFunction, AllowedInConditionFunction
class Sub(source: String, values: Map<String, AwsTemplateValue>? = null) : Fn("Fn::Sub", if (values == null) source else listOf(source, values)), AwsTemplateValue, IntrinsicFunction, AllowedInConditionFunction