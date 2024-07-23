package com.fsryan.ui.segments

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun DrawClassic7SegmentChar() {
    SegmentActivator { activatedSegments ->
        Canvas(
            modifier = Modifier
                .width(200.dp)
                .height(400.dp)
        ) {
            drawClassic7SegmentChar(
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