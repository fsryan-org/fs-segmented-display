import fsryan.shouldConfigureAndroid
import fsryan.shouldConfigureIOS
import fsryan.shouldConfigureJvm
import fsryan.shouldConfigureWASM
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    id("maven-publish")
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
