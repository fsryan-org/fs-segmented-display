package com.fsryan.ui.segments

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun DrawRect7SegmentChar() {
    Canvas(
        modifier = Modifier.width(200.dp)
            .height(400.dp)
    ) {
        drawRect7SegmentChar(
            width = size.width,
            height = size.height
        )
    }
}