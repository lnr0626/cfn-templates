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
package com.lloydramey.cfn.scripting.helpers

import com.lloydramey.cfn.model.Export
import com.lloydramey.cfn.model.Output
import com.lloydramey.cfn.model.functions.AwsTemplateValue
import com.lloydramey.cfn.model.resources.attributes.ConditionalOn
import com.lloydramey.cfn.scripting.CfnTemplateScript
import kotlin.properties.Delegates
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class OutputDefinition(val condition: ConditionalOn?) {
    var value by Delegates.notNull<AwsTemplateValue>()
    var description: String? = null
    var export: Export? = null

    fun exportAs(name: AwsTemplateValue) {
        export = Export(name)
    }

    fun exportAs(name: String) {
        export = Export(name)
    }
}

class OutputDelegate(condition: ConditionalOn? = null, init: OutputDefinition.() -> Unit) : ReadOnlyProperty<CfnTemplateScript, Output> {
    val value = OutputDefinition(condition)

    init {
        value.init()
    }

    override fun getValue(thisRef: CfnTemplateScript, property: KProperty<*>) = Output(
        id = property.name,
        condition = value.condition,
        desciprtion = value.description,
        export = value.export,
        value = value.value
    )

}
