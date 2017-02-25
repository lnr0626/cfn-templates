package template

import com.lloydramey.cfn.model.aws.ApiGateway
import com.lloydramey.cfn.model.parameters.Types.*
import tests.*

val test = "asdf"

val another = ApiGateway.Account()

val param = Str

var blah = Testing()

description = "This is a test"

val EnvType = parameter("EnvType", Str) {
    description = "Environment Type"
    default = "test"
    allowedValues = listOf("prod", "test")
    constraintDescription = "must specify prod or test."
}
