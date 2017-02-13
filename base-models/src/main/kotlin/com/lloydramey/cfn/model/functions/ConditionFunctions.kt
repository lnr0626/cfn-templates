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
package com.lloydramey.cfn.model.functions

data class Condition(val id: String, val condition: ConditionFunction)

interface AllowedInIfValues
interface AllowedInConditionFunction
interface ConditionFunction : AllowedInConditionFunction {
    operator fun not(): ConditionFunction = Not(this)
}

class And(conditions: List<AllowedInConditionFunction>) : Fn("Fn::And", conditions), ConditionFunction {
    constructor(vararg conditions: AllowedInConditionFunction): this(conditions.asList())
}

class Equals(left: AllowedInConditionFunction, right: AllowedInConditionFunction) : Fn("Fn::Equals", listOf(left, right)), ConditionFunction {
    constructor(left: AllowedInConditionFunction, right: String) : this(left, Val(right))
    constructor(left: AllowedInConditionFunction, right: Number) : this(left, Val(right))
    constructor(left: String, right: AllowedInConditionFunction) : this(Val(left), right)
    constructor(left: Number, right: AllowedInConditionFunction) : this(Val(left), right)
    constructor(left: String, right: String) : this(Val(left), Val(right))
    constructor(left: Number, right: Number) : this(Val(left), Val(right))
}

class If(condition: Condition, ifTrue: AllowedInIfValues, ifFalse: AllowedInIfValues) : Fn("Fn::If", listOf(condition.id, ifTrue, ifFalse)), ConditionFunction, AllowedInIfValues {
    constructor(condition: Condition, left: AllowedInIfValues, right: String) : this(condition, left, Val(right))
    constructor(condition: Condition, left: AllowedInIfValues, right: Number) : this(condition, left, Val(right))
    constructor(condition: Condition, left: String, right: AllowedInIfValues) : this(condition, Val(left), right)
    constructor(condition: Condition, left: Number, right: AllowedInIfValues) : this(condition, Val(left), right)
    constructor(condition: Condition, left: String, right: String) : this(condition, Val(left), Val(right))
    constructor(condition: Condition, left: Number, right: Number) : this(condition, Val(left), Val(right))
}

class Not(condition: AllowedInConditionFunction) : Fn("Fn::Not", condition), ConditionFunction
class Or(conditions: List<AllowedInConditionFunction>) : Fn("Fn::Or", conditions), ConditionFunction {
    constructor(vararg conditions: AllowedInConditionFunction) : this(conditions.asList())
}

class ConditionReference(condition: Condition) : Fn("Condition", condition.id), ConditionFunction
