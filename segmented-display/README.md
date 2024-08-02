# segmented-display

This library provides the following Composable functions that can be used to create a segmented display:
* [Rect7SegmentDisplay.kt](src/commonMain/kotlin/com/fsryan/ui/segments/Rect7SegmentDisplay.kt)
* [Classic7SegmentDisplay](src/commonMain/kotlin/com/fsryan/ui/segments/Classic7SegmentDisplay.kt)

Of these, the one you'll most likely want to use is [Classic7SegmentDisplay](src/commonMain/kotlin/com/fsryan/ui/segments/Classic7SegmentDisplay.kt) because the rectangular version is visually unappealing.

## Usage

The most basic usage of the `Classic7SegmentDisplay` composable function is as follows:

```kotlin
Classic7SegmentDisplay(
    modifier = Modifier.fillMaxWidth().aspectRatio(0.5F),
    text = "01234567"
)
```

However, you can also style the display in a number of ways:

```kotlin
Classic7SegmentDisplay(
    modifier = Modifier.fillMaxWidth().aspectRatio(0.5F),
    text = "01234567",
    shearPct = 0.18F,
    topAreaPercentage = 0.45F,
    thicknessMultiplier = 0.75F,
    gapSizeMultiplier = 1.25F,
    activatedColor = Color.Red,
    debuggingEnabled = true,
    angledSegmentEndsOf = ::createAsymmetricAngled7SegmentEndsFun
)
```

## Developing your own segmented display

If you want to develop your own segmented display, you can use the more-generic [SingleLineSegmentedDisplay](src/commonMain/kotlin/com/fsryan/ui/segments/SingleLineSegmentedDisplay.kt) composable function as a starting point and then implement your own `renderCharOnCanvas` function. As for how to implement your `renderCharOnCanvas` function, you could use the [drawRect7SegmentChar](src/commonMain/kotlin/com/fsryan/ui/segments/Rect7SegmentDisplay.kt#L57) and [drawClassic7SegmentChar](src/commonMain/kotlin/com/fsryan/ui/segments/Classic7SegmentDisplay.kt#L61) functions as examples.

Then you can pass your new function to the `SingleLineSegmentedDisplay` composable function as the `renderCharOnCanvas` parameter as below:

```kotlin
SingleLineSegmentedDisplay(
    modifier = Modifier.fillMaxWidth().aspectRatio(0.5F),
    text = "01234567",
    shearPct = 0.15F,
    renderCharOnCanvas = { idx: Int, char: Char, offset: Offset, charWidth: Float, charHeight: Float ->
        // your implementation that draws a vertical segmented display character here
        
    }
)
```