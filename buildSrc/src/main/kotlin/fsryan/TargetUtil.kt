package fsryan

import org.gradle.api.GradleException
import org.gradle.api.Project

/**
 * The name of the hostOs
 */
val hostOs: String by lazy { System.getProperty("os.name") }

/**
 * Returns the name of the native target for the host OS.
 */
fun nativeTarget(): String = hostOs.let {
    when {
        it == "Mac OS X" -> "MacosX64"
        it == "Linux" -> "LinuxX64"
        it.startsWith("Windows") -> "MingwX64"
        else -> throw GradleException("Host $it is not supported in Kotlin/Native.")
    }
}

/**
 * Returns whether to configure the iOS target
 */
fun Project.shouldConfigureIOS(): Boolean = canBuildIOS() && isIOSEnabled()
/**
 * Returns whether to configure the macOS target
 */
fun Project.shouldConfigureMacOS(): Boolean = canBuildMacOS() && isMacOSEnabled()
/**
 * Returns whether to configure the Android target
 */
fun Project.shouldConfigureAndroid(): Boolean = isAndroidEnabled()
/**
 * Returns whether to configure the JVM target
 */
fun Project.shouldConfigureJvm(): Boolean = isJvmEnabled(this)
/**
 * Returns whether to configure the Windows target
 */
fun Project.shouldConfigureWindows(): Boolean = canBuildWindows() && isWindowsEnabled()
/**
 * Returns whether to configure the JS target
 */
fun Project.shouldConfigureJs(): Boolean = isJsEnabled()
/**
 * Returns whether to configure the Linux target
 */
fun Project.shouldConfigureLinux(): Boolean = canBuildLinux() && isLinuxEnabled()

/**
 * Returns whether the host OS is Mac OS X, and thus, can build iOS version
 */
fun Project.canBuildIOS(): Boolean = hostOs == "Mac OS X"
/**
 *  Returns whether the host OS is Mac OS X, and thus, can build Mac OS version
 */
fun Project.canBuildMacOS(): Boolean = hostOs == "Mac OS X"
/**
 * Returns whether the host OS is Windows, and thus, can build MingwX64 version
 */
fun Project.canBuildWindows(): Boolean = hostOs.startsWith("Windows")
/**
 * Returns whether the host OS is Linux, and thus, can build LinuxX64 version
 */
fun Project.canBuildLinux(): Boolean = hostOs == "Linux"

/**
 * Returns whether the iOS target is enabled
 */
fun Project.isIOSEnabled() : Boolean = isTargetEnabled("ios")
/**
 * Returns whether the Mac OS target is enabled
 */
fun Project.isMacOSEnabled() : Boolean = isTargetEnabled("macos")
/**
 * Returns whether the Android target is enabled
 */
fun Project.isAndroidEnabled() : Boolean = isTargetEnabled("android")
/**
 * Returns whether the JVM target is enabled
 */
fun Project.isJvmEnabled(project: Project): Boolean = isTargetEnabled("jvm")
/**
 * Returns whether the JS target is enabled
 */
fun Project.isJsEnabled(): Boolean = isTargetEnabled("js")
/**
 * Returns whether the Windows target is enabled
 */
fun Project.isWindowsEnabled(): Boolean = isTargetEnabled("windows")
/**
 * Returns whether the Linux target is enabled
 */
fun Project.isLinuxEnabled(): Boolean = isTargetEnabled("linux")

/**
 * Returns whether not the target is enabled via gradle property
 */
fun Project.isTargetEnabled(targetName: String): Boolean {
    val propName = "fsryan.$targetName"
    val propValue = BuildProperties.prop(this, propName, defaultValue = true.toString())
    return propValue.toBoolean()
}