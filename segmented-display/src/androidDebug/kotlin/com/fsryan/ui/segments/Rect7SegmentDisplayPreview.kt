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
    Column {
        var activatedSegments by remember { mutableIntStateOf(0b01111111) }
        Canvas(
            modifier = Modifier.width(200.dp)
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
        Row(
            modifier = Modifier
                .width(200.dp)
                // rotating 180 degrees enables us to see the least significant bit on the RIGHT
                .rotate(180F),
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            for (i in 0 until 7) {
                val activated = (activatedSegments and (1 shl i)) != 0
                Box(
                    modifier = Modifier.height(50.dp)
                        .weight(1F)
                        .background(if (activated) Color.Red else Color.Red.copy(alpha = 0.25F))
                        .clickable { activatedSegments = activatedSegments xor (1 shl i) }
                )
            }
        }
    }
}