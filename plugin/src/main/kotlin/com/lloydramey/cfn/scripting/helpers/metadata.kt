/*
 * Copyright ${year} ${name} <${email}>
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
package com.lloydramey.cfn.scripting.helpers

import com.lloydramey.cfn.scripting.CfnTemplateScript
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class MetadataDelegate(val value: Any) : ReadOnlyProperty<CfnTemplateScript, TemplateMetadata> {
    override fun getValue(thisRef: CfnTemplateScript, property: KProperty<*>): TemplateMetadata = TemplateMetadata(property.name, value)
}

data class TemplateMetadata(val name: String, val value: Any)
