package com.fsryan.ui.segments

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp

/**
 * A composable function that draws a single line of segmented characters using
 * the [drawRect7SegmentChar] function.
 *
 * @param modifier the [Modifier] passed to the [SingleLineSegmentedDisplay]
 * @param text the [String] whose characters should be drawn
 * @param shearPct serves to transform the x-axis as though it is skewed to the
 * right/left as a percentage of the height. Thus, a value of 1 will skew the
 * output to the right as much as the view is tall. A value of -1 will do the
 * same, but will skew to the left instead of the right.
 * @param paddingValues the [PaddingValues] to apply to each character drawn
 * @param topAreaPercentage the percentage of the height of the character that
 * should be considered the top area
 * @param thicknessMultiplier a multiplier to apply to the thickness of the
 * segments
 * @param gapSizeMultiplier a multiplier to apply to the size of the gaps
 * between segments
 * @param activatedColor the [Color] to use for activated segments
 * @param deactivatedColor the [Color] to use for deactivated segments
 * @param debuggingEnabled whether or not to draw debugging information
 * @param charToActivatedSegments a function that returns the activated segments
 * for a given character.
 *
 * @see transformCharToActiveSegments for a diagram of the segment indices.
 * @see Rect7SegmentDisplay for a rectangular version of a segmented display
 * @author fsryan
 */
@Composable
fun Rect7SegmentDisplay(
    modifier: Modifier = Modifier,
    text: String,
    shearPct: Float = 0F,
    paddingValues: PaddingValues = PaddingValues(4.dp),
    topAreaPercentage: Float = 0.495F,
    thicknessMultiplier: Float = 1F,
    gapSizeMultiplier: Float = 1F,
    activatedColor: Color = Color.Black,
    deactivatedColor: Color = activatedColor.copy(alpha = 0.05F),
    debuggingEnabled: Boolean = false,
    charToActivatedSegments: (Char) -> Int = ::transformCharToActiveSegments
) {
    val density = LocalDensity.current.density
    val layoutDirection = LocalLayoutDirection.current
    val topLeftPadding = Offset(
        x = paddingValues.calculateLeftPadding(layoutDirection).value * density,
        y = paddingValues.calculateTopPadding().value * density
    )
    val bottomRightPadding = Offset(
        x = paddingValues.calculateRightPadding(layoutDirection).value * density,
        y = paddingValues.calculateBottomPadding().value * density
    )
    SingleLineSegmentedDisplay(
        modifier = modifier,
        text = text,
        shearPct = shearPct
    ) { _, char, origin, charWidth, charHeight ->
        drawRect7SegmentChar(
            activatedSegments = charToActivatedSegments(char),
            origin = origin,
            width = charWidth,
            height = charHeight,
            gapSizeMultiplier = gapSizeMultiplier,
            topLeftPadding = topLeftPadding,
            topAreaPercentage = topAreaPercentage,
            bottomRightPadding = bottomRightPadding,
            thicknessMultiplier = thicknessMultiplier,
            activatedColor = activatedColor,
            deactivatedColor = deactivatedColor,
            debuggingEnabled = debuggingEnabled
        )
    }
}

/**
 * Function responsible for drawing a 7-segment character on the canvas. This
 * function is intended to be called called within the `renderCharOnCanvas`
 * function that is an argument passed to [SingleLineSegmentedDisplay]. The
 * segments drawn by this this function are only rectangular.
 *
 * > *Note*:
 * > The thickness of the segments is related to the height of the character.
 * > If the `thicknessMultiplier` is `1F`, then the thickness will be roughly
 * > 12% of the drawable height (the height of the character minus the top and
 * > bottom padding)
 *
 * > *Note*:
 * > The gap size is also related to the height of the character via being
 * > related to the thickness. If `gapSizeMultiplier` is `1F`, then the gap
 * > size will be 10% of the thickness.
 *
 * @param origin the [Offset] to apply to each point
 * @param activatedSegments an integer describing the segments that should be
 * activated.
 * @param width the width of the character
 * @param height the height of the character
 * @param topLeftPadding the padding to apply to the top and left sides of the
 * character
 * @param bottomRightPadding the padding to apply to the right and bottom sides
 * of the character
 * @param topAreaPercentage the percentage of the height of the character that
 * should be taken by the top part of the character
 * @param thicknessMultiplier a multiplier to apply to the thickness of the
 * segments
 * @param gapSizeMultiplier a multiplier to apply to the size of the gaps
 * between segments
 * @param activatedColor the [Color] to use for activated segments
 * @param deactivatedColor the [Color] to use for deactivated segments
 * @param debuggingEnabled whether or not to draw debugging information
 *
 * @see symmetricEvenAngledSegmentEnds for a function that returns
 * [AngledSegmentEnds.EVEN] in all cases
 * @see createSymmetricAngled7SegmentEndsFun for a function that returns
 * a function that styles the segments symmetrically
 * @see createAsymmetricAngled7SegmentEndsFun for a function that returns
 * a function that styles the segments asymmetrically
 */
fun DrawScope.drawRect7SegmentChar(
    origin: Offset,
    activatedSegments: Int,
    width: Float,
    height: Float,
    topLeftPadding: Offset,
    bottomRightPadding: Offset,
    topAreaPercentage: Float,
    thicknessMultiplier: Float,
    gapSizeMultiplier: Float,
    activatedColor: Color,
    deactivatedColor: Color = activatedColor.copy(alpha = 0.05F),
    debuggingEnabled: Boolean = true
) {
    // Here we define some important values that serve as anchor points and
    // sizes that can be referenced to draw correctly
    val leftmostX = origin.x + topLeftPadding.x
    val topY = origin.y + topLeftPadding.y
    val drawableWidth = width - topLeftPadding.x - bottomRightPadding.x
    val drawableHeight = height - topLeftPadding.y - bottomRightPadding.y
    val centerX = leftmostX + drawableWidth / 2
    val centerY = topY + drawableHeight * topAreaPercentage

    val configuredThickness = 29F / 240.37F * drawableHeight * thicknessMultiplier
    val gapSize = configuredThickness / 10 * gapSizeMultiplier
    val halfGapSize = gapSize / 2
    val actualThickness = configuredThickness - halfGapSize

    val horizontalSegmentLength = drawableWidth - gapSize - 2 * actualThickness
    val horizonalSegmentStartX = leftmostX + actualThickness + halfGapSize

    val topVerticalSegmentStartY = topY + actualThickness + halfGapSize
    val topVerticalSegmentEndY = centerY - actualThickness / 2 - halfGapSize
    val topVerticalSegmentHeight = topVerticalSegmentEndY - topVerticalSegmentStartY
    val bottomVerticalSegmentStartY = centerY + actualThickness / 2 + halfGapSize
    val bottomVerticalSegmentEndY = topY + drawableHeight - actualThickness - halfGapSize
    val bottomVerticalSegmentHeight = bottomVerticalSegmentEndY - bottomVerticalSegmentStartY
    val rightVerticalSegmentStartX = leftmostX + drawableWidth - actualThickness

    val activatedBrush = SolidColor(activatedColor)
    val deactivatedBrush = SolidColor(deactivatedColor)
    // top horizontal segment
    drawRect(
        brush = if (activatedSegments and 0b1 != 0) activatedBrush else deactivatedBrush,
        topLeft = Offset(x = horizonalSegmentStartX, y = topY),
        size = Size(width = horizontalSegmentLength, height = actualThickness)
    )
    // middle horizontal segment
    drawRect(
        brush = if (activatedSegments and 0b1000 != 0) activatedBrush else deactivatedBrush,
        topLeft = Offset(x = horizonalSegmentStartX, y = centerY - actualThickness / 2),
        size = Size(width = horizontalSegmentLength, height = actualThickness)
    )
    // bottom horizontal segment
    drawRect(
        brush = if (activatedSegments and 0b1000000 != 0) activatedBrush else deactivatedBrush,
        topLeft = Offset(x = horizonalSegmentStartX, y = topY + drawableHeight - actualThickness),
        size = Size(width = horizontalSegmentLength, height = actualThickness)
    )
    // top-left vertical segment
    drawRect(
        brush = SolidColor(if (activatedSegments and 0b10 != 0) activatedColor else deactivatedColor),
        topLeft = Offset(x = leftmostX, y = topVerticalSegmentStartY),
        size = Size(width = actualThickness, height = topVerticalSegmentHeight)
    )
    // top-right vertical segment
    drawRect(
        brush = SolidColor(if (activatedSegments and 0b100 != 0) activatedColor else deactivatedColor),
        topLeft = Offset(x = rightVerticalSegmentStartX, y = topVerticalSegmentStartY),
        size = Size(width = actualThickness, height = topVerticalSegmentHeight)
    )
    // bottom-left vertical segment
    drawRect(
        brush = SolidColor(if (activatedSegments and 0b10000 != 0) activatedColor else deactivatedColor),
        topLeft = Offset(x = leftmostX, y = bottomVerticalSegmentStartY),
        size = Size(width = actualThickness, height = bottomVerticalSegmentHeight)
    )
    // bottom-right vertical segment
    drawRect(
        brush = SolidColor(if (activatedSegments and 0b100000 != 0) activatedColor else deactivatedColor),
        topLeft = Offset(x = rightVerticalSegmentStartX, y = bottomVerticalSegmentStartY),
        size = Size(width = actualThickness, height = bottomVerticalSegmentHeight)
    )

    // debugging
    if (debuggingEnabled) {
        // Allocated Area
        drawRect(
            brush = SolidColor(Color.Red),
            topLeft = Offset(x = 0F, y = 0F),
            size = Size(width = width, height = height),
            style = Stroke(width = Stroke.HairlineWidth)
        )
        // Drawable Area
        drawRect(
            brush = SolidColor(Color.Red),
            topLeft = Offset(x = leftmostX, y = topY),
            size = Size(width = drawableWidth, height = drawableHeight),
            style = Stroke(width = Stroke.HairlineWidth)
        )
        // Vertical center line
        drawLine(
            brush = SolidColor(Color.Blue),
            start = Offset(x = centerX, y = topY),
            end = Offset(x = centerX, y = topY + drawableHeight)
        )
        // Horizontal Center line of the top segment
        drawLine(
            brush = SolidColor(Color.Green),
            start = Offset(x = leftmostX, y = centerY),
            end = Offset(x = leftmostX + drawableWidth, y = centerY),
        )
        // Center line of the top area
        val topAreaCenterLine = (topY + centerY + configuredThickness / 2) / 2
        drawLine(
            brush = SolidColor(Color.Green),
            start = Offset(x = leftmostX, y = topAreaCenterLine),
            end = Offset(x = leftmostX + drawableWidth, y = topAreaCenterLine),
        )
        // Center line of the bottom area
        val bottomAreaCenterLine = (centerY + topY + drawableHeight - configuredThickness / 2) / 2
        drawLine(
            brush = SolidColor(Color.Green),
            start = Offset(x = leftmostX, y = bottomAreaCenterLine),
            end = Offset(x = leftmostX + drawableWidth, y = bottomAreaCenterLine),
        )
    }
}