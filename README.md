# cloudify-templates
This project is aiming towards a better way to define cloud formation templates. I've found that
writing templates by hand is an error prone and laborious task. It's also difficult to catch errors
and to figure out what the error actually is. This project is based of ideas I've seen in other places - 
such as troposphere (which is a wonderful project, I just can't use it for various reasons).

This is still changing fairly rapidly, but I'm starting to settle on a syntax. My next task is to finish the gradle
plugin and write an IDEA plugin to enable better syntax highlighting and code completion. I do not currently plan on
writing an eclipse plugin for this, mostly because I never use eclipse.

Once I've finished the plugins, I will resume adding resource types. I plan on supporting all of the AWS resource types,
and am considering adding support for the open stack resource types. I think it'd be cool to write something to automatically
generate the subclasses based on the AWS documentation, but I also feel that would be too much work to be worthwhile.

## Supported Types

This list may be slighted outdated. You can get the most up to date list by running the test
 com.lloydramey.cloudify.model.ListAllResourcePropertyTypes.listAllResourcePropertyTypes() - this will print the list of supported types to stdout
 
- AWS::ApiGateway::Account
- AWS::ApiGateway::ApiKey
- AWS::ApiGateway::Authorizer
- AWS::ApiGateway::BasePathMapping
- AWS::ApiGateway::ClientCertificate
- AWS::ApiGateway::Deployment
- AWS::ApiGateway::Method
- AWS::ApiGateway::Model
- AWS::ApiGateway::Resource
- AWS::ApiGateway::RestApi
- AWS::ApiGateway::Stage
- AWS::ApiGateway::UsagePlan
- AWS::ApplicationAutoScaling::ScalableTarget
- AWS::ApplicationAutoScaling::ScalingPolicy
- AWS::AutoScaling::AutoScalingGroup
- AWS::AutoScaling::LaunchConfiguration
- AWS::AutoScaling::LifecycleHook
- AWS::AutoScaling::ScalingPolicy
- AWS::AutoScaling::ScheduledAction
- AWS::CertificateManager::Certificate


## Syntax

I plan on writing some tests to verify behavior which are pure conversions of the AWS examples. Those should serve as 
nice syntax examples. However, for the time being, the following is an example taken from the documentation which 
demonstrates most aspects of CFN templates.

```json
{
  "AWSTemplateFormatVersion" : "2010-09-09",

  "Mappings" : {
    "RegionMap" : {
      "us-east-1"      : { "AMI" : "ami-7f418316", "TestAz" : "us-east-1a" },
      "us-west-1"      : { "AMI" : "ami-951945d0", "TestAz" : "us-west-1a" },
      "us-west-2"      : { "AMI" : "ami-16fd7026", "TestAz" : "us-west-2a" },
      "eu-west-1"      : { "AMI" : "ami-24506250", "TestAz" : "eu-west-1a" },
      "sa-east-1"      : { "AMI" : "ami-3e3be423", "TestAz" : "sa-east-1a" },
      "ap-southeast-1" : { "AMI" : "ami-74dda626", "TestAz" : "ap-southeast-1a" },
      "ap-southeast-2" : { "AMI" : "ami-b3990e89", "TestAz" : "ap-southeast-2a" },
      "ap-northeast-1" : { "AMI" : "ami-dcfa4edd", "TestAz" : "ap-northeast-1a" }
    }
  },
    
  "Parameters" : {
    "EnvType" : {
      "Description" : "Environment resourceType.",
      "Default" : "test",
      "Type" : "String",
      "AllowedValues" : ["prod", "test"],
      "ConstraintDescription" : "must specify prod or test."
    }
  },
  
  "Conditions" : {
    "CreateProdResources" : {"Fn::Equals" : [{"Ref" : "EnvType"}, "prod"]}
  },
  
  "Resources" : {
    "EC2Instance" : {
      "Type" : "AWS::EC2::Instance",
      "Properties" : {
        "ImageId" : { "Fn::FindInMap" : [ "RegionMap", { "Ref" : "AWS::Region" }, "AMI" ]}
      }
    },
    
    "MountPoint" : {
      "Type" : "AWS::EC2::VolumeAttachment",
      "Condition" : "CreateProdResources",
      "Properties" : {
        "InstanceId" : { "Ref" : "EC2Instance" },
        "VolumeId"  : { "Ref" : "NewVolume" },
        "Device" : "/dev/sdh"
      }
    },

    "NewVolume" : {
      "Type" : "AWS::EC2::Volume",
      "Condition" : "CreateProdResources",
      "Properties" : {
        "Size" : "100",
        "AvailabilityZone" : { "Fn::GetAtt" : [ "EC2Instance", "AvailabilityZone" ]}
      }
    }
  },
  
  "Outputs" : {
    "VolumeId" : {
      "Value" : { "Ref" : "NewVolume" }, 
      "Condition" : "CreateProdResources"
    }
  }  
}
```

This is an equivalent template using Cloudify:

```kotlin
val EnvType by parameter(Str) {
    description = "Environment Type"
    default = "test"
    allowedValues = listOf("prod", "test")
    constraintDescription = "must specify prod or test."
}

val CreateProdResource by condition { Equals(Ref(EnvType), Val("prod")) }

class RegionData(val AMI: String, val TestAZ: String)

val RegionMap by mapping {
    "us-east-1" { RegionData(AMI = "ami-7f418316", TestAZ = "us-east-1a") }
    "us-west-1" { RegionData(AMI = "ami-951945d0", TestAz = "us-west-1a") }
    "us-west-2" { RegionData(AMI = "ami-16fd7026", TestAz = "us-west-2a") }
    "eu-west-1" { RegionData(AMI = "ami-24506250", TestAz = "eu-west-1a") }
    "sa-east-1" { RegionData(AMI = "ami-3e3be423", TestAz = "sa-east-1a") }
    "ap-southeast-1" { RegionData(AMI = "ami-74dda626", TestAz = "ap-southeast-1a") }
    "ap-southeast-2" { RegionData(AMI = "ami-b3990e89", TestAz = "ap-southeast-2a") }
    "ap-northeast-1" { RegionData(AMI = "ami-dcfa4edd", TestAz = "ap-northeast-1a") }
}
val EC2Instance by resource<Instance> {
    imageId = FindInMap(RegionMap, AWS.Region, "AMI")
}
val NewVolume by resource<Volume>(ConditionalOn(CreateProdResource)) {
    size = Val(100)
    availabilityZone = instance["AvailabilityZone"]
}
val MountPoint by resource<VolumeAttachment>(DependsOn(volume), ConditionalOn(CreateProdResource)) {
    instanceId = Ref(instance)
    volumeId = Ref(volume)
    device = Val("/dev/sdh")
}

val VolumeId by output(ConditionalOn(CreateProdResource)) {
    value = Ref(volume)
}

```

#TODO
- AWS models for cloud formation resources
- IDEA plugin for syntax highlighting nicities
