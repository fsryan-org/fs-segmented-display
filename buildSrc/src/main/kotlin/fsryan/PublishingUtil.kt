package fsryan

import fsryan.BuildProperties.evaluateProperty
import org.gradle.api.Project

fun Project.fsryanMavenUser(): String {
    return project.evaluateProperty(
        propName = "com.fsryan.fsryan_maven_repo_user",
        envVarName = "FSRYAN_MAVEN_USER"
    )
}

fun Project.fsryanMavenRepoPassword(release: Boolean = true): String {
    if (release) {
        return project.evaluateProperty(
            propName = "com.fsryan.fsryan_release_password",
            envVarName = "FSRYAN_MAVEN_RELEASE_PASSWORD"
        )
    }
    return project.evaluateProperty(propName = "com.fsryan.fsryan_snapshot_password", envVarName = "FSRYAN_MAVEN_SNAPSHOT_PASSWORD")
}

fun Project.fsryanNPMRegistryName(): String {
    return project.evaluateProperty(propName = "com.fsryan.npm_registry_name", envVarName = "NPM_REGISTRY_NAME")
}

fun Project.fsryanNPMRegistryUrl(includeProtocol: Boolean = true): String {
    val npmUrl = project.evaluateProperty(propName = "com.fsryan.npm_registry_url", envVarName = "NPM_REGISTRY_URL")
    return if (includeProtocol) npmUrl else npmUrl.removePrefix("https:")
}

fun Project.fsryanNPMRegistryToken(): String {
    return project.evaluateProperty(propName = "com.fsryan.npm_registry_token", envVarName = "NPM_REGISTRY_TOKEN")
}