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

import com.lloydramey.cfn.model.functions.AwsTemplateValue
import com.lloydramey.cfn.model.functions.Val
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class AwsValueVerifier : ReadWriteProperty<ResourceProperties, AwsTemplateValue?> {
    private var value: AwsTemplateValue? = null

    override fun getValue(thisRef: ResourceProperties, property: KProperty<*>): AwsTemplateValue? = value

    override fun setValue(thisRef: ResourceProperties, property: KProperty<*>, value: AwsTemplateValue?) {
        if (value == null) {
            throw IllegalArgumentException("${property.name} in ${thisRef.resourceType} cannot be null")
        }

        if (value is Val) {
            validateVal(value, thisRef.resourceType, property.name)
        }

        this.value = value
    }

    private fun AwsValueVerifier.validateVal(value: Val, resourceType: String, name: String) {
        val int = value.value.toIntOrNull()
        if (int != null) {
            validate(int, resourceType, name)
        } else {
            try {
                val bool = value.value.toBoolean()
                validate(bool, resourceType, name)
            } catch (e: Exception) {
                validate(value.value, resourceType, name)
            }
        }
    }

    protected abstract fun validate(int: Int, resourceType: String, name: String)
    protected abstract fun validate(str: String, resourceType: String, name: String)
    protected abstract fun validate(bool: Boolean, resourceType: String, name: String)
}

class IntValidator(val min: Int = Int.MIN_VALUE, val max: Int = Int.MAX_VALUE) : AwsValueVerifier() {
    override fun validate(int: Int, resourceType: String, name: String) {
        if (int < min) throw IllegalArgumentException("$name in $resourceType must be greater than or equal to $min")
        if (int > max) throw IllegalArgumentException("$name in $resourceType must be less than or equal to $max")
    }

    override fun validate(str: String, resourceType: String, name: String) {
        throw IllegalArgumentException("You must specify an Int for $name in $resourceType")
    }

    override fun validate(bool: Boolean, resourceType: String, name: String) {
        throw IllegalArgumentException("You must specify an Int for $name in $resourceType")
    }

}

class BooleanValidator : AwsValueVerifier() {
    override fun validate(int: Int, resourceType: String, name: String) {
        throw IllegalArgumentException("You must specify an Boolean for $name in $resourceType")
    }

    override fun validate(str: String, resourceType: String, name: String) {
        throw IllegalArgumentException("You must specify an Boolean for $name in $resourceType")
    }

    override fun validate(bool: Boolean, resourceType: String, name: String) {
        // no-op, just verifying that it is a boolean
    }
}

class StringValidator(
    val minLength: Int = 0,
    val maxLength: Int = Int.MAX_VALUE,
    val regex: Regex = Regex(".*")
) : AwsValueVerifier() {
    override fun validate(int: Int, resourceType: String, name: String) {
        validate(int.toString(), resourceType, name)
    }

    override fun validate(bool: Boolean, resourceType: String, name: String) {
        validate(bool.toString(), resourceType, name)
    }

    override fun validate(str: String, resourceType: String, name: String) {
        if (str.length < minLength) throw IllegalArgumentException("$name in $resourceType must be at least $minLength characters long")
        if (str.length > maxLength) throw IllegalArgumentException("$name in $resourceType must be at most $maxLength characters long")
        if (!str.matches(regex)) throw IllegalArgumentException("$name in $resourceType does not match validation regex ($regex)")
    }
}
