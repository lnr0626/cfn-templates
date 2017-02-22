# cfn-templates
This project is aiming towards a better way to define cloud formation templates. I've found that
writing templates by hand is an error prone and laborious task. It's also difficult to catch errors
and to figure out what the error actually is. This project is based of ideas I've seen in other places - 
such as troposphere (which is a wonderful project, I just can't use it for various reasons).

I chose kotlin for this project mostly because I enjoy writing code in it, and it's JVM based. It also 
provides (in most cases) a concise syntax for specifying things.

## Supported Types

This list may be slighted outdated. You can get the most up to date list by running the test
 com.lloydramey.cfn.model.ListAllResourcePropertyTypes.listAllResourcePropertyTypes() - this will print the list of supported types to stdout
 
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


## Some general thoughts

The following is an example taken from the AWS CFN documentation and some thoughts on how that could look:

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

```kotlin
val EnvType = parameter("EnvType", Str) {
    description = "Environment Type"
    default = "test"
    allowedValues = listOf("prod", "test")
    constraintDescription = "must specify prod or test."
}
val CreateProdResource = condition("CreateProdResources", Equals(Ref(EnvType), Val("prod")))
val RegionMap = mapping("RegionMap") {
    key("us-east-1", "AMI" to "ami-7f418316", "TestAZ" to "us-east-1a")
    key("us-west-1", "AMI" to "ami-951945d0", "TestAz" to "us-west-1a")
    key("us-west-2", "AMI" to "ami-16fd7026", "TestAz" to "us-west-2a")   
    key("eu-west-1", "AMI" to "ami-24506250", "TestAz" to "eu-west-1a")      
    key("sa-east-1", "AMI" to "ami-3e3be423", "TestAz" to "sa-east-1a")      
    key("ap-southeast-1", "AMI" to "ami-74dda626", "TestAz" to "ap-southeast-1a")
    key("ap-southeast-2", "AMI" to "ami-b3990e89", "TestAz" to "ap-southeast-2a")
    key("ap-northeast-1", "AMI" to "ami-dcfa4edd", "TestAz" to "ap-northeast-1a")
}
val instance = resource<Instance>("EC2Instance") {
    imageId = FindInMap(RegionMap, AWS.Region, "AMI")
}
val volume = resource<Volume>("NewVolume", ConditionalOn(CreateProdResource)) {
    size = Val(100)
    availabilityZone = instance["AvailabilityZone"]
}
resource<VolumeAttachment>("MountPoint", DependsOn(volume), ConditionalOn(CreateProdResource)) {
    instanceId = Ref(instance)
    volumeId = Ref(volume)
    device = Val("/dev/sdh")
}

output("VolumeId", ConditionalOn(CreateProdResource)) {
    value = Ref(volume)
}

```

I'm still playing around with syntax to see what I think looks nice and makes sense.
My main reasons aren't just for conciseness - that's nice, but not worth it. I think that
moving to a typed language for this has a number of benefits. The include: it's possible to
use the compiler to aid in validating something like the latter compare to the former, it
makes it possible to create libraries of cloudformation templates that can be easily shared,
and when updated errors are caught much earlier, and a few others. I'm also generally of
the opinion that code is easier to write, read, and debug that configuration files - that
may be one of the main reasons I prefer tools such as gradle and gulp to maven and grunt.
