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
package com.lloydramey.cfn.model.resources

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.lloydramey.cfn.model.functions.Condition

@JsonSerialize(using = ConditionalOnSerializer::class)
data class ConditionalOn(val condition: Condition) : ResourceDefinitionAttribute("Condition")

class ConditionalOnSerializer : StdSerializer<ConditionalOn>(ConditionalOn::class.java) {
    override fun serialize(value: ConditionalOn?, gen: JsonGenerator?, provider: SerializerProvider?) {
        gen?.writeString(value?.condition?.id)
    }
}

