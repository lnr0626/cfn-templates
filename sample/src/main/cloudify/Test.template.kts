
import com.lloydramey.cfn.model.parameters.Types.Str
import com.lloydramey.cfn.model.functions.*
import com.lloydramey.cfn.model.resources.attributes.*
import com.lloydramey.cfn.model.functions.Equals
import com.lloydramey.cfn.model.functions.Ref
import com.lloydramey.cfn.model.functions.Val

val EnvType by parameter(Str) {
    description = "Environment Type"
    default = "test"
    allowedValues = listOf("prod", "test")
    constraintDescription = "must specify prod or test."
}

val CreateProdResource by condition { Equals(Ref(EnvType), Val("prod")) }

data class RegionData(val AMI: String, val TestAZ: String)

val RegionMap by mapping {
    key("us-east-1", "AMI" to "ami-7f418316", "TestAZ" to "us-east-1a")
    key("us-west-1", "AMI" to "ami-951945d0", "TestAZ" to "us-west-1a")
    key("us-west-2", "AMI" to "ami-16fd7026", "TestAZ" to "us-west-2a")
    key("eu-west-1", "AMI" to "ami-24506250", "TestAZ" to "eu-west-1a")
    key("sa-east-1", "AMI" to "ami-3e3be423", "TestAZ" to "sa-east-1a")
    key("ap-southeast-1", "AMI" to "ami-74dda626", "TestAZ" to "ap-southeast-1a")
    key("ap-southeast-2", "AMI" to "ami-b3990e89", "TestAZ" to "ap-southeast-2a")
    key("ap-northeast-1", "AMI" to "ami-dcfa4edd", "TestAZ" to "ap-northeast-1a")
}
//val EC2Instance by resource<Instance> {
//    imageId = FindInMap(RegionMap, AWS.Region, "AMI")
//}
//val NewVolume by resource<Volume>(ConditionalOn(CreateProdResource)) {
//    size = Val(100)
//    availabilityZone = instance["AvailabilityZone"]
//}
//val MountPoint by resource<VolumeAttachment>(DependsOn(NewVolume), ConditionalOn(CreateProdResource)) {
//    instanceId = Ref(instance)
//    volumeId = Ref(NewVolume)
//    device = Val("/dev/sdh")
//}
//
//val VolumeId by output(ConditionalOn(CreateProdResource)) {
//    value = Ref(NewVolume)
//}