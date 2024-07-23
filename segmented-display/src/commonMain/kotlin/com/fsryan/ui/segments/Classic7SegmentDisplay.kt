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

    val thickness = 29F / 240.37F * drawableHeight * thicknessMultiplier

    val horizontalSegmentLength = drawableWidth - 2 * thickness
    val horizonalSegmentStartX = leftmostX + thickness

    val topVerticalSegmentStartY = topY + thickness
    val topVerticalSegmentEndY = centerY - thickness / 2
    val bottomVerticalSegmentStartY = centerY + thickness / 2
    val bottomVerticalSegmentEndY = topY + drawableHeight - thickness
    val rightVerticalSegmentStartX = leftmostX + drawableWidth - thickness

    // top horizontal segment
    drawPath(
        color = if (activatedSegments and 0b1 != 0) activatedColor else deactivatedColor,
        path = Path().apply {
            moveTo(x = horizonalSegmentStartX, y = topY)
            lineTo(x = horizonalSegmentStartX + horizontalSegmentLength, y = topY)
            lineTo(x = horizonalSegmentStartX + horizontalSegmentLength, y = topY + thickness)
            lineTo(x = horizonalSegmentStartX, y = topY + thickness)
            close()
        }
    )
    // middle horizontal segment
    drawPath(
        color = if (activatedSegments and 0b1000 != 0) activatedColor else deactivatedColor,
        path = Path().apply {
            moveTo(x = horizonalSegmentStartX, y = centerY - thickness / 2)
            lineTo(x = horizonalSegmentStartX + horizontalSegmentLength, y = centerY - thickness / 2)
            lineTo(x = horizonalSegmentStartX + horizontalSegmentLength, y = centerY + thickness / 2)
            lineTo(x = horizonalSegmentStartX, y = centerY + thickness / 2)
            close()
        }
    )
    // bottom horizontal segment
    drawPath(
        color = if (activatedSegments and 0b1000000 != 0) activatedColor else deactivatedColor,
        path = Path().apply {
            moveTo(x = horizonalSegmentStartX, y = topY + drawableHeight - thickness)
            lineTo(x = horizonalSegmentStartX + horizontalSegmentLength, y = topY + drawableHeight - thickness)
            lineTo(x = horizonalSegmentStartX + horizontalSegmentLength, y = topY + drawableHeight)
            lineTo(x = horizonalSegmentStartX, y = topY + drawableHeight)
            close()
        }
    )
    // top-left vertical segment
    drawPath(
        color = if (activatedSegments and 0b10 != 0) activatedColor else deactivatedColor,
        path = Path().apply {
            moveTo(x = leftmostX, y = topVerticalSegmentStartY)
            lineTo(x = leftmostX + thickness, y = topVerticalSegmentStartY)
            lineTo(x = leftmostX + thickness, y = topVerticalSegmentEndY)
            lineTo(x = leftmostX, y = topVerticalSegmentEndY)
            close()
        }
    )
    // top-right vertical segment
    drawPath(
        color = if (activatedSegments and 0b100 != 0) activatedColor else deactivatedColor,
        path = Path().apply {
            moveTo(x = rightVerticalSegmentStartX, y = topVerticalSegmentStartY)
            lineTo(x = rightVerticalSegmentStartX + thickness, y = topVerticalSegmentStartY)
            lineTo(x = rightVerticalSegmentStartX + thickness, y = topVerticalSegmentEndY)
            lineTo(x = rightVerticalSegmentStartX, y = topVerticalSegmentEndY)
            close()
        }
    )
    // bottom-left vertical segment
    drawPath(
        color = if (activatedSegments and 0b10000 != 0) activatedColor else deactivatedColor,
        path = Path().apply {
            moveTo(x = leftmostX, y = bottomVerticalSegmentStartY)
            lineTo(x = leftmostX + thickness, y = bottomVerticalSegmentStartY)
            lineTo(x = leftmostX + thickness, y = bottomVerticalSegmentEndY)
            lineTo(x = leftmostX, y = bottomVerticalSegmentEndY)
            close()
        }
    )
    // bottom-right vertical segment
    drawPath(
        color = if (activatedSegments and 0b100000 != 0) activatedColor else deactivatedColor,
        path = Path().apply {
            moveTo(x = rightVerticalSegmentStartX, y = bottomVerticalSegmentStartY)
            lineTo(x = rightVerticalSegmentStartX + thickness, y = bottomVerticalSegmentStartY)
            lineTo(x = rightVerticalSegmentStartX + thickness, y = bottomVerticalSegmentEndY)
            lineTo(x = rightVerticalSegmentStartX, y = bottomVerticalSegmentEndY)
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