package com.fsryan.ui.segments

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope

@Composable
fun Hexagonal7SegmentDisplay(
    modifier: Modifier = Modifier,
    text: String,
    thicknessMultiplier: Float = 1F,
    gapSize: Float = 5F,
    activatedColor: Color = Color.Black,
    deactivatedColor: Color = activatedColor.copy(alpha = 0.05F),
    hexagonalSegmentParams: (index: Int, leftTop: Boolean) -> HexagonalSegmentParams = { _, _ ->
        HexagonalSegmentParams.EVEN
    },
    charToActivatedSegments: (Char) -> Int = ::translateHexToActiveSegments
) {
    val topLeftPadding = Offset(x = 10F, y = 10F)
    val bottomRightPadding = Offset(x = 10F, y = 10F)
    SingleLineSegmentedDisplay(modifier = modifier, text = text) { char, origin, charWidth, charHeight ->
        drawHex7SegmentChar(
            activatedSegments = charToActivatedSegments(char),
            origin = origin,
            width = charWidth,
            height = charHeight,
            gapSize = gapSize,
            topLeftPadding = topLeftPadding,
            bottomRightPadding = bottomRightPadding,
            thicknessMultiplier = thicknessMultiplier,
            activatedColor = activatedColor,
            deactivatedColor = deactivatedColor,
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
    gapSize: Float,
    activatedColor: Color,
    deactivatedColor: Color = activatedColor.copy(alpha = 0.05F),
    hexagonalSegmentParams: (index: Int, leftTop: Boolean) -> HexagonalSegmentParams = { _, _ ->
        HexagonalSegmentParams.EVEN
    }
) {
    // Important sizes
    val drawableWidth = width - topLeftPadding.x - bottomRightPadding.x
    val drawableHeight = height - topLeftPadding.y - bottomRightPadding.y
    val topAreaHeight = drawableHeight * topHeightPercentage
    val bottomAreaHeight = drawableHeight - topAreaHeight
    val halfGapSize = gapSize / 2
    
    // thickness is calculated pre-gap size. The gap size must eat into the
    // resulting size of each segment, but it does so differently per segment
    val configuredThickness = 29F / 240.37F * drawableHeight * thicknessMultiplier
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

    // Horizontal anchors
    val rectLeftX = centerX - halfActualHorizontalSegmentWidth
    val rectRightX = centerX + halfActualHorizontalSegmentWidth

    // Vertical anchors
    val topAreaSegmentCenterY = topAreaCenterY + configuredThickness / 4
    val topAreaRectTop = topAreaSegmentCenterY - halfTopSegmentHeight
    val topAreaRectBottom = topAreaSegmentCenterY + halfTopSegmentHeight
    val bottomAreaSegmentCenterY = bottomAreaCenterY - configuredThickness / 4
    val bottomAreaRectTop = bottomAreaSegmentCenterY - halfBottomSegmentHeight
    val bottomAreaRectBottom = bottomAreaSegmentCenterY + halfBottomSegmentHeight

    // Draw top horizontal segment
    drawPath(
        path = Path().apply {
            val leftParams = hexagonalSegmentParams(0, true)
            val rightParams = hexagonalSegmentParams(0, false)

            val leftTopX = centerX - halfActualHorizontalSegmentWidth * leftParams.outerLengthPct
            val leftTopY = topY
            val leftMiddleX = rectLeftX - actualThickness * leftParams.extThicknessHorizPct
            val leftMiddleY = leftTopY + actualThickness * leftParams.extThicknessVertPct
            val leftBottomX = centerX - halfActualHorizontalSegmentWidth * leftParams.innerLengthPct
            val leftBottomY = leftTopY + actualThickness
            val rightTopX = centerX + halfActualHorizontalSegmentWidth * rightParams.outerLengthPct
            val rightTopY = leftTopY
            val rightMiddleX = rectRightX + actualThickness * rightParams.extThicknessHorizPct
            val rightMiddleY = rightTopY + actualThickness * rightParams.extThicknessVertPct
            val rightBottomX = centerX + halfActualHorizontalSegmentWidth * rightParams.innerLengthPct
            val rightBottomY = leftBottomY

            moveTo(leftMiddleX, leftMiddleY)
            lineTo(leftTopX, leftTopY)
            lineTo(rightTopX, rightTopY)
            lineTo(rightMiddleX, rightMiddleY)
            lineTo(rightBottomX, rightBottomY)
            lineTo(leftBottomX, leftBottomY)
            close()
        },
        color = if (activatedSegments and 0b1 == 1) activatedColor else deactivatedColor
    )

    // Draw top-left vertical segment
    drawPath(
        path = Path().apply {
            val topParams = hexagonalSegmentParams(1, true)
            val bottomParams = hexagonalSegmentParams(1, false)

            val leftTopX = leftmostX
            val leftTopY = topAreaSegmentCenterY - halfTopSegmentHeight * topParams.outerLengthPct
            val topMiddleX = leftTopX + actualThickness * topParams.extThicknessVertPct
            val topMiddleY = topAreaRectTop - actualThickness * topParams.extThicknessHorizPct
            val rightTopX = leftTopX + actualThickness
            val rightTopY = topAreaSegmentCenterY - halfTopSegmentHeight * topParams.innerLengthPct
            val rightBottomX = rightTopX
            val rightBottomY = topAreaSegmentCenterY + halfTopSegmentHeight * bottomParams.innerLengthPct
            val bottomMiddleX = leftTopX + actualThickness * bottomParams.extThicknessVertPct
            val bottomMiddleY = topAreaRectBottom + actualThickness * bottomParams.extThicknessHorizPct
            val leftBottomX = leftTopX
            val leftBottomY = topAreaSegmentCenterY + halfTopSegmentHeight * bottomParams.outerLengthPct

            moveTo(bottomMiddleX, bottomMiddleY)
            lineTo(leftBottomX, leftBottomY)
            lineTo(leftTopX, leftTopY)
            lineTo(topMiddleX, topMiddleY)
            lineTo(rightTopX, rightTopY)
            lineTo(rightBottomX, rightBottomY)
            close()
        },
        color = if (activatedSegments and 0b10 != 0) activatedColor else deactivatedColor
    )

    // Draw top-right vertical segment
    drawPath(
        path = Path().apply {
            val topParams = hexagonalSegmentParams(2, true)
            val bottomParams = hexagonalSegmentParams(2, false)

            val leftTopX = rightmostX - actualThickness
            val leftTopY = topAreaSegmentCenterY - halfTopSegmentHeight * topParams.innerLengthPct

            val topMiddleX = leftTopX + actualThickness * topParams.extThicknessVertPct
            val topMiddleY = topAreaRectTop - actualThickness * topParams.extThicknessHorizPct

            val rightTopX = leftTopX + actualThickness
            val rightTopY = topAreaSegmentCenterY - halfTopSegmentHeight * topParams.outerLengthPct
            val rightBottomX = rightTopX
            val rightBottomY = topAreaSegmentCenterY + halfTopSegmentHeight * bottomParams.outerLengthPct
            val bottomMiddleX = leftTopX + actualThickness * bottomParams.extThicknessVertPct
            val bottomMiddleY = topAreaRectBottom + actualThickness * bottomParams.extThicknessHorizPct
            val leftBottomX = leftTopX
            val leftBottomY = topAreaSegmentCenterY + halfTopSegmentHeight * bottomParams.innerLengthPct

            moveTo(bottomMiddleX, bottomMiddleY)
            lineTo(leftBottomX, leftBottomY)
            lineTo(leftTopX, leftTopY)
            lineTo(topMiddleX, topMiddleY)
            lineTo(rightTopX, rightTopY)
            lineTo(rightBottomX, rightBottomY)
            close()
        },
        color = if (activatedSegments and 0b100 != 0) activatedColor else deactivatedColor
    )

    // Draw middle horizontal segment
    drawPath(
        path = Path().apply {
            val leftParams = hexagonalSegmentParams(3, true)
            val rightParams = hexagonalSegmentParams(3, false)

            val leftTopX = centerX - halfActualHorizontalSegmentWidth * leftParams.outerLengthPct
            val leftTopY = topY + topAreaHeight - actualThickness / 2
            val leftMiddleX = rectLeftX - actualThickness * leftParams.extThicknessHorizPct
            val leftMiddleY = leftTopY + actualThickness * leftParams.extThicknessVertPct
            val leftBottomX = centerX - halfActualHorizontalSegmentWidth * leftParams.innerLengthPct
            val leftBottomY = leftTopY + actualThickness
            val rightTopX = centerX + halfActualHorizontalSegmentWidth * rightParams.outerLengthPct
            val rightTopY = leftTopY
            val rightMiddleX = rectRightX + actualThickness * rightParams.extThicknessHorizPct
            val rightMiddleY = rightTopY + actualThickness * rightParams.extThicknessVertPct
            val rightBottomX = centerX + halfActualHorizontalSegmentWidth * rightParams.innerLengthPct
            val rightBottomY = leftBottomY

            moveTo(leftMiddleX, leftMiddleY)
            lineTo(leftTopX, leftTopY)
            lineTo(rightTopX, rightTopY)
            lineTo(rightMiddleX, rightMiddleY)
            lineTo(rightBottomX, rightBottomY)
            lineTo(leftBottomX, leftBottomY)
            close()
        },
        color = if (activatedSegments and 0b1000 != 0) activatedColor else deactivatedColor
    )

    // Draw bottom-left vertical segment
    drawPath(
        path = Path().apply {
            val topParams = hexagonalSegmentParams(4, true)
            val bottomParams = hexagonalSegmentParams(4, false)

            val leftTopX = leftmostX
            val leftTopY = bottomAreaSegmentCenterY - halfBottomSegmentHeight * topParams.outerLengthPct
            val topMiddleX = leftTopX + actualThickness * topParams.extThicknessVertPct
            val topMiddleY = bottomAreaRectTop - actualThickness * topParams.extThicknessHorizPct
            val rightTopX = leftTopX + actualThickness
            val rightTopY = bottomAreaSegmentCenterY - halfBottomSegmentHeight * topParams.innerLengthPct
            val rightBottomX = rightTopX
            val rightBottomY = bottomAreaSegmentCenterY + halfBottomSegmentHeight * bottomParams.innerLengthPct
            val bottomMiddleX = leftTopX + actualThickness * bottomParams.extThicknessVertPct
            val bottomMiddleY = bottomAreaRectBottom + actualThickness * bottomParams.extThicknessHorizPct
            val leftBottomX = leftTopX
            val leftBottomY = bottomAreaSegmentCenterY + halfBottomSegmentHeight * bottomParams.outerLengthPct

            moveTo(bottomMiddleX, bottomMiddleY)
            lineTo(leftBottomX, leftBottomY)
            lineTo(leftTopX, leftTopY)
            lineTo(topMiddleX, topMiddleY)
            lineTo(rightTopX, rightTopY)
            lineTo(rightBottomX, rightBottomY)
            close()
        },
        color = if (activatedSegments and 0b10000 != 0) activatedColor else deactivatedColor
    )

    // Draw bottom-right vertical segment
    drawPath(
        path = Path().apply {
            val topParams = hexagonalSegmentParams(5, true)
            val bottomParams = hexagonalSegmentParams(5, false)

            val leftTopX = rightmostX - actualThickness
            val leftTopY = bottomAreaSegmentCenterY - halfBottomSegmentHeight * topParams.innerLengthPct
            val topMiddleX = leftTopX + actualThickness * topParams.extThicknessVertPct
            val topMiddleY = bottomAreaRectTop - actualThickness * topParams.extThicknessHorizPct
            val rightTopX = leftTopX + actualThickness
            val rightTopY = bottomAreaSegmentCenterY - halfBottomSegmentHeight * topParams.outerLengthPct
            val rightBottomX = rightTopX
            val rightBottomY = bottomAreaSegmentCenterY + halfBottomSegmentHeight * bottomParams.outerLengthPct
            val bottomMiddleX = leftTopX + actualThickness * bottomParams.extThicknessVertPct
            val bottomMiddleY = bottomAreaRectBottom + actualThickness * bottomParams.extThicknessHorizPct
            val leftBottomX = leftTopX
            val leftBottomY = bottomAreaSegmentCenterY + halfBottomSegmentHeight * bottomParams.innerLengthPct

            moveTo(bottomMiddleX, bottomMiddleY)
            lineTo(leftBottomX, leftBottomY)
            lineTo(leftTopX, leftTopY)
            lineTo(topMiddleX, topMiddleY)
            lineTo(rightTopX, rightTopY)
            lineTo(rightBottomX, rightBottomY)
            close()
        },
        color = if (activatedSegments and 0b100000 != 0) activatedColor else deactivatedColor
    )

    // Draw bottom horizontal segment
    drawPath(
        path = Path().apply {
            val leftParams = hexagonalSegmentParams(6, true)
            val rightParams = hexagonalSegmentParams(6, false)

            val leftTopX = centerX - halfActualHorizontalSegmentWidth * leftParams.innerLengthPct
            val leftTopY = bottomY - actualThickness
            val leftMiddleX = rectLeftX - actualThickness * leftParams.extThicknessHorizPct
            val leftMiddleY = leftTopY + actualThickness * leftParams.extThicknessVertPct
            val leftBottomX = centerX - halfActualHorizontalSegmentWidth * leftParams.outerLengthPct
            val leftBottomY = leftTopY + actualThickness
            val rightTopX = centerX + halfActualHorizontalSegmentWidth * rightParams.innerLengthPct
            val rightTopY = leftTopY
            val rightMiddleX = rectRightX + actualThickness * rightParams.extThicknessHorizPct
            val rightMiddleY = rightTopY + actualThickness * rightParams.extThicknessVertPct
            val rightBottomX = centerX + halfActualHorizontalSegmentWidth * rightParams.outerLengthPct
            val rightBottomY = leftBottomY

            moveTo(leftMiddleX, leftMiddleY)
            lineTo(leftTopX, leftTopY)
            lineTo(rightTopX, rightTopY)
            lineTo(rightMiddleX, rightMiddleY)
            lineTo(rightBottomX, rightBottomY)
            lineTo(leftBottomX, leftBottomY)
            close()
        },
        color = if (activatedSegments and 0b1000000 != 0) activatedColor else deactivatedColor
    )




    // debugging
//    drawLine(
//        brush = SolidColor(Color.Blue),
//        start = Offset(x = centerX, y = topY),
//        end = Offset(x = centerX, y = topY + drawableHeight),
//    )
//    drawLine(
//        brush = SolidColor(Color.Green),
//        start = Offset(x = leftmostX, y = topAreaSegmentCenterY),
//        end = Offset(x = leftmostX + drawableWidth, y = topAreaSegmentCenterY),
//    )
//    drawLine(
//        brush = SolidColor(Color.Green),
//        start = Offset(x = leftmostX, y = topY + topAreaHeight),
//        end = Offset(x = leftmostX + drawableWidth, y = topY + topAreaHeight),
//    )
//    drawLine(
//        brush = SolidColor(Color.Green),
//        start = Offset(x = leftmostX, y = bottomAreaSegmentCenterY),
//        end = Offset(x = leftmostX + drawableWidth, y = bottomAreaSegmentCenterY),
//    )
}