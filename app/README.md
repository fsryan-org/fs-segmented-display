# FS Segmented Display Sample App

This app is intended to enable basic manipulation of some of the properties you can style.

## Running

The app can be run:
* in a browser: `./gradlew :app:wasmJsBrowserRun`
* In a desktop window: `./gradlew :app:desktopRun -DmainClass=MainKt`
* On an Android device: `./gradlew :app:installDebug && adb shell am start -n com.fsryan.ui.segmenteddisplay/.MainActivity`
* On an iOS device: it's just best to use Android Studio plus the [Kotlin Multiplatform plugin](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform)

## Example usage

![Example run on iOS](../docs/images/app_use_example.gif)

More to come on this.