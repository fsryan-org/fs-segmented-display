import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.dokka)
    `maven-publish`
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "fs-segmented-display"
        browser()
        binaries.library()
    }

    androidTarget {
        publishLibraryVariants("release")

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    jvm()

    iosArm64()
    iosSimulatorArm64()
    iosX64()

    sourceSets {
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = false
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

tasks.withType<DokkaTask> {
    val dokkaBaseConfiguration = buildString {
        append("{\"customAssets\": [\"")
        append(rootProject.file("docs/images/readme_headline.png"))
        append("\"],")
        append("\"customStyleSheets\": [],")
        append("\"footerMessage\": \"(c) 2024 FS Ryan Software\"")
        append("}")
    }
    pluginsMapConfiguration.set(
        mapOf("org.jetbrains.dokka.base.DokkaBase" to dokkaBaseConfiguration)
    )
    dokkaSourceSets.configureEach {
        reportUndocumented.set(true)
        includes.from("MODULE.md")
    }
}

// A function that will configure the maven publishing for a publication
fun MavenPublication.configureMultiplatformPublishing(project: Project) {
    val publicationName = name
    with(pom) {
        description.set("$publicationName target of the Compose Multiplatform FS Ryan library for rendering segmented displays")
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

publishing {
    publications.withType(MavenPublication::class.java) {
        configureMultiplatformPublishing(project)
    }
    publications.whenObjectAdded {
        (this as? MavenPublication)?.configureMultiplatformPublishing(project)
    }
}