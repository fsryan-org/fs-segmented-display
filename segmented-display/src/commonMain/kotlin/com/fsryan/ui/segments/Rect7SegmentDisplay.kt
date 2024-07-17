package com.fsryan.ui.segments

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

fun DrawScope.drawRect7SegmentChar(
    width: Float,
    height: Float,
    debuggingEnabled: Boolean = true
) {

    val origin = Offset(x = 0F, y = 0F)

    // Here we define some important values that serve as anchor points and
    // sizes that can be referenced to draw correctly
    val leftmostX = origin.x
    val topY = origin.y
    val drawableWidth = width
    val drawableHeight = height
    val centerX = leftmostX + drawableWidth / 2
    val centerY = topY + drawableHeight / 2

    val configuredThickness = 29F / 240.37F * drawableHeight
    val gapSize = configuredThickness / 10
    val halfGapSize = gapSize / 2
    val actualThickness = configuredThickness - halfGapSize

    val horizontalSegmentLength = drawableWidth - gapSize - 2 * actualThickness
    val horizonalSegmentStartX = leftmostX + actualThickness + halfGapSize

    // top horizontal segment
    drawRect(
        brush = SolidColor(Color.Black),
        topLeft = Offset(x = horizonalSegmentStartX, y = topY),
        size = Size(width = horizontalSegmentLength, height = actualThickness)
    )

    // middle horizontal segment
    drawRect(
        brush = SolidColor(Color.Black),
        topLeft = Offset(x = horizonalSegmentStartX, y = centerY - actualThickness / 2),
        size = Size(width = horizontalSegmentLength, height = actualThickness)
    )

    // bottom horizontal segment
    drawRect(
        brush = SolidColor(Color.Black),
        topLeft = Offset(x = horizonalSegmentStartX, y = topY + drawableHeight - actualThickness),
        size = Size(width = horizontalSegmentLength, height = actualThickness)
    )

    // debugging
    if (debuggingEnabled) {
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
    }
}