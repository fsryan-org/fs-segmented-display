package com.fsryan.ui.segments

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope

@Composable
fun Rect7SegmentDisplay(
    modifier: Modifier = Modifier,
    text: String,
    thicknessMultiplier: Float = 1F,
    gapSize: Float = 5F,
    activatedColor: Color = Color.Black,
    deactivatedColor: Color = activatedColor.copy(alpha = 0.05F),
    charToActivatedSegments: (Char) -> Int = { char ->
        when (char) {
            '0' -> 0b01110111
            '1' -> 0b00100100
            '2' -> 0b01011101
            '3' -> 0b01101101
            '4' -> 0b00101110
            '5' -> 0b01101011
            '6' -> 0b01111011
            '7' -> 0b00100101
            '8' -> 0b01111111
            '9' -> 0b01101111
            'A', 'a' -> 0b00111111
            'B', 'b' -> 0b01111010
            'C', 'c' -> 0b01010011
            'D', 'd' -> 0b01111100
            'E', 'e' -> 0b01011011
            'F', 'f' -> 0b00011011
            else -> 0   // <-- cannot render
        }
    }
) {
    val topLeftPadding = Offset(x = 10F, y = 10F)
    val bottomRightPadding = Offset(x = 10F, y = 10F)
    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        val height = size.height
        val charWidth = size.width / text.length
        text.forEachIndexed { idx, char ->
            drawRect7SegmentChar(
                activatedSegments = charToActivatedSegments(char),
                origin = Offset(x = idx * charWidth, y = 0F),
                width = charWidth,
                height = height,
                gapSize = gapSize,
                topLeftPadding = topLeftPadding,
                bottomRightPadding = bottomRightPadding,
                thicknessMultiplier = thicknessMultiplier,
                activatedColor = activatedColor,
                deactivatedColor = deactivatedColor
            )
        }
    }
}

fun DrawScope.drawRect7SegmentChar(
    activatedSegments: Int,
    origin: Offset,
    width: Float,
    height: Float,
    topLeftPadding: Offset,
    bottomRightPadding: Offset,
    topBottomHeightRatio: Float = 105F / 107,
    thicknessMultiplier: Float = 1F,
    gapSize: Float,
    activatedColor: Color,
    deactivatedColor: Color = activatedColor.copy(alpha = 0.05F)
) {
    val drawableWidth = width - topLeftPadding.x - bottomRightPadding.x
    val drawableHeight = height - topLeftPadding.y - bottomRightPadding.y
    val topAreaHeight = drawableHeight * topBottomHeightRatio / 2
    val bottomAreaHeight = drawableHeight - topAreaHeight
    val halfGapSize = gapSize / 2
    
    // thickness is calculated pre-gap size. The gap size must eat into the
    // resulting size of each segment, but it does so differently per segment
    val configuredThickness = 29F / 240.37F * drawableHeight * thicknessMultiplier
    val actualThickness = configuredThickness - halfGapSize // <-- the actual thickness of each segment

    // anchor points
    val topLeftX = origin.x + topLeftPadding.x
    val topLeftY = origin.y + topLeftPadding.y
    val bottomRightX = origin.x + width - bottomRightPadding.x
    val bottomRightY = origin.y + height - bottomRightPadding.y

    // sizes without gaps
    val topSegmentHeight = topAreaHeight - configuredThickness * 1.5F  // <-- the full top segment plus half of the middle segment
    val bottomSegmentHeight = bottomAreaHeight - configuredThickness * 1.5F  // <-- the full bottom segment plus half of the middle segment
    val configuredHorizontalSegmentWidth = drawableWidth - 2 * configuredThickness
    val actualHorizontalSegmentWidth = configuredHorizontalSegmentWidth - gapSize

    // Draw top horizontal segment
    drawPath(
        path = Path().apply {
            val leftX = topLeftX + configuredThickness + halfGapSize
            val topY = topLeftY
            val rightX = leftX + actualHorizontalSegmentWidth
            val bottomY = topY + actualThickness
            moveTo(leftX, topY)
            lineTo(rightX, topY)
            lineTo(rightX, bottomY)
            lineTo(leftX, bottomY)
            close()
        },
        color = if (activatedSegments and 0b1 == 1) activatedColor else deactivatedColor
    )

    // Draw top-left vertical segment
    drawPath(
        path = Path().apply {
            val leftX = topLeftX
            val topY = topLeftY + configuredThickness + halfGapSize
            val rightX = leftX + actualThickness
            val bottomY = topY + topSegmentHeight - gapSize
            moveTo(leftX, topY)
            lineTo(rightX, topY)
            lineTo(rightX, bottomY)
            lineTo(leftX, bottomY)
            close()
        },
        color = if (activatedSegments and 0b10 != 0) activatedColor else deactivatedColor
    )

    // Draw top-right vertical segment
    drawPath(
        path = Path().apply {
            val leftX = bottomRightX - actualThickness
            val topY = topLeftY + configuredThickness + halfGapSize
            val rightX = bottomRightX
            val bottomY = topY + topSegmentHeight - gapSize
            moveTo(leftX, topY)
            lineTo(rightX, topY)
            lineTo(rightX, bottomY)
            lineTo(leftX, bottomY)
            close()
        },
        color = if (activatedSegments and 0b100 != 0) activatedColor else deactivatedColor
    )

    // Draw middle horizontal segment
    drawPath(
        path = Path().apply {
            val middleY = topLeftY + topAreaHeight
            val leftX = topLeftX + configuredThickness + halfGapSize
            val topY = middleY - actualThickness / 2
            val rightX = leftX + actualHorizontalSegmentWidth
            val bottomY = topY + actualThickness
            moveTo(leftX, topY)
            lineTo(rightX, topY)
            lineTo(rightX, bottomY)
            lineTo(leftX, bottomY)
            close()
        },
        color = if (activatedSegments and 0b1000 != 0) activatedColor else deactivatedColor
    )

    // Draw bottom-left vertical segment
    drawPath(
        path = Path().apply {
            val leftX = topLeftX
            val topY = bottomRightY - configuredThickness - bottomSegmentHeight + halfGapSize
            val rightX = leftX + actualThickness
            val bottomY = topY + bottomSegmentHeight - gapSize
            moveTo(leftX, topY)
            lineTo(rightX, topY)
            lineTo(rightX, bottomY)
            lineTo(leftX, bottomY)
            close()
        },
        color = if (activatedSegments and 0b10000 != 0) activatedColor else deactivatedColor
    )

    // Draw bottom-right vertical segment
    drawPath(
        path = Path().apply {
            val leftX = bottomRightX - actualThickness
            val topY = bottomRightY - configuredThickness - bottomSegmentHeight + halfGapSize
            val rightX = bottomRightX
            val bottomY = topY + bottomSegmentHeight - gapSize
            moveTo(leftX, topY)
            lineTo(rightX, topY)
            lineTo(rightX, bottomY)
            lineTo(leftX, bottomY)
            close()
        },
        color = if (activatedSegments and 0b100000 != 0) activatedColor else deactivatedColor
    )

    // Draw bottom horizontal segment
    drawPath(
        path = Path().apply {
            val leftX = topLeftX + configuredThickness + halfGapSize
            val topY = bottomRightY - configuredThickness + halfGapSize
            val rightX = leftX + configuredHorizontalSegmentWidth - gapSize
            val bottomY = bottomRightY
            moveTo(leftX, topY)
            lineTo(rightX, topY)
            lineTo(rightX, bottomY)
            lineTo(leftX, bottomY)
            close()
        },
        color = if (activatedSegments and 0b1000000 != 0) activatedColor else deactivatedColor
    )
}