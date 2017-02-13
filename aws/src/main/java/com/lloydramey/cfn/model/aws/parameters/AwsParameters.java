package com.lloydramey.cfn.model.aws.parameters;

import com.lloydramey.cfn.model.ParameterType;

public class AwsParameters {
    public static class EC2 {
        public static final ParameterType AvailabilityZone = new ParameterType.Single("AWS::EC2::AvailabilityZone::Name");
        public static final ParameterType Image = new ParameterType.Single("AWS::EC2::Image::Id");
        public static final ParameterType Instance = new ParameterType.Single("AWS::EC2::Instance::Id");
        public static final ParameterType KeyPair = new ParameterType.Single("AWS::EC2::KeyPair::KeyName");

        public static class SecurityGroup {
            public static final ParameterType Name = new ParameterType.Single("AWS::EC2::SecurityGroup::GroupName");
            public static final ParameterType Id = new ParameterType.Single("AWS::EC2::SecurityGroup::Id");
        }

        public static final ParameterType Subnet = new ParameterType.Single("AWS::EC2::Subnet::Id");
        public static final ParameterType Volume = new ParameterType.Single("AWS::EC2::Volume::Id");
        public static final ParameterType VPC = new ParameterType.Single("AWS::EC2::VPC::Id");
    }

    public static class Route53 {
        public static final ParameterType HostedZone = new ParameterType.Single("AWS::Route53::HostedZone::Id");
    }
}
