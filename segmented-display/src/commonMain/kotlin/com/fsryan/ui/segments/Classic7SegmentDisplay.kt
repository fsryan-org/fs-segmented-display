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
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.rotateRad
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp

@Composable
fun Classic7SegmentDisplay(
    modifier: Modifier = Modifier,
    text: String,
    paddingValues: PaddingValues = PaddingValues(4.dp),
    topAreaPercentage: Float = 0.495F,
    thicknessMultiplier: Float = 1F,
    gapSizeMultiplier: Float = 1F,
    activatedColor: Color = Color.Black,
    deactivatedColor: Color = activatedColor.copy(alpha = 0.05F),
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
    SingleLineSegmentedDisplay(modifier = modifier, text = text) { _, char, origin, charWidth, charHeight ->
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
            debuggingEnabled = false
        )
    }
}

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
    angledSegmentEndsOf: (Int) -> AngledSegmentEnds = { AngledSegmentEnds.EVEN }
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

    val baseHorizontalSegmentLength = drawableWidth - 2 * thickness
    val baseHorizontalSegmentLeftX = leftmostX + thickness
    val baseHorizontalSegmentRightX = baseHorizontalSegmentLeftX + baseHorizontalSegmentLength

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
    val rightVerticalSegmentEndX = rightmostX

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
    val topRightCornerSlope = slope(x1 = topHorizontalOuterEdgeRightX, y1 = topY, x2 = rightVerticalSegmentEndX, y2 = topRightVerticalOuterEdgeTopY)
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

            moveTo(x = topHorizontalOuterEdgeLeftX, y = topY)
            lineTo(x = topHorizontalOuterEdgeRightX, y = topY)

            val remainingRightEndArea = rightmostX - topHorizontalOuterEdgeRightX
            val rightIntermediateX = topHorizontalOuterEdgeRightX + topHorizontalEnds.rightIntersectionPct * remainingRightEndArea
            val rightIntermediateY = topY + topRightCornerSlope * (rightIntermediateX - topHorizontalOuterEdgeRightX)
            lineTo(x = rightIntermediateX, y = rightIntermediateY)
            lineTo(x = topHorizontalEnds.horizInnerEdgeRightX(baseX = baseHorizontalSegmentRightX, thickness), y = topHorizontalSegmentBottomY)
            lineTo(x = topHorizontalEnds.horizInnerEdgeLeftX(baseX = baseHorizontalSegmentLeftX, thickness), y = topHorizontalSegmentBottomY)

            val remainingLeftEndArea = topHorizontalOuterEdgeLeftX - leftmostX
            val leftIntermediateX = topHorizontalOuterEdgeLeftX - topHorizontalEnds.leftIntersectionPct * remainingLeftEndArea
            val leftIntermediateY = topY - topLeftCornerSlope * (topHorizontalOuterEdgeLeftX - leftIntermediateX)
            lineTo(x = leftIntermediateX, y = leftIntermediateY)
            close()
        }
    )
    // middle horizontal segment
    drawPath(
        color = if (activatedSegments and 0b1000 != 0) activatedColor else deactivatedColor,
        path = Path().apply {
            // This path could possibly be an octagon because of the way we're calculating the intersection points
            moveTo(x = middleHorizontalUpperEdgeLeftX, y = middleHorizontalSegmentTopY)
            lineTo(x = middleHorizontalUpperEdgeRightX, y = middleHorizontalSegmentTopY)

            val upperRemainingRightEndArea = rightmostX - middleHorizontalUpperEdgeRightX
            val upperRightIntermediateX = middleHorizontalUpperEdgeRightX + middleHorizontalEnds.rightIntersectionPct * upperRemainingRightEndArea
            val upperRightIntermediateY = middleHorizontalSegmentTopY + middleRightUpperToLowerSlope * (upperRightIntermediateX - middleHorizontalUpperEdgeRightX)
            lineTo(x = upperRightIntermediateX, y = upperRightIntermediateY)

            val lowerRemainingRightEndArea = rightmostX - middleHorizontalLowerEdgeRightX
            val lowerRightIntermediateX = middleHorizontalLowerEdgeRightX + middleHorizontalEnds.rightIntersectionPct * lowerRemainingRightEndArea
            val lowerRightIntermediateY = middleHorizontalSegmentBottomY + middleRightLowerToUpperSlope * (lowerRightIntermediateX - middleHorizontalLowerEdgeRightX)
            lineTo(x = lowerRightIntermediateX, y = lowerRightIntermediateY)
            lineTo(x = middleHorizontalLowerEdgeRightX, y = middleHorizontalSegmentBottomY)
            lineTo(x = middleHorizontalLowerEdgeLeftX, y = middleHorizontalSegmentBottomY)

            val lowerRemainingLeftEndArea = middleHorizontalLowerEdgeLeftX - leftmostX
            val lowerLeftIntermediateX = middleHorizontalLowerEdgeLeftX - middleHorizontalEnds.leftIntersectionPct * lowerRemainingLeftEndArea
            val lowerLeftIntermediateY = middleHorizontalSegmentBottomY - middleLeftUpperToLowerSlope * (middleHorizontalLowerEdgeLeftX - lowerLeftIntermediateX)
            lineTo(x = lowerLeftIntermediateX, y = lowerLeftIntermediateY)

            val upperRemainingLeftEndArea = middleHorizontalUpperEdgeLeftX - leftmostX
            val upperLeftIntermediateX = middleHorizontalUpperEdgeLeftX - middleHorizontalEnds.leftIntersectionPct * upperRemainingLeftEndArea
            val upperLeftIntermediateY = middleHorizontalSegmentTopY + middleLeftLowerToUpperSlope * (upperLeftIntermediateX - middleHorizontalUpperEdgeLeftX)
            lineTo(x = upperLeftIntermediateX, y = upperLeftIntermediateY)

            close()
        }
    )
    // bottom horizontal segment
    drawPath(
        color = if (activatedSegments and 0b1000000 != 0) activatedColor else deactivatedColor,
        path = Path().apply {
            moveTo(x = bottomHorizontalEnds.horizInnerEdgeLeftX(baseX = baseHorizontalSegmentLeftX, thickness), y = bottomHorizontalSegmentTopY)
            lineTo(x = bottomHorizontalEnds.horizInnerEdgeRightX(baseX = baseHorizontalSegmentRightX, thickness), y = bottomHorizontalSegmentTopY)

            val remainingRightEndArea = rightmostX - bottomHorizontalOuterEdgeRightX
            val rightIntermediateX = bottomHorizontalOuterEdgeRightX + bottomHorizontalEnds.rightIntersectionPct * remainingRightEndArea
            val rightIntermediateY = bottomHorizontalSegmentBottomY + bottomRightCornerSlope * (rightIntermediateX - bottomHorizontalOuterEdgeRightX)
            lineTo(x = rightIntermediateX, y = rightIntermediateY)
            lineTo(x = bottomHorizontalOuterEdgeRightX, y = bottomHorizontalSegmentBottomY)
            lineTo(x = bottomHorizontalOuterEdgeLeftX, y = bottomHorizontalSegmentBottomY)

            val remainingLeftEndArea = bottomHorizontalOuterEdgeLeftX - leftmostX
            val leftIntermediateX = bottomHorizontalOuterEdgeLeftX - bottomHorizontalEnds.leftIntersectionPct * remainingLeftEndArea
            val leftIntermediateY = bottomHorizontalSegmentBottomY - bottomLeftCornerSlope * (bottomHorizontalOuterEdgeLeftX - leftIntermediateX)
            lineTo(x = leftIntermediateX, y = leftIntermediateY)
            close()
        }
    )
    // top-left vertical segment
    drawPath(
        color = if (activatedSegments and 0b10 != 0) activatedColor else deactivatedColor,
        path = Path().apply {
            moveTo(x = leftmostX, y = topLeftVerticalOuterEdgeTopY)

            val remainingTopEndArea = topLeftVerticalOuterEdgeTopY - topY
            val topIntermediateY = topLeftVerticalOuterEdgeTopY - topLeftVerticalEnds.rightIntersectionPct * remainingTopEndArea
            val topIntermediateX = leftmostX + 1 / topLeftCornerSlope * (topIntermediateY - topLeftVerticalOuterEdgeTopY)
            lineTo(x = topIntermediateX, y = topIntermediateY)
            lineTo(x = leftVerticalSegmentRightX, y = topLeftVerticalEnds.leftVertInnerEdgeTopY(baseY = baseTopVerticalSegmentTopY, thickness))
            lineTo(x = leftVerticalSegmentRightX, y = topLeftVerticalEnds.leftVertInnerEdgeBottomY(baseY = baseTopVerticalSegmentBottomY, thickness))

            val remainingBottomEndArea = middleHorizontalSegmentBottomY - topLeftVerticalOuterEdgeBottomY
            val bottomIntermediateY = topLeftVerticalOuterEdgeBottomY + topLeftVerticalEnds.leftIntersectionPct * remainingBottomEndArea
            val bottomIntermediateX = leftmostX + 1 / middleLeftUpperToLowerSlope * (bottomIntermediateY - topLeftVerticalOuterEdgeBottomY)
            lineTo(x = bottomIntermediateX, y = bottomIntermediateY)
            lineTo(x = leftmostX, y = topLeftVerticalOuterEdgeBottomY)
            close()
        }
    )
    // top-right vertical segment
    drawPath(
        color = if (activatedSegments and 0b100 != 0) activatedColor else deactivatedColor,
        path = Path().apply {
            moveTo(x = rightVerticalSegmentLeftX, y = topRightVerticalEnds.rightVertInnerEdgeTopY(baseY = baseTopVerticalSegmentTopY, thickness))

            val remainingTopEndArea = topRightVerticalOuterEdgeTopY - topY
            val topIntermediateY = topRightVerticalOuterEdgeTopY - topRightVerticalEnds.leftIntersectionPct * remainingTopEndArea
            val topIntermediateX = rightmostX - 1 / topRightCornerSlope * (topRightVerticalOuterEdgeTopY - topIntermediateY)
            lineTo(x = topIntermediateX, y = topIntermediateY)
            lineTo(x = rightmostX, y = topRightVerticalOuterEdgeTopY)
            lineTo(x = rightmostX, y = topRightVerticalOuterEdgeBottomY)

            val remainingBottomEndArea = middleHorizontalSegmentBottomY - topRightVerticalOuterEdgeBottomY
            val bottomIntermediateY = topRightVerticalOuterEdgeBottomY + topRightVerticalEnds.rightIntersectionPct * remainingBottomEndArea
            val bottomIntermediateX = rightmostX - 1 / middleRightUpperToLowerSlope * (bottomIntermediateY - topRightVerticalOuterEdgeBottomY)
            lineTo(x = bottomIntermediateX, y = bottomIntermediateY)
            lineTo(x = rightVerticalSegmentLeftX, y = topRightVerticalEnds.rightVertInnerEdgeBottomY(baseY = baseTopVerticalSegmentBottomY, thickness))
            close()
        }
    )
    // bottom-left vertical segment
    drawPath(
        color = if (activatedSegments and 0b10000 != 0) activatedColor else deactivatedColor,
        path = Path().apply {
            moveTo(x = leftmostX, y = bottomLeftVerticalOuterEdgeTopY)

            val remainingTopEndArea = bottomLeftVerticalOuterEdgeTopY - middleHorizontalSegmentTopY
            val topIntermediateY = bottomLeftVerticalOuterEdgeTopY - bottomLeftVerticalEnds.rightIntersectionPct * remainingTopEndArea
            val topIntermediateX = leftmostX + 1 / middleLeftLowerToUpperSlope * (topIntermediateY - bottomLeftVerticalOuterEdgeTopY)
            lineTo(x = topIntermediateX, y = topIntermediateY)
            lineTo(x = leftVerticalSegmentRightX, y = bottomLeftVerticalEnds.leftVertInnerEdgeTopY(baseY = baseBottomVerticalSegmentTopY, thickness))
            lineTo(x = leftVerticalSegmentRightX, y = bottomLeftVerticalEnds.leftVertInnerEdgeBottomY(baseY = baseBottomVerticalSegmentBottomY, thickness))

            val remainingBottomEndArea = bottomHorizontalSegmentBottomY - bottomLeftVerticalOuterEdgeBottomY
            val bottomIntermediateY = bottomLeftVerticalOuterEdgeBottomY + bottomLeftVerticalEnds.leftIntersectionPct * remainingBottomEndArea
            val bottomIntermediateX = leftmostX + 1 / bottomLeftCornerSlope * (bottomIntermediateY - bottomLeftVerticalOuterEdgeBottomY)
            lineTo(x = bottomIntermediateX, y = bottomIntermediateY)
            lineTo(x = leftmostX, y = bottomLeftVerticalOuterEdgeBottomY)
            close()
        }
    )
    // bottom-right vertical segment
    drawPath(
        color = if (activatedSegments and 0b100000 != 0) activatedColor else deactivatedColor,
        path = Path().apply {
            val remainingTopEndArea = bottomRightVerticalOuterEdgeTopY - middleHorizontalSegmentTopY
            val topIntermediateY = bottomRightVerticalOuterEdgeTopY - bottomRightEnds.leftIntersectionPct * remainingTopEndArea
            val topIntermediateX = rightmostX - 1 / middleRightLowerToUpperSlope * (topIntermediateY - bottomRightVerticalOuterEdgeTopY)

            val remainingBottomEndArea = bottomHorizontalSegmentBottomY - bottomRightVerticalOuterEdgeBottomY
            val bottomIntermediateY = bottomRightVerticalOuterEdgeBottomY + bottomRightEnds.rightIntersectionPct * remainingBottomEndArea
            val bottomIntermediateX = rightmostX - 1 / bottomRightCornerSlope * (bottomRightVerticalOuterEdgeBottomY - bottomIntermediateY)

            moveTo(x = rightVerticalSegmentLeftX, y = bottomRightEnds.rightVertInnerEdgeTopY(baseY = baseBottomVerticalSegmentTopY, thickness))
            lineTo(x = topIntermediateX, y = topIntermediateY)
            lineTo(x = rightmostX, y = bottomRightVerticalOuterEdgeTopY)
            lineTo(x = rightmostX, y = bottomRightVerticalOuterEdgeBottomY)
            lineTo(x = bottomIntermediateX, y = bottomIntermediateY)
            lineTo(x = rightVerticalSegmentLeftX, y = bottomRightEnds.rightVertInnerEdgeBottomY(baseY = baseBottomVerticalSegmentBottomY, thickness))
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

internal fun slope(x1: Float, y1: Float, x2: Float, y2: Float): Float = (y2 - y1) / (x2 - x1)