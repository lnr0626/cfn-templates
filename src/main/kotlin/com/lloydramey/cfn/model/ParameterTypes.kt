package com.lloydramey.cfn.model

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.StdSerializer

@JsonSerialize(using = ParameterTypeSerializer::class)
sealed class ParameterType(val type: String) {
    class Single(type: String) : ParameterType(type)
    class List(contents: Single) : ParameterType("List<${contents.type}>")
}

object AWS {
    object EC2 {
        val AvailabilityZone = ParameterType.Single("AWS::EC2::AvailabilityZone::Name")
        val Image = ParameterType.Single("AWS::EC2::Image::Id")
        val Instance = ParameterType.Single("AWS::EC2::Instance::Id")
        val KeyPair = ParameterType.Single("AWS::EC2::KeyPair::KeyName")

        object SecurityGroup {
            val Name = ParameterType.Single("AWS::EC2::SecurityGroup::GroupName")
            val Id = ParameterType.Single("AWS::EC2::SecurityGroup::Id")
        }

        val Subnet = ParameterType.Single("AWS::EC2::Subnet::Id")
        val Volume = ParameterType.Single("AWS::EC2::Volume::Id")
        val VPC = ParameterType.Single("AWS::EC2::VPC::Id")
    }

    object Route53 {
        val HostedZone = ParameterType.Single("AWS::Route53::HostedZone::Id")
    }
}

object Types {
    val Str = ParameterType.Single("String")
    val Number = ParameterType.Single("Number")
    val CommaDelimitedList = ParameterType.Single("CommaDelimitedList")
}


class ParameterTypeSerializer : StdSerializer<ParameterType>(ParameterType::class.java) {
    override fun serialize(value: ParameterType?, gen: JsonGenerator?, provider: SerializerProvider?) {
        gen?.writeString(value?.type)
    }

}