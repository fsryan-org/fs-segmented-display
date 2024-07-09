import fsryan.shouldConfigureAndroid
import fsryan.shouldConfigureIOS
import fsryan.shouldConfigureJvm
import fsryan.shouldConfigureWASM
import org.jetbrains.dokka.DokkaConfiguration
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinMultiplatformPluginWrapper
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    `maven-publish`
    signing
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kover)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    if (shouldConfigureWASM()) {
        wasmJs {
            moduleName = "fs-segmented-display"
            browser {
                testTask {
                    useKarma {
                        useChromeHeadless()
                    }
                }
            }
            binaries.library()
        }
    }

    if (shouldConfigureAndroid()) {
        androidTarget {
            publishLibraryVariants("release")

            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }
    }

    if (shouldConfigureJvm()) {
        jvm("jvm")
        jvmToolchain(17)
    }

    if (shouldConfigureIOS()) {
        iosArm64()
        iosSimulatorArm64()
        iosX64()
    }

    
    sourceSets {
        val jvmMain by getting

        androidMain.dependencies {
        }

        maybeCreate("androidDebug").apply {
            dependencies {
            }
        }

        commonMain.dependencies {
            implementation(compose.foundation)
            implementation(compose.ui)
        }
    }
}

android {
    namespace = "com.fsryan.ui"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}

tasks.register<Jar>("dokkaHtmlJar") {
    dependsOn(tasks.dokkaHtml)
    from(tasks.dokkaHtml.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

tasks.withType<DokkaTask>().configureEach {
    dokkaSourceSets.configureEach {
        documentedVisibilities.set(
            setOf(DokkaConfiguration.Visibility.PUBLIC, DokkaConfiguration.Visibility.PROTECTED)
        )

        jdkVersion.set(17)

        perPackageOption {
            matchingRegex.set(".*internal.*")
            suppress.set(true)
        }
    }
}

// Workaround found here: https://slack-chats.kotlinlang.org/t/13149393/i-m-getting-the-following-two-errors-when-trying-to-publish-
// Further information found here: https://youtrack.jetbrains.com/issue/KT-46466
tasks.withType<AbstractPublishToMaven>().configureEach {
    val signingTasks = tasks.withType<Sign>()
    mustRunAfter(signingTasks)
}

var configuredPublishing = false
var configuredMultiplatformPublishing = false
var configuredSigning = false

fun configureMultiplatformPublishingIfPossible(publishingExtension: PublishingExtension?) {
    if (configuredMultiplatformPublishing) {
        return
    }
    if (!plugins.hasPlugin("org.jetbrains.kotlin.multiplatform")) {
        return
    }
    val actual = publishingExtension ?: extensions.findByType(PublishingExtension::class) ?: return

    actual.lazyConfigureMultiplatformPublishing(project)
    configuredMultiplatformPublishing = true
}

fun configurePublishingIfPossible() {
    extensions.findByType(PublishingExtension::class)?.let { publishing ->
        publishing.repositories {
            maven {
                name = "mavenCentral"
                url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = project.findProperty("com.fsryan.publishing.ossrh.release.username")?.toString().orEmpty()
                    password = project.findProperty("com.fsryan.publishing.ossrh.release.password")?.toString().orEmpty()
                }
            }
        }
        configuredPublishing = true
        configureMultiplatformPublishingIfPossible(publishingExtension = publishing)
    }
}

fun configureSigningIfPossible() {
    extensions.findByType(PublishingExtension::class)?.let { publishing ->
        extensions.findByType(SigningExtension::class)?.let { signingExtension ->
            if (project.hasProperty("signing.keyId")) {
                println("signing.keyId FOUND!!!")
                if (project.hasProperty("signing.password")) {
                    println("signing.password FOUND!!!")
                    if (project.hasProperty("signing.secretKeyRingFile")) {
                        println("signing.secretKeyRingFile FOUND!!!")
                        signingExtension.sign(publishing.publications)
                        configuredSigning = true
                    } else {
                        println("Missing signing.secretKeyRingFile: cannot sign ${project.name}")
                    }
                } else {
                    println("Missing signing.password: cannot sign ${project.name}")
                }
            } else {
                println("Missing signing.keyId: cannot sign ${project.name}")
            }
        }
    }
}

fun configurePublishingAndSigningIfPossible() {
    if (!configuredPublishing) {
        configurePublishingIfPossible()
    }
    if (!configuredSigning) {
        configureSigningIfPossible()
    }
}

fun PublishingExtension.lazyConfigureMultiplatformPublishing(project: Project) {
    println("Configuring multiplatform publishing")
    publications.withType(MavenPublication::class.java) {
        configureMultiplatformPublishing(project)
    }
    publications.whenObjectAdded {
        (this as? MavenPublication)?.configureMultiplatformPublishing(project)
    }
}

fun MavenPublication.configureMultiplatformPublishing(project: Project) {
    with(pom) {
        name.set("fs-${project.name}")
        description.set("$name version of the Compose Multiplatform FS Ryan library for rendering segmented displays")
        inceptionYear.set("2024")
        url.set("https://github.com/fsryan-org/fs-segmented-display")

        issueManagement {
            url.set("https://github.com/fsryan-org/fs-segmented-display/issues")
            system.set("GitHub Issues")
        }

        licenses {
            license {
                name.set("The Apache Software License, Version 2.0")
                url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("repo")
            }
        }

        developers {
            developer {
                id.set("ryan")
                name.set("Ryan Scott")
                email.set("ryan@fsryan.com")
                organization.set("FS Ryan")
                organizationUrl.set("https://www.fsryan.com")
            }
        }

        scm {
            url.set("https://github.com/fsryan-org/fs-segmented-display.git")
            developerConnection.set("scm:git:git@github.com:fsryan-org/fs-segmented-display.git")
        }
    }

    if (name != "androidRelease") {
        artifact(project.tasks.withType<Jar>().first { it.name == "dokkaHtmlJar" })
    }
}

fun configureReleaseTask() {
    println("Configuring release task")
    val releaseTask = tasks.create(name = "release") {
        group = "Releasing"
        description = "Release all multiplatform targets to Maven repository"
        dependsOn(tasks.withType<PublishToMavenRepository>().toTypedArray())
        dependsOn("publishAndroidReleasePublicationToMavenRepository")
    }
    project.afterEvaluate {
        tasks.findByName("publishAndroidReleasePublicationToMavenCentralRepository")?.let {
            println("setting release task dependent upon publishAndroidReleasePublicationToMavenCentralRepository")
            releaseTask.dependsOn(it)
        } ?: println("could not find publishAndroidReleasePublicationToMavenCentralRepository task")
    }
    configureMultiplatformPublishingIfPossible(publishingExtension = null)
}

plugins.findPlugin("org.jetbrains.kotlin.multiplatform")?.let {
    println("Found KotlinMultiplatformPluginWrapper plugin")
    configureReleaseTask()
} ?: plugins.whenPluginAdded {
    if (this is KotlinMultiplatformPluginWrapper) {
        println("Found KotlinMultiplatformPluginWrapper plugin")
        configureReleaseTask()
    }
}

plugins.findPlugin(SigningPlugin::class)?.let {
    plugins.findPlugin(PublishingPlugin::class)?.let {
        println("Found signing plugin and publishing plugin")
        configurePublishingAndSigningIfPossible()
    }
} ?: plugins.whenPluginAdded {
    if (this is SigningPlugin) {
        println("Found signing plugin")
        plugins.findPlugin(PublishingPlugin::class)?.let {
            configurePublishingAndSigningIfPossible()
        }
    }
    if (this is PublishingPlugin) {
        println("Found publishing plugin")
        plugins.findPlugin(SigningPlugin::class)?.let {
            configurePublishingAndSigningIfPossible()
        }
    }
}