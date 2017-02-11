package com.lloydramey.cfn.model

data class Condition(val id: String, val condition: ConditionFunction)

interface ConditionFunction {
    operator fun not(): ConditionFunction = Not(this)
}

class And(conditions: List<ConditionFunction>) : Fn("Fn::And", conditions), ConditionFunction {
    constructor(vararg conditions: ConditionFunction): this(conditions.asList())
}

class Equals(left: Any, right: Any) : Fn("Fn::Equals", listOf(left, right)), ConditionFunction

class If(condition: String, ifTrue: Any, ifFalse: Any) : Fn("Fn::If", listOf(condition, ifTrue, ifFalse)), ConditionFunction

class Not(condition: ConditionFunction) : Fn("Fn::Not", condition), ConditionFunction
class Or(conditions: List<ConditionFunction>) : Fn("Fn::Or", conditions), ConditionFunction {
    constructor(vararg conditions: ConditionFunction) : this(conditions.asList())
}

class ConditionReference(condition: String) : Fn("Condition", condition), ConditionFunction
