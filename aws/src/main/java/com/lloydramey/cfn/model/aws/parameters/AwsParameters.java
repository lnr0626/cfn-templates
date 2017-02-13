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
