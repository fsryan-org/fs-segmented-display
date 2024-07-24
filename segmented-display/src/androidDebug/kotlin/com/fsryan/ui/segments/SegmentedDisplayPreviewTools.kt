package com.fsryan.ui.segments

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp

@Composable
fun SegmentActivator(
    numSegments: Int = 7,
    activatedSegments: MutableState<Int> = remember { mutableIntStateOf(0b01111111) },
    textMeasurer: TextMeasurer = rememberTextMeasurer(),
    content: @Composable (activatedSegments: Int) -> Unit
) {
    val density = LocalDensity.current.density
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        content(activatedSegments.value)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                // rotating 180 degrees enables us to see the least significant bit on the RIGHT
                .rotate(180F),
            horizontalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            for (i in 0 until numSegments) {
                val activated = (activatedSegments.value and (1 shl i)) != 0
                val text = i.toString()
                val measuredSize = textMeasurer.measure(text)
                Box(
                    modifier = Modifier
                        .height(50.dp)
                        .weight(1F)
                        .background(if (activated) Color.Red else Color.Red.copy(alpha = 0.25F))
                        .clickable { activatedSegments.value = activatedSegments.value xor (1 shl i) },
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(
                        modifier = Modifier.width((measuredSize.size.width / density).dp)
                            .height((measuredSize.size.height / density).dp)
                            .rotate(180F)
                    ) {
                        drawText(textMeasurer = textMeasurer, "$i", topLeft = Offset.Zero)
                    }
                }
            }
        }
    }
}