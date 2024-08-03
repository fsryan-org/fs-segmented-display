package com.fsryan.ui.segments

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
import androidx.compose.ui.unit.dp

/**
 * A composable function that draws a single line of segmented characters using
 * the [drawClassic7SegmentChar] function.
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
 * @param angledSegmentEndsOf a function that returns the [AngledSegmentEnds] for
 * a given segment index.
 * @param charToActivatedSegments a function that returns the activated segments
 * for a given character.
 *
 * @see transformCharToActiveSegments for a diagram of the segment indices.
 * @see Rect7SegmentDisplay for a rectangular version of a segmented display
 * @author fsryan
 */
@Composable
fun Classic7SegmentDisplay(
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
    angledSegmentEndsOf: (Int) -> AngledSegmentEnds = ::symmetricEvenAngledSegmentEnds,
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
        drawClassic7SegmentChar(
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
            debuggingEnabled = debuggingEnabled,
            angledSegmentEndsOf = angledSegmentEndsOf
        )
    }
}

/**
 * Function responsible for drawing a 7-segment character on the canvas. This
 * function is intended to be called called within the `renderCharOnCanvas`
 * function that is an argument passed to [SingleLineSegmentedDisplay]. The
 * segments drawn by this function are angled, and their ends are defined by
 * the [AngledSegmentEnds] objects returned by the [angledSegmentEndsOf]
 * function.
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
 * @param angledSegmentEndsOf a function that returns the [AngledSegmentEnds] for
 * a given segment index.
 *
 * @see symmetricEvenAngledSegmentEnds for a function that returns
 * [AngledSegmentEnds.EVEN] in all cases
 * @see createSymmetricAngled7SegmentEndsFun for a function that returns
 * a function that styles the segments symmetrically
 * @see createAsymmetricAngled7SegmentEndsFun for a function that returns
 * a function that styles the segments asymmetrically
 * @author fsryan
 */
