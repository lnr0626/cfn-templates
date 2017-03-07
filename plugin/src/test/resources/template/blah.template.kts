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
package template
import com.lloydramey.cfn.model.parameters.Types.Str
import com.lloydramey.cfn.model.functions.*
import com.lloydramey.cfn.model.resources.attributes.*
import com.lloydramey.cfn.model.functions.Equals
import com.lloydramey.cfn.model.functions.Ref
import com.lloydramey.cfn.model.functions.Val

val EnvType by parameter(Str) {
    description = "The environment to deploy to"
    default = "test"
    allowedValues = listOf("test", "prod")
}

val CreateProdResources by condition { Equals(Ref(EnvType), Val("prod")) }

val CloudWatchArn by parameter(Str) {
    description = "ARN for cloudwatch"
    default = "test"
}

val Output by output {
    value = Ref(CloudWatchArn)
    description = "The thing that does the thing"
}

val Output3 by output(ConditionalOn(CreateProdResources)) {
    value = Ref(CloudWatchArn)
    description = "The thing that does the thing"
}