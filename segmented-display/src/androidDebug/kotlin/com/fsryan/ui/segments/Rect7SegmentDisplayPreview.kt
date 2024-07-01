package com.fsryan.ui.segments

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview(showBackground = true, widthDp = 1675, heightDp = 179)
fun Rectangular7SegmentsAllHex() {
    Rect7SegmentDisplay(
        modifier = Modifier.background(Color.LightGray),
        text = "0123456789ABCDEF",
        thicknessMultiplier = 0.6F,
        gapSize = 3F,
        activatedColor = Color.Black
    )
}


