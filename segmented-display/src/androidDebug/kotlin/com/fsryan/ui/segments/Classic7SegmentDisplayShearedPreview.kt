package com.fsryan.ui.segments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(widthDp = 800)
@Composable
fun Classic7SegmentAsymmetricHexCharactersSheared() {
    val angledSegmentEnds = createAsymmetricAngled7SegmentEndsFun()
    Classic7SegmentDisplay(
        modifier = Modifier
            .background(Color.LightGray)
            .width(800.dp)
            .height(200.dp),
        text = "89ABCDE8",
        shearPct = 0.5F,
        thicknessMultiplier = 0.75F,
        gapSizeMultiplier = 2F,
        activatedColor = Color.Black,
        angledSegmentEndsOf = angledSegmentEnds,
        debuggingEnabled = true
    )
}