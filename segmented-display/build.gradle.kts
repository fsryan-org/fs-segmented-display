import fsryan.shouldConfigureAndroid
import fsryan.shouldConfigureIOS
import fsryan.shouldConfigureJvm
import fsryan.shouldConfigureWASM
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
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
//            commonWebpackConfig {
//                outputFileName = "fs-segmented-display.js"
//                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
//                    static = (static ?: mutableListOf()).apply {
//                        // Serve sources to debug inside browser
//                        add(project.projectDir.path)
//                    }
//                }
//            }
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
        jvm("desktop")
        jvmToolchain(17)
    }

    if (shouldConfigureIOS()) {
        iosArm64()
        iosSimulatorArm64()
        iosX64()
    }

    
    sourceSets {
        val desktopMain by getting

        // TODO: switch compose preview to debug only.
        androidMain.dependencies {
//            implementation(libs.androidx.activity.compose)
        }

        maybeCreate("androidDebug").apply {
            dependencies {
                implementation(compose.preview)
            }
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
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

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.fsryan.ui"
            packageVersion = "1.0.0"
        }
    }
}
