package com.fsryan.ui.segments

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Display the input text via characters of seven hexagonal segments, supposing
 * that no three of the calculated points of a segment lie on the same line
 * (which is possible and even desirable in some cases).
 *
 * @param modifier The [Modifier] applied to the [Canvas] on which the
 * 7-segment characters are drawn
 * @param text the [String] of characters to draw via the 7-segments
 * @param thicknessMultiplier A multiplier applied to the thickness of the
 * segments. The thickness of each segment has a large effect on the ultimate
 * look of the character.
 * @param gapSize The size (in [Dp]) of the gaps between the segments of each
 * character. This, too, plays a large role in the look of the characters.
 * @param shearPct This acts like a multiplier of the width of a character that
 * results in the bottom of the character being shifted to the left/right and
 * the top of the character being shifted to the right/left when the value is
 * positive/negative. When 0, the character will be straight and vertical.
 * @param paddingValues The amount of "padding" applied to each character. When
 * [shearPct] is 0, this does act like padding, and nothing will be drawn to
 * the [Canvas] in this area around each character. However, when [shearPct] is
 * nonzero, you have to look at it as though the "padding" is also getting
 * sheared.
 * @param activatedColor The [Color] of the activated segments
 * @param deactivatedColor the [Color] of the deactivated segments. you will
 * most likely want to make this the same as the activated color, but with some
 * alpha applied. (defaults to alpha of `0.05F`)
 * @param hexagonalSegmentParams a function that returns
 * [HexagonalSegmentParams] that define how the tips of each segment are
 * rendered
 * @param charToActivatedSegments a function which defines the activated
 * segments for a char passed in.
 */
@Composable
fun Hexagonal7SegmentDisplay(
    modifier: Modifier = Modifier,
    text: String,
    thicknessMultiplier: Float = 1F,
    topHeightPercentage: Float = 105F / 212,
    gapSizeMultiplier: Float = 1F,
    shearPct: Float = 0F,
    paddingValues: PaddingValues = PaddingValues(all = 4.dp),
    activatedColor: Color = Color.Black,
    deactivatedColor: Color = activatedColor.copy(alpha = 0.05F),
    debuggingEnabled: Boolean = false,
    hexagonalSegmentParams: (index: Int, leftTop: Boolean) -> HexagonalSegmentParams = { _, _ ->
        HexagonalSegmentParams.EVEN
    },
    charToActivatedSegments: (Char) -> Int = ::translateHexToActiveSegments
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
    SingleLineSegmentedDisplay(modifier = modifier, text = text, shearPct = shearPct) { char, origin, charWidth, charHeight ->
        drawHex7SegmentChar(
            activatedSegments = charToActivatedSegments(char),
            origin = origin,
            width = charWidth,
            height = charHeight,
            gapSizeMultiplier = gapSizeMultiplier,
            topLeftPadding = topLeftPadding,
            bottomRightPadding = bottomRightPadding,
            thicknessMultiplier = thicknessMultiplier,
            topHeightPercentage = topHeightPercentage,
            activatedColor = activatedColor,
            deactivatedColor = deactivatedColor,
            debuggingEnabled = debuggingEnabled,
            hexagonalSegmentParams = hexagonalSegmentParams
        )
    }
}

fun DrawScope.drawHex7SegmentChar(
    activatedSegments: Int,
    origin: Offset,
    width: Float,
    height: Float,
    topLeftPadding: Offset,
    bottomRightPadding: Offset,
    topHeightPercentage: Float = 105F / 212,
    thicknessMultiplier: Float = 1F,
    gapSizeMultiplier: Float = 1F,
    activatedColor: Color = Color.Black,
    deactivatedColor: Color = activatedColor.copy(alpha = 0.05F),
    debuggingEnabled: Boolean = false,
    hexagonalSegmentParams: (index: Int, leftTop: Boolean) -> HexagonalSegmentParams = { _, _ ->
        HexagonalSegmentParams.EVEN
    }
) {
    // Important sizes
    val drawableWidth = width - topLeftPadding.x - bottomRightPadding.x
    val drawableHeight = height - topLeftPadding.y - bottomRightPadding.y
    val topAreaHeight = drawableHeight * topHeightPercentage
    val bottomAreaHeight = drawableHeight - topAreaHeight
    
    // thickness is calculated pre-gap size. The gap size must eat into the
    // resulting size of each segment, but it does so differently per segment
    val configuredThickness = 29F / 240.37F * drawableHeight * thicknessMultiplier
    val gapSize = gapSizeMultiplier * configuredThickness / 10
    val halfGapSize = gapSize / 2
    val actualThickness = configuredThickness - halfGapSize // <-- the actual thickness of each segment

    // anchor points
    val leftmostX = origin.x + topLeftPadding.x
    val centerX = leftmostX + drawableWidth / 2
    val rightmostX = origin.x + width - bottomRightPadding.x
    val topY = origin.y + topLeftPadding.y
    val topAreaCenterY = topY + topAreaHeight / 2
    val bottomY = origin.y + height - bottomRightPadding.y
    val bottomAreaCenterY = bottomY - bottomAreaHeight / 2

    // sizes without gaps
    val topSegmentHeight = topAreaHeight - configuredThickness * 1.5F  // <-- the full top segment plus half of the middle segment
    val halfTopSegmentHeight = topSegmentHeight / 2
    val bottomSegmentHeight = bottomAreaHeight - configuredThickness * 1.5F  // <-- the full bottom segment plus half of the middle segment
    val halfBottomSegmentHeight = bottomSegmentHeight / 2
    val configuredHorizontalSegmentWidth = drawableWidth - 2 * configuredThickness

    // Sizes with gaps
    val actualHorizontalSegmentWidth = configuredHorizontalSegmentWidth - gapSize
    val halfActualHorizontalSegmentWidth = actualHorizontalSegmentWidth / 2

    // Vertical anchors
    val topAreaSegmentCenterY = topAreaCenterY + configuredThickness / 4
    val bottomAreaSegmentCenterY = bottomAreaCenterY - configuredThickness / 4

    // Draw top horizontal segment
    var leftTopParams = hexagonalSegmentParams(0, true)
    var rightBottomParams = hexagonalSegmentParams(0, false)
    drawPath(
        path = createHorizontalSegmentPath(
            leftTopX = centerX - halfActualHorizontalSegmentWidth * leftTopParams.outerLengthPct,
            leftTopY = topY,
            centerX = centerX,
            thickness = actualThickness,
            halfSegmentWidth = halfActualHorizontalSegmentWidth,
            leftParams = leftTopParams,
            rightParams = rightBottomParams,
            invertExtThicknessVertPct = false
        ),
        color = if (activatedSegments and 0b1 == 1) activatedColor else deactivatedColor
    )

    // Draw top-left vertical segment
    leftTopParams = hexagonalSegmentParams(1, true)
    rightBottomParams = hexagonalSegmentParams(1, false)
    drawPath(
        path = createVerticalSegmentPath(
            leftTopX = leftmostX,
            leftTopY = topAreaSegmentCenterY - halfTopSegmentHeight * leftTopParams.outerLengthPct,
            thickness = actualThickness,
            centerY = topAreaSegmentCenterY,
            halfSegmentHeight = halfTopSegmentHeight,
            topParams = leftTopParams,
            bottomParams = rightBottomParams,
            leftIsInner = false
        ),
        color = if (activatedSegments and 0b10 != 0) activatedColor else deactivatedColor
    )

    // Draw top-right vertical segment
    leftTopParams = hexagonalSegmentParams(2, true)
    rightBottomParams = hexagonalSegmentParams(2, false)
    drawPath(
        path = createVerticalSegmentPath(
            leftTopX = rightmostX - actualThickness,
            leftTopY = topAreaSegmentCenterY - halfTopSegmentHeight * leftTopParams.innerLengthPct,
            thickness = actualThickness,
            centerY = topAreaSegmentCenterY,
            halfSegmentHeight = halfTopSegmentHeight,
            topParams = leftTopParams,
            bottomParams = rightBottomParams,
            leftIsInner = true
        ),
        color = if (activatedSegments and 0b100 != 0) activatedColor else deactivatedColor
    )

    // Draw middle horizontal segment
    leftTopParams = hexagonalSegmentParams(3, true)
    rightBottomParams = hexagonalSegmentParams(3, false)
    drawPath(
        path = createHorizontalSegmentPath(
            leftTopX = centerX - halfActualHorizontalSegmentWidth * leftTopParams.outerLengthPct,
            leftTopY = topY + topAreaHeight - actualThickness / 2,
            centerX = centerX,
            thickness = actualThickness,
            halfSegmentWidth = halfActualHorizontalSegmentWidth,
            leftParams = leftTopParams,
            rightParams = rightBottomParams,
            invertExtThicknessVertPct = false
        ),
        color = if (activatedSegments and 0b1000 != 0) activatedColor else deactivatedColor
    )

    // Draw bottom-left vertical segment
    leftTopParams = hexagonalSegmentParams(4, true)
    rightBottomParams = hexagonalSegmentParams(4, false)
    drawPath(
        path = createVerticalSegmentPath(
            leftTopX = leftmostX,
            leftTopY = bottomAreaSegmentCenterY - halfBottomSegmentHeight * leftTopParams.outerLengthPct,
            thickness = actualThickness,
            centerY = bottomAreaSegmentCenterY,
            halfSegmentHeight = halfBottomSegmentHeight,
            topParams = leftTopParams,
            bottomParams = rightBottomParams,
            leftIsInner = false
        ),
        color = if (activatedSegments and 0b10000 != 0) activatedColor else deactivatedColor
    )

    // Draw bottom-right vertical segment
    leftTopParams = hexagonalSegmentParams(5, true)
    rightBottomParams = hexagonalSegmentParams(5, false)
    drawPath(
        path = createVerticalSegmentPath(
            leftTopX = rightmostX - actualThickness,
            leftTopY = bottomAreaSegmentCenterY - halfBottomSegmentHeight * leftTopParams.innerLengthPct,
            thickness = actualThickness,
            centerY = bottomAreaSegmentCenterY,
            halfSegmentHeight = halfBottomSegmentHeight,
            topParams = leftTopParams,
            bottomParams = rightBottomParams,
            leftIsInner = true
        ),
        color = if (activatedSegments and 0b100000 != 0) activatedColor else deactivatedColor
    )

    // Draw bottom horizontal segment
    leftTopParams = hexagonalSegmentParams(6, true)
    rightBottomParams = hexagonalSegmentParams(6, false)
    drawPath(
        path = createHorizontalSegmentPath(
            leftTopX = centerX - halfActualHorizontalSegmentWidth * leftTopParams.innerLengthPct,
            leftTopY = bottomY - actualThickness,
            centerX = centerX,
            thickness = actualThickness,
            halfSegmentWidth = halfActualHorizontalSegmentWidth,
            leftParams = leftTopParams,
            rightParams = rightBottomParams,
            invertExtThicknessVertPct = true
        ),
        color = if (activatedSegments and 0b1000000 != 0) activatedColor else deactivatedColor
    )

    // debugging
    if (debuggingEnabled) {
        // Alocated Area
        drawRect(
            brush = SolidColor(Color.Red),
            topLeft = origin,
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
        // Center line of the top segment
        drawLine(
            brush = SolidColor(Color.Green),
            start = Offset(x = leftmostX, y = topAreaSegmentCenterY),
            end = Offset(x = leftmostX + drawableWidth, y = topAreaSegmentCenterY),
        )
        // Center line of the top segment
        drawLine(
            brush = SolidColor(Color.Green),
            start = Offset(x = leftmostX, y = topY + topAreaHeight),
            end = Offset(x = leftmostX + drawableWidth, y = topY + topAreaHeight),
        )
        drawLine(
            brush = SolidColor(Color.Green),
            start = Offset(x = leftmostX, y = bottomAreaSegmentCenterY),
            end = Offset(x = leftmostX + drawableWidth, y = bottomAreaSegmentCenterY),
        )
    }
}

private fun createHorizontalSegmentPath(
    leftTopX: Float,
    leftTopY: Float,
    centerX: Float,
    thickness: Float,
    halfSegmentWidth: Float,
    leftParams: HexagonalSegmentParams,
    rightParams: HexagonalSegmentParams,
    invertExtThicknessVertPct: Boolean
): Path {
    val rectLeftX = centerX - halfSegmentWidth
    val rectRightX = centerX + halfSegmentWidth

    val leftExtThicknessVertPct = if (invertExtThicknessVertPct) 1 - leftParams.extThicknessVertPct else leftParams.extThicknessVertPct
    val rightExtThicknessVertPct = if (invertExtThicknessVertPct) 1 - rightParams.extThicknessVertPct else rightParams.extThicknessVertPct

    val leftMiddleX = rectLeftX - thickness * leftParams.extThicknessHorizPct
    val leftMiddleY = leftTopY + thickness * leftExtThicknessVertPct
    val leftBottomX = centerX - halfSegmentWidth * leftParams.innerLengthPct
    val leftBottomY = leftTopY + thickness
    val rightTopX = centerX + halfSegmentWidth * rightParams.outerLengthPct
    val rightTopY = leftTopY
    val rightMiddleX = rectRightX + thickness * rightParams.extThicknessHorizPct
    val rightMiddleY = rightTopY + thickness * rightExtThicknessVertPct
    val rightBottomX = centerX + halfSegmentWidth * rightParams.innerLengthPct
    val rightBottomY = leftBottomY
    return Path().apply {
        moveTo(leftMiddleX, leftMiddleY)
        lineTo(leftTopX, leftTopY)
        lineTo(rightTopX, rightTopY)
        lineTo(rightMiddleX, rightMiddleY)
        lineTo(rightBottomX, rightBottomY)
        lineTo(leftBottomX, leftBottomY)
        close()
    }
}

private fun createVerticalSegmentPath(
    leftTopX: Float,
    leftTopY: Float,
    thickness: Float,
    centerY: Float,
    halfSegmentHeight: Float,
    topParams: HexagonalSegmentParams,
    bottomParams: HexagonalSegmentParams,
    leftIsInner: Boolean
): Path {
    val rectTop = centerY - halfSegmentHeight
    val rectBottom = centerY + halfSegmentHeight

    val topExtThicknessVertPct = if (leftIsInner) 1 - topParams.extThicknessVertPct else topParams.extThicknessVertPct
    val bottomExtThicknessVertPct = if (leftIsInner) 1 - bottomParams.extThicknessVertPct else bottomParams.extThicknessVertPct

    val topMiddleX = leftTopX + thickness * topExtThicknessVertPct
    val topMiddleY = rectTop - thickness * topParams.extThicknessHorizPct
    val rightTopX = leftTopX + thickness
    val rightTopY = centerY - halfSegmentHeight * (if (leftIsInner) topParams.outerLengthPct else topParams.innerLengthPct)
    val rightBottomX = rightTopX
    val rightBottomY = centerY + halfSegmentHeight * (if (leftIsInner) bottomParams.outerLengthPct else bottomParams.innerLengthPct)
    val bottomMiddleX = leftTopX + thickness * bottomExtThicknessVertPct
    val bottomMiddleY = rectBottom + thickness * bottomParams.extThicknessHorizPct
    val leftBottomX = leftTopX
    val leftBottomY = centerY + halfSegmentHeight * (if (leftIsInner) bottomParams.innerLengthPct else bottomParams.outerLengthPct)
    return Path().apply {
        moveTo(bottomMiddleX, bottomMiddleY)
        lineTo(leftBottomX, leftBottomY)
        lineTo(leftTopX, leftTopY)
        lineTo(topMiddleX, topMiddleY)
        lineTo(rightTopX, rightTopY)
        lineTo(rightBottomX, rightBottomY)
        close()
    }
}