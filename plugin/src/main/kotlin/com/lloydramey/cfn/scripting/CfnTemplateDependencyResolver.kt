package com.lloydramey.cfn.scripting

import com.lloydramey.cfn.coroutines.future
import org.jetbrains.kotlin.script.KotlinScriptExternalDependencies
import org.jetbrains.kotlin.script.ScriptContents
import org.jetbrains.kotlin.script.ScriptDependenciesResolver
import java.util.concurrent.Future


internal
typealias Environment = Map<String, Any?>

class CfnTemplateDependencyResolver : ScriptDependenciesResolver {
    override fun resolve(
        script: ScriptContents,
        environment: Map<String, Any?>?,
        report: (ScriptDependenciesResolver.ReportSeverity, String, ScriptContents.Position?) -> Unit,
        previousDependencies: KotlinScriptExternalDependencies?): Future<KotlinScriptExternalDependencies?> = future {
        KotlinScriptExternalDependenciesWithImports(ImplicitImports.list)
    }
}