fun DrawScope.drawClassic7SegmentChar(
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
    debuggingEnabled: Boolean = true,
    angledSegmentEndsOf: (Int) -> AngledSegmentEnds = ::symmetricEvenAngledSegmentEnds
) {
    // Here we define some important values that serve as anchor points and
    // sizes that can be referenced to draw correctly
    val drawableWidth = width - topLeftPadding.x - bottomRightPadding.x
    val leftmostX = origin.x + topLeftPadding.x
    val rightmostX = leftmostX + drawableWidth
    val topY = origin.y + topLeftPadding.y
    val drawableHeight = height - topLeftPadding.y - bottomRightPadding.y
    val centerX = leftmostX + drawableWidth / 2
    val centerY = topY + drawableHeight * topAreaPercentage

    val thickness = 29F / 240.37F * drawableHeight * thicknessMultiplier
    val gapSize = thickness / 10 * gapSizeMultiplier

    val baseHorizontalSegmentLeftX = leftmostX + thickness
    val baseHorizontalSegmentRightX = baseHorizontalSegmentLeftX + drawableWidth - 2 * thickness

    val middleHorizontalSegmentTopY = centerY - thickness / 2
    val middleHorizontalSegmentBottomY = centerY + thickness / 2

    val bottomHorizontalSegmentBottomY = topY + drawableHeight
    val bottomHorizontalSegmentTopY = bottomHorizontalSegmentBottomY - thickness

    val baseTopVerticalSegmentTopY = topY + thickness
    val baseTopVerticalSegmentBottomY = middleHorizontalSegmentTopY
    val baseBottomVerticalSegmentTopY = middleHorizontalSegmentBottomY
    val baseBottomVerticalSegmentBottomY = topY + drawableHeight - thickness

    val leftVerticalSegmentRightX = baseHorizontalSegmentLeftX
    val rightVerticalSegmentLeftX = rightmostX - thickness
    val rightVerticalSegmentRightX = rightmostX

    // Outer segment dimensions
    val topHorizontalEnds = angledSegmentEndsOf(0)
    val topHorizontalOuterEdgeLeftX = topHorizontalEnds.horizOuterEdgeLeftX(baseX = baseHorizontalSegmentLeftX, thickness)
    val topHorizontalOuterEdgeRightX = topHorizontalEnds.horizOuterEdgeRightX(baseX = baseHorizontalSegmentRightX, thickness)

    val topLeftVerticalEnds = angledSegmentEndsOf(1)
    val topLeftVerticalOuterEdgeTopY = topLeftVerticalEnds.leftVertOuterEdgeTopY(baseY = baseTopVerticalSegmentTopY, thickness)
    val topLeftVerticalOuterEdgeBottomY = topLeftVerticalEnds.leftVertOuterEdgeBottomY(baseY = baseTopVerticalSegmentBottomY, thickness)

    val topRightVerticalEnds = angledSegmentEndsOf(2)
    val topRightVerticalOuterEdgeTopY = topRightVerticalEnds.rightVertOuterEdgeTopY(baseY = baseTopVerticalSegmentTopY, thickness)
    val topRightVerticalOuterEdgeBottomY = topRightVerticalEnds.rightVertOuterEdgeBottomY(baseY = baseTopVerticalSegmentBottomY, thickness)

    val middleHorizontalEnds = angledSegmentEndsOf(3)
    val middleHorizontalUpperEdgeLeftX = middleHorizontalEnds.horizOuterEdgeLeftX(baseX = baseHorizontalSegmentLeftX, thickness)
    val middleHorizontalUpperEdgeRightX = middleHorizontalEnds.horizOuterEdgeRightX(baseX = baseHorizontalSegmentRightX, thickness)
    val middleHorizontalLowerEdgeLeftX = middleHorizontalEnds.horizInnerEdgeLeftX(baseX = baseHorizontalSegmentLeftX, thickness)
    val middleHorizontalLowerEdgeRightX = middleHorizontalEnds.horizInnerEdgeRightX(baseX = baseHorizontalSegmentRightX, thickness)

    val bottomLeftVerticalEnds = angledSegmentEndsOf(4)
    val bottomLeftVerticalOuterEdgeTopY = bottomLeftVerticalEnds.leftVertOuterEdgeTopY(baseY = baseBottomVerticalSegmentTopY, thickness)
    val bottomLeftVerticalOuterEdgeBottomY = bottomLeftVerticalEnds.leftVertOuterEdgeBottomY(baseY = baseBottomVerticalSegmentBottomY, thickness)

    val bottomRightEnds = angledSegmentEndsOf(5)
    val bottomRightVerticalOuterEdgeTopY = bottomRightEnds.rightVertOuterEdgeTopY(baseY = baseBottomVerticalSegmentTopY, thickness)
    val bottomRightVerticalOuterEdgeBottomY = bottomRightEnds.rightVertOuterEdgeBottomY(baseY = baseBottomVerticalSegmentBottomY, thickness)
    val bottomRightVerticalInnerEdgeTopY = bottomRightEnds.rightVertInnerEdgeTopY(baseY = baseBottomVerticalSegmentTopY, thickness)

    val bottomHorizontalEnds = angledSegmentEndsOf(6)
    val bottomHorizontalOuterEdgeLeftX = bottomHorizontalEnds.horizOuterEdgeLeftX(baseX = baseHorizontalSegmentLeftX, thickness)
    val bottomHorizontalOuterEdgeRightX = bottomHorizontalEnds.horizOuterEdgeRightX(baseX = baseHorizontalSegmentRightX, thickness)

    // Slopes for intersections
    val topLeftCornerSlope = slope(x1 = leftmostX, y1 = topLeftVerticalOuterEdgeTopY, x2 = topHorizontalOuterEdgeLeftX, y2 = topY)
    val topRightCornerSlope = slope(x1 = topHorizontalOuterEdgeRightX, y1 = topY, x2 = rightVerticalSegmentRightX, y2 = topRightVerticalOuterEdgeTopY)
    val middleRightUpperToLowerSlope = slope(x1 = middleHorizontalUpperEdgeRightX, y1 = middleHorizontalSegmentTopY, x2 = rightmostX, y2 = bottomRightVerticalOuterEdgeTopY)
    val middleRightLowerToUpperSlope = slope(x1 = rightVerticalSegmentLeftX, y1 = bottomRightVerticalInnerEdgeTopY, x2 = rightmostX, y2 = topRightVerticalOuterEdgeBottomY)
    val bottomRightCornerSlope = slope(x1 = bottomHorizontalOuterEdgeRightX, y1 = bottomHorizontalSegmentBottomY, x2 = rightmostX, y2 = bottomRightVerticalOuterEdgeBottomY)
    val bottomLeftCornerSlope = slope(x1 = leftmostX, y1 = bottomLeftVerticalOuterEdgeBottomY, x2 = bottomHorizontalOuterEdgeLeftX, y2 = bottomHorizontalSegmentBottomY)
    val middleLeftUpperToLowerSlope = slope(x1 = leftmostX, y1 = topLeftVerticalOuterEdgeBottomY, x2 = middleHorizontalLowerEdgeLeftX, y2 = middleHorizontalSegmentBottomY)
    val middleLeftLowerToUpperSlope = slope(x1 = leftmostX, y1 = bottomLeftVerticalOuterEdgeTopY, x2 = middleHorizontalUpperEdgeLeftX, y2 = middleHorizontalSegmentTopY)

    // top horizontal segment
    drawPath(
        color = if (activatedSegments and 0b1 != 0) activatedColor else deactivatedColor,
        path = Path().apply {
            val topHorizontalSegmentBottomY = topY + thickness
            val leftGapOffset = topHorizontalEnds.horizLeftGapOffset(gapSize)
            val rightGapOffset = topHorizontalEnds.horizRightGapOffset(gapSize)

            moveTo(x = topHorizontalOuterEdgeLeftX + leftGapOffset, y = topY)
            lineTo(x = topHorizontalOuterEdgeRightX - rightGapOffset, y = topY)

            val remainingRightEndArea = rightmostX - topHorizontalOuterEdgeRightX
            val rightIntermediateX = topHorizontalOuterEdgeRightX + topHorizontalEnds.rightIntersectionPct * remainingRightEndArea
            val rightIntermediateY = topY + topRightCornerSlope * (rightIntermediateX - topHorizontalOuterEdgeRightX)
            lineTo(x = rightIntermediateX - rightGapOffset, y = rightIntermediateY)
            lineTo(x = topHorizontalEnds.horizInnerEdgeRightX(baseX = baseHorizontalSegmentRightX, thickness) - rightGapOffset, y = topHorizontalSegmentBottomY)
            lineTo(x = topHorizontalEnds.horizInnerEdgeLeftX(baseX = baseHorizontalSegmentLeftX, thickness) + leftGapOffset, y = topHorizontalSegmentBottomY)

            val remainingLeftEndArea = topHorizontalOuterEdgeLeftX - leftmostX
            val leftIntermediateX = topHorizontalOuterEdgeLeftX - topHorizontalEnds.leftIntersectionPct * remainingLeftEndArea
            val leftIntermediateY = topY - topLeftCornerSlope * (topHorizontalOuterEdgeLeftX - leftIntermediateX)
            lineTo(x = leftIntermediateX + leftGapOffset, y = leftIntermediateY)
            close()
        }
    )
    // middle horizontal segment
    drawPath(
        color = if (activatedSegments and 0b1000 != 0) activatedColor else deactivatedColor,
        path = Path().apply {
            // This path could possibly be an octagon because of the way we're calculating the intersection points
            val leftGapOffset = middleHorizontalEnds.horizLeftGapOffset(gapSize)
            val rightGapOffset = middleHorizontalEnds.horizRightGapOffset(gapSize)

            moveTo(x = middleHorizontalUpperEdgeLeftX + leftGapOffset, y = middleHorizontalSegmentTopY)
            lineTo(x = middleHorizontalUpperEdgeRightX - rightGapOffset, y = middleHorizontalSegmentTopY)

            val upperRemainingRightEndArea = rightmostX - middleHorizontalUpperEdgeRightX
            val upperRightIntermediateX = middleHorizontalUpperEdgeRightX + middleHorizontalEnds.rightIntersectionPct * upperRemainingRightEndArea
            val upperRightIntermediateY = middleHorizontalSegmentTopY + middleRightUpperToLowerSlope * (upperRightIntermediateX - middleHorizontalUpperEdgeRightX)
            lineTo(x = upperRightIntermediateX - rightGapOffset, y = upperRightIntermediateY)

            val lowerRemainingRightEndArea = rightmostX - middleHorizontalLowerEdgeRightX
            val lowerRightIntermediateX = middleHorizontalLowerEdgeRightX + middleHorizontalEnds.rightIntersectionPct * lowerRemainingRightEndArea
            val lowerRightIntermediateY = middleHorizontalSegmentBottomY + middleRightLowerToUpperSlope * (lowerRightIntermediateX - middleHorizontalLowerEdgeRightX)
            lineTo(x = lowerRightIntermediateX - rightGapOffset, y = lowerRightIntermediateY)
            lineTo(x = middleHorizontalLowerEdgeRightX - rightGapOffset, y = middleHorizontalSegmentBottomY)
            lineTo(x = middleHorizontalLowerEdgeLeftX + leftGapOffset, y = middleHorizontalSegmentBottomY)

            val lowerRemainingLeftEndArea = middleHorizontalLowerEdgeLeftX - leftmostX
            val lowerLeftIntermediateX = middleHorizontalLowerEdgeLeftX - middleHorizontalEnds.leftIntersectionPct * lowerRemainingLeftEndArea
            val lowerLeftIntermediateY = middleHorizontalSegmentBottomY - middleLeftUpperToLowerSlope * (middleHorizontalLowerEdgeLeftX - lowerLeftIntermediateX)
            lineTo(x = lowerLeftIntermediateX + leftGapOffset, y = lowerLeftIntermediateY)

            val upperRemainingLeftEndArea = middleHorizontalUpperEdgeLeftX - leftmostX
            val upperLeftIntermediateX = middleHorizontalUpperEdgeLeftX - middleHorizontalEnds.leftIntersectionPct * upperRemainingLeftEndArea
            val upperLeftIntermediateY = middleHorizontalSegmentTopY + middleLeftLowerToUpperSlope * (upperLeftIntermediateX - middleHorizontalUpperEdgeLeftX)
            lineTo(x = upperLeftIntermediateX + leftGapOffset, y = upperLeftIntermediateY)

            close()
        }
    )
    // bottom horizontal segment
    drawPath(
        color = if (activatedSegments and 0b1000000 != 0) activatedColor else deactivatedColor,
        path = Path().apply {
            val leftGapOffset = bottomHorizontalEnds.horizLeftGapOffset(gapSize)
            val rightGapOffset = bottomHorizontalEnds.horizRightGapOffset(gapSize)

            moveTo(x = bottomHorizontalEnds.horizInnerEdgeLeftX(baseX = baseHorizontalSegmentLeftX, thickness) + leftGapOffset, y = bottomHorizontalSegmentTopY)
            lineTo(x = bottomHorizontalEnds.horizInnerEdgeRightX(baseX = baseHorizontalSegmentRightX, thickness) - rightGapOffset, y = bottomHorizontalSegmentTopY)

            val remainingRightEndArea = rightmostX - bottomHorizontalOuterEdgeRightX
            val rightIntermediateX = bottomHorizontalOuterEdgeRightX + bottomHorizontalEnds.rightIntersectionPct * remainingRightEndArea
            val rightIntermediateY = bottomHorizontalSegmentBottomY + bottomRightCornerSlope * (rightIntermediateX - bottomHorizontalOuterEdgeRightX)
            lineTo(x = rightIntermediateX - rightGapOffset, y = rightIntermediateY)
            lineTo(x = bottomHorizontalOuterEdgeRightX - rightGapOffset, y = bottomHorizontalSegmentBottomY)
            lineTo(x = bottomHorizontalOuterEdgeLeftX + leftGapOffset, y = bottomHorizontalSegmentBottomY)

            val remainingLeftEndArea = bottomHorizontalOuterEdgeLeftX - leftmostX
            val leftIntermediateX = bottomHorizontalOuterEdgeLeftX - bottomHorizontalEnds.leftIntersectionPct * remainingLeftEndArea
            val leftIntermediateY = bottomHorizontalSegmentBottomY - bottomLeftCornerSlope * (bottomHorizontalOuterEdgeLeftX - leftIntermediateX)
            lineTo(x = leftIntermediateX + leftGapOffset, y = leftIntermediateY)
            close()
        }
    )
    // top-left vertical segment
    drawPath(
        color = if (activatedSegments and 0b10 != 0) activatedColor else deactivatedColor,
        path = Path().apply {
            val topGapOffset = topLeftVerticalEnds.leftVertTopGapOffset(gapSize)
            val bottomGapOffset = topLeftVerticalEnds.leftVertBottomGapOffset(gapSize)
            moveTo(x = leftmostX, y = topLeftVerticalOuterEdgeTopY + topGapOffset)

            val remainingTopEndArea = topLeftVerticalOuterEdgeTopY - topY
            val topIntermediateY = topLeftVerticalOuterEdgeTopY - topLeftVerticalEnds.rightIntersectionPct * remainingTopEndArea
            val topIntermediateX = leftmostX + 1 / topLeftCornerSlope * (topIntermediateY - topLeftVerticalOuterEdgeTopY)
            lineTo(x = topIntermediateX, y = topIntermediateY + topGapOffset)
            lineTo(x = leftVerticalSegmentRightX, y = topLeftVerticalEnds.leftVertInnerEdgeTopY(baseY = baseTopVerticalSegmentTopY, thickness) + topGapOffset)
            lineTo(x = leftVerticalSegmentRightX, y = topLeftVerticalEnds.leftVertInnerEdgeBottomY(baseY = baseTopVerticalSegmentBottomY, thickness) - bottomGapOffset)

            val remainingBottomEndArea = middleHorizontalSegmentBottomY - topLeftVerticalOuterEdgeBottomY
            val bottomIntermediateY = topLeftVerticalOuterEdgeBottomY + topLeftVerticalEnds.leftIntersectionPct * remainingBottomEndArea
            val bottomIntermediateX = leftmostX + 1 / middleLeftUpperToLowerSlope * (bottomIntermediateY - topLeftVerticalOuterEdgeBottomY)
            lineTo(x = bottomIntermediateX, y = bottomIntermediateY - bottomGapOffset)
            lineTo(x = leftmostX, y = topLeftVerticalOuterEdgeBottomY - bottomGapOffset)
            close()
        }
    )
    // top-right vertical segment
    drawPath(
        color = if (activatedSegments and 0b100 != 0) activatedColor else deactivatedColor,
        path = Path().apply {
            val topGapOffset = topRightVerticalEnds.rightVertTopGapOffset(gapSize)
            val bottomGapOffset = topRightVerticalEnds.rightVertBottomGapOffset(gapSize)

            moveTo(x = rightVerticalSegmentLeftX, y = topRightVerticalEnds.rightVertInnerEdgeTopY(baseY = baseTopVerticalSegmentTopY, thickness) + topGapOffset)

            val remainingTopEndArea = topRightVerticalOuterEdgeTopY - topY
            val topIntermediateY = topRightVerticalOuterEdgeTopY - topRightVerticalEnds.leftIntersectionPct * remainingTopEndArea
            val topIntermediateX = rightmostX - 1 / topRightCornerSlope * (topRightVerticalOuterEdgeTopY - topIntermediateY)
            lineTo(x = topIntermediateX, y = topIntermediateY + topGapOffset)
            lineTo(x = rightmostX, y = topRightVerticalOuterEdgeTopY + topGapOffset)
            lineTo(x = rightmostX, y = topRightVerticalOuterEdgeBottomY - bottomGapOffset)

            val remainingBottomEndArea = middleHorizontalSegmentBottomY - topRightVerticalOuterEdgeBottomY
            val bottomIntermediateY = topRightVerticalOuterEdgeBottomY + topRightVerticalEnds.rightIntersectionPct * remainingBottomEndArea
            val bottomIntermediateX = rightmostX - 1 / middleRightUpperToLowerSlope * (bottomIntermediateY - topRightVerticalOuterEdgeBottomY)
            lineTo(x = bottomIntermediateX, y = bottomIntermediateY - bottomGapOffset)
            lineTo(x = rightVerticalSegmentLeftX, y = topRightVerticalEnds.rightVertInnerEdgeBottomY(baseY = baseTopVerticalSegmentBottomY, thickness) - bottomGapOffset)
            close()
        }
    )
    // bottom-left vertical segment
    drawPath(
        color = if (activatedSegments and 0b10000 != 0) activatedColor else deactivatedColor,
        path = Path().apply {
            val topGapOffset = bottomLeftVerticalEnds.leftVertTopGapOffset(gapSize)
            val bottomGapOffset = bottomLeftVerticalEnds.leftVertBottomGapOffset(gapSize)

            moveTo(x = leftmostX, y = bottomLeftVerticalOuterEdgeTopY + topGapOffset)

            val remainingTopEndArea = bottomLeftVerticalOuterEdgeTopY - middleHorizontalSegmentTopY
            val topIntermediateY = bottomLeftVerticalOuterEdgeTopY - bottomLeftVerticalEnds.rightIntersectionPct * remainingTopEndArea
            val topIntermediateX = leftmostX + 1 / middleLeftLowerToUpperSlope * (topIntermediateY - bottomLeftVerticalOuterEdgeTopY)
            lineTo(x = topIntermediateX, y = topIntermediateY + topGapOffset)
            lineTo(x = leftVerticalSegmentRightX, y = bottomLeftVerticalEnds.leftVertInnerEdgeTopY(baseY = baseBottomVerticalSegmentTopY, thickness) + topGapOffset)
            lineTo(x = leftVerticalSegmentRightX, y = bottomLeftVerticalEnds.leftVertInnerEdgeBottomY(baseY = baseBottomVerticalSegmentBottomY, thickness) - bottomGapOffset)

            val remainingBottomEndArea = bottomHorizontalSegmentBottomY - bottomLeftVerticalOuterEdgeBottomY
            val bottomIntermediateY = bottomLeftVerticalOuterEdgeBottomY + bottomLeftVerticalEnds.leftIntersectionPct * remainingBottomEndArea
            val bottomIntermediateX = leftmostX + 1 / bottomLeftCornerSlope * (bottomIntermediateY - bottomLeftVerticalOuterEdgeBottomY)
            lineTo(x = bottomIntermediateX, y = bottomIntermediateY - bottomGapOffset)
            lineTo(x = leftmostX, y = bottomLeftVerticalOuterEdgeBottomY - bottomGapOffset)
            close()
        }
    )
    // bottom-right vertical segment
    drawPath(
        color = if (activatedSegments and 0b100000 != 0) activatedColor else deactivatedColor,
        path = Path().apply {
            val topGapOffset = bottomRightEnds.rightVertTopGapOffset(gapSize)
            val bottomGapOffset = bottomRightEnds.rightVertBottomGapOffset(gapSize)

            moveTo(x = rightVerticalSegmentLeftX, y = bottomRightEnds.rightVertInnerEdgeTopY(baseY = baseBottomVerticalSegmentTopY, thickness) + topGapOffset)

            val remainingTopEndArea = bottomRightVerticalOuterEdgeTopY - middleHorizontalSegmentTopY
            val topIntermediateY = bottomRightVerticalOuterEdgeTopY - bottomRightEnds.leftIntersectionPct * remainingTopEndArea
            val topIntermediateX = rightmostX - 1 / middleRightLowerToUpperSlope * (topIntermediateY - bottomRightVerticalOuterEdgeTopY)
            lineTo(x = topIntermediateX, y = topIntermediateY + topGapOffset)
            lineTo(x = rightmostX, y = bottomRightVerticalOuterEdgeTopY + topGapOffset)
            lineTo(x = rightmostX, y = bottomRightVerticalOuterEdgeBottomY - bottomGapOffset)

            val remainingBottomEndArea = bottomHorizontalSegmentBottomY - bottomRightVerticalOuterEdgeBottomY
            val bottomIntermediateY = bottomRightVerticalOuterEdgeBottomY + bottomRightEnds.rightIntersectionPct * remainingBottomEndArea
            val bottomIntermediateX = rightmostX - 1 / bottomRightCornerSlope * (bottomRightVerticalOuterEdgeBottomY - bottomIntermediateY)
            lineTo(x = bottomIntermediateX, y = bottomIntermediateY - bottomGapOffset)
            lineTo(x = rightVerticalSegmentLeftX, y = bottomRightEnds.rightVertInnerEdgeBottomY(baseY = baseBottomVerticalSegmentBottomY, thickness) - bottomGapOffset)
            close()
        }
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
        val topAreaCenterLine = (topY + centerY + thickness / 2) / 2
        drawLine(
            brush = SolidColor(Color.Green),
            start = Offset(x = leftmostX, y = topAreaCenterLine),
            end = Offset(x = leftmostX + drawableWidth, y = topAreaCenterLine),
        )
        // Center line of the bottom area
        val bottomAreaCenterLine = (centerY + topY + drawableHeight - thickness / 2) / 2
        drawLine(
            brush = SolidColor(Color.Green),
            start = Offset(x = leftmostX, y = bottomAreaCenterLine),
            end = Offset(x = leftmostX + drawableWidth, y = bottomAreaCenterLine),
        )
    }
}

