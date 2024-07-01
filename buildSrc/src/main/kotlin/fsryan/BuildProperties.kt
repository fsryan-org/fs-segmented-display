package fsryan

import org.gradle.api.Project
import java.util.*

/**
 * Our continuous integration builds pull their properties from a securely
 * stored file on our CI/CD server.
 *
 * From now on, when you get a property in any of the build.gradle.kts files,
 * you should access the property via the [evaluateProperty] function.
 */
object BuildProperties {

    private val props = Properties()

    fun initializeWith(project: Project) {
        println("property override path: ${project.findProperty("fsryan.propertyOverridePath")}")
        project.findProperty("fsryan.propertyOverridePath")?.toString()?.let { filepath ->
            val propsFile = project.rootProject.file(filepath)

            if (propsFile.exists()) {
                propsFile.inputStream().use { props.load(it) }
            } else {
                project.logger.error("File does not exist: $filepath")
            }
        }
        if (props.isEmpty) {
            project.logger.error("NO PROPERTY OVERRIDES LOADED")
        } else {
            project.logger.error("Loaded property overrides from path ${project.findProperty("fsryan.propertyOverridePath")}")
        }
    }

    fun prop(
        project: Project,
        propName: String,
        envVarName: String = "",
        preferEnvVar: Boolean = false,
        defaultValue: String? = null
    ): String = project.evaluateProperty(propName, envVarName, preferEnvVar, defaultValue)

    fun Project.evaluateProperty(
        propName: String,
        envVarName: String = "",
        preferEnvVar: Boolean = false,
        defaultValue: String? = null
    ): String = evaluatePropertyWith(this, propName, envVarName, preferEnvVar, defaultValue)

    fun evaluatePropertyWith(
        project: Project,
        propName: String,
        envVarName: String = "",
        preferEnvVar: Boolean = false,
        defaultValue: String? = null
    ): String {
        if (envVarName.isNotEmpty() && preferEnvVar) {
            System.getenv()[envVarName]?.let { return@evaluatePropertyWith it }
        }
        return props[propName]?.toString()
            ?: project.findProperty(propName)?.toString()
            ?: (if (envVarName.isNotEmpty()) System.getenv()[envVarName] else null)
            ?: defaultValue
            ?: throw IllegalStateException("no such property '$propName' or env var '$envVarName'")
    }
}