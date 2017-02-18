import com.lloydramey.cfn.model.resources.ResourceProperties
import org.junit.Test
import org.reflections.Reflections

class ListAllResourcePropertyTypes {
    @Test
    fun listAllResourcePropertyTypes() {
        val subtypes = Reflections("com.lloydramey.cfn.model.aws").getSubTypesOf(ResourceProperties::class.java)

        println(subtypes
            .map { it.getMethod("getResourceType").invoke(it.newInstance()) }
            .joinToString("\n"))

    }
}