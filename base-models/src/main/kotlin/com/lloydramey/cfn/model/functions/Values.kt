package com.lloydramey.cfn.model.functions

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import com.lloydramey.cfn.model.Mapping
import com.lloydramey.cfn.model.resources.Resource
import com.lloydramey.cfn.model.resources.ResourceProperties

interface IntrinsicFunction
interface AwsTemplateValue
abstract class Referencable(@JsonIgnore val id: String)

@JsonSerialize(using = ToStringSerializer::class)
class Val(val value: String) : AwsTemplateValue, AllowedInConditionFunction, AllowedInIfValues {
    constructor(value: Boolean) : this(value.toString())
    constructor(value: Number) : this(value.toString())

    override fun toString(): String {
        return value
    }
}

abstract class Fn(@JsonIgnore val name: String, @JsonIgnore val value: Any) {
    @JsonAnyGetter
    fun json() = mapOf(name to value)
}

class Ref(ref: Referencable) : Fn("Ref", ref.id), AwsTemplateValue, IntrinsicFunction, AllowedInConditionFunction, AllowedInIfValues
class Base64(toEncode: AwsTemplateValue) : Fn("Fn::Base64", toEncode), AwsTemplateValue, IntrinsicFunction, AllowedInIfValues
class FindInMap(map: Mapping, topLevel: AwsTemplateValue, secondLevel: AwsTemplateValue) : Fn("Fn::FindInMap", listOf(map.id, topLevel, secondLevel)), AwsTemplateValue, IntrinsicFunction, AllowedInConditionFunction
class GetAtt(res: Resource<ResourceProperties>, attr: String) : Fn("Fn::GetAtt", listOf(res.id, attr)), AwsTemplateValue, IntrinsicFunction, AllowedInIfValues
class GetAZs(region: String) : Fn("Fn::GetAZs", region), AwsTemplateValue, IntrinsicFunction, AllowedInIfValues
class ImportValue(name: String) : Fn("Fn::ImportValue", name), AwsTemplateValue, IntrinsicFunction
class Join(values: List<AwsTemplateValue>) : Fn("Fn::Join", values), AwsTemplateValue, IntrinsicFunction, AllowedInIfValues {
    constructor(vararg values: AwsTemplateValue) : this(values.asList())
}
class Select(index: Int, vararg values: AwsTemplateValue) : Fn("Fn::Select", listOf(index.toString(), if(values.size > 1) values.asList() else values[0])), AwsTemplateValue, IntrinsicFunction, AllowedInIfValues
class Split(delimiter: String, source: AwsTemplateValue) : Fn("Fn::Split", listOf(delimiter, source))
class Sub(source: String, values: Map<String, AwsTemplateValue>? = null) : Fn("Fn::Sub", if(values == null) source else listOf(source, values))