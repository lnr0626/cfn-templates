package com.lloydramey.cfn.model

abstract class ResourceTest(val id: String, val condition: ConditionFunction?) {
    abstract fun properties(): Map<String, Any>
}

class Volume(id: String, condition: ConditionFunction?) : ResourceTest(id, condition) {
    override fun properties(): Map<String, Any> = mapOf()

}

class Instance(id: String, condition: ConditionFunction?) : ResourceTest(id, condition) {
    var test: String = ""
    override fun properties(): Map<String, Any> = mapOf()
}

inline fun <reified T : ResourceTest> resource(id: String, condition: ConditionFunction? = null, init: T.() -> Unit): T {
    val constructor = T::class.java.getConstructor(String::class.java, ConditionFunction::class.java)
    val res = constructor.newInstance(id, condition)!!
    res.init()
    return res
}


object Test {
    val instance: Instance = resource("EC2Instance") {
        test = "asdf"
    }
}