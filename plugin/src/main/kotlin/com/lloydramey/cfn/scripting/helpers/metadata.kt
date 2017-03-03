package com.lloydramey.cfn.scripting.helpers

import com.lloydramey.cfn.scripting.CfnTemplateScript
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


class MetadataDelegate(val value: Any) : ReadOnlyProperty<CfnTemplateScript, TemplateMetadata> {
    override fun getValue(thisRef: CfnTemplateScript, property: KProperty<*>): TemplateMetadata = TemplateMetadataImpl(property.name, value)
}

sealed class TemplateMetadata
internal data class TemplateMetadataImpl(val name: String, val value: Any) : TemplateMetadata()
