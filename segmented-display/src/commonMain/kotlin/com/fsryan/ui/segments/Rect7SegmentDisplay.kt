package com.fsryan.ui.segments

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

fun DrawScope.drawRect7SegmentChar(
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

    val origin = Offset(x = 0F, y = 0F)

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