private fun AngledSegmentEnds.horizOuterEdgeLeftX(baseX: Float, thickness: Float): Float = baseX - outerEdgeLeftArea * thickness
private fun AngledSegmentEnds.horizOuterEdgeRightX(baseX: Float, thickness: Float): Float = baseX + outerEdgeRightArea * thickness
private fun AngledSegmentEnds.horizInnerEdgeLeftX(baseX: Float, thickness: Float): Float = baseX - innerEdgeLeftArea * thickness
private fun AngledSegmentEnds.horizInnerEdgeRightX(baseX: Float, thickness: Float): Float = baseX + innerEdgeRightArea * thickness
private fun AngledSegmentEnds.leftVertOuterEdgeTopY(baseY: Float, thickness: Float): Float = baseY - outerEdgeRightArea * thickness
private fun AngledSegmentEnds.leftVertOuterEdgeBottomY(baseY: Float, thickness: Float): Float = baseY + outerEdgeLeftArea * thickness
private fun AngledSegmentEnds.leftVertInnerEdgeTopY(baseY: Float, thickness: Float): Float = baseY - innerEdgeRightArea * thickness
private fun AngledSegmentEnds.leftVertInnerEdgeBottomY(baseY: Float, thickness: Float): Float = baseY + innerEdgeLeftArea * thickness
private fun AngledSegmentEnds.rightVertOuterEdgeTopY(baseY: Float, thickness: Float): Float = baseY - outerEdgeLeftArea * thickness
private fun AngledSegmentEnds.rightVertOuterEdgeBottomY(baseY: Float, thickness: Float): Float = baseY + outerEdgeRightArea * thickness
private fun AngledSegmentEnds.rightVertInnerEdgeTopY(baseY: Float, thickness: Float): Float = baseY - innerEdgeLeftArea * thickness
private fun AngledSegmentEnds.rightVertInnerEdgeBottomY(baseY: Float, thickness: Float): Float = baseY + innerEdgeRightArea * thickness

private fun AngledSegmentEnds.horizLeftGapOffset(gapSize: Float): Float = gapSize * (1 - leftIntersectionPct)
private fun AngledSegmentEnds.horizRightGapOffset(gapSize: Float): Float = gapSize * (1 - rightIntersectionPct)
private fun AngledSegmentEnds.leftVertTopGapOffset(gapSize: Float): Float = gapSize * (1 - rightIntersectionPct)
private fun AngledSegmentEnds.leftVertBottomGapOffset(gapSize: Float): Float = gapSize * (1 - leftIntersectionPct)
private fun AngledSegmentEnds.rightVertTopGapOffset(gapSize: Float): Float = gapSize * (1 - leftIntersectionPct)
private fun AngledSegmentEnds.rightVertBottomGapOffset(gapSize: Float): Float = gapSize * (1 - rightIntersectionPct)

internal fun slope(x1: Float, y1: Float, x2: Float, y2: Float): Float = (y2 - y1) / (x2 - x1)