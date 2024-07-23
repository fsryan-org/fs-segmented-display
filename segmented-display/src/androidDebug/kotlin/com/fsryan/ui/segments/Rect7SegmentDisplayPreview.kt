package com.fsryan.ui.segments

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun DrawRect7SegmentChar() {
    SegmentActivator { activatedSegments ->
        Canvas(
            modifier = Modifier
                .width(200.dp)
                .height(400.dp)
        ) {
            drawRect7SegmentChar(
                origin = Offset(x = 0F, y = 0F),
                activatedSegments = activatedSegments,
                width = size.width,
                height = size.height,
                topLeftPadding = Offset(x = 40F, y = 40F),
                bottomRightPadding = Offset(x = 40F, y = 40F),
                topAreaPercentage = 0.40F,
                thicknessMultiplier = 0.5F,
                gapSizeMultiplier = 4F,
                activatedColor = Color.Black
            )
        }
    }
}

@Preview(widthDp = 800)
@Composable
fun RectangularHexCharacters() {
    Column {
        Rect7SegmentDisplay(
            modifier = Modifier
                .background(Color.LightGray)
                .width(800.dp)
                .height(200.dp),
            text = "01234567",
            thicknessMultiplier = 0.6F,
            activatedColor = Color.Black
        )
        Rect7SegmentDisplay(
            modifier = Modifier
                .background(Color.LightGray)
                .width(800.dp)
                .height(200.dp),
            thicknessMultiplier = 0.6F,
            gapSizeMultiplier = 0.75F,
            text = "89ABCDEF",
            activatedColor = Color.Black
        )
    }
}