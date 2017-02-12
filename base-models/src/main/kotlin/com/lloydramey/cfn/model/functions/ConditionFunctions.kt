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
