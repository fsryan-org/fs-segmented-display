import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.dokka.DokkaConfiguration.Visibility
import org.jetbrains.dokka.gradle.DokkaTask
//import org.jetbrains.dokka.base.DokkaBase
//import org.jetbrains.dokka.base.DokkaBaseConfiguration
import org.jetbrains.dokka.Platform
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.dokka)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "fs-segmented-display"
        browser()
        binaries.library()
    }
    
    androidTarget {
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

tasks.withType<DokkaTask> {
    moduleName.set("fs-segmented-display")

    val dokkaBaseConfiguration = """
    {
      "customAssets": ["${rootProject.file("docs/images/readme_headline.png")}"],
      "customStyleSheets": [],
      "footerMessage": "(c) 2024 FS Ryan Software"
    }
    """
    pluginsMapConfiguration.set(
        mapOf("org.jetbrains.dokka.base.DokkaBase" to dokkaBaseConfiguration)
    )
    dokkaSourceSets.configureEach {
        reportUndocumented.set(true)
        includes.from("MODULE.md")
    }
}