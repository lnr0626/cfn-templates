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
package com.lloydramey.cfn.model

import com.fasterxml.jackson.annotation.JsonAnyGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.lloydramey.cfn.model.functions.AwsTemplateValue
import com.lloydramey.cfn.model.functions.Val
import com.lloydramey.cfn.model.resources.ConditionalOn

data class Export(val name: AwsTemplateValue) {
    constructor(name: String) : this(Val(name))
}

data class Output(
    @JsonIgnore val name: String,
    @JsonIgnore val condition: ConditionalOn? = null,
    val value: AwsTemplateValue,
    val description: String? = null,
    val export: Export? = null
) {
    @JsonAnyGetter
    fun json() = if(condition != null) mapOf("Condition" to condition) else emptyMap()
}
