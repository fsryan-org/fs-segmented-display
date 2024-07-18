package com.fsryan.ui.segments

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope

@Composable
fun SingleLineSegmentedDisplay(
    modifier: Modifier = Modifier,
    text: String,
    renderCharOnCanvas: DrawScope.(idx: Int, char: Char, offset: Offset, charWidth: Float, charHeight: Float) -> Unit
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val charWidth = size.width / text.length
        text.forEachIndexed { idx, char ->
            val offset = Offset(x = idx * charWidth, y = 0F)
            renderCharOnCanvas(idx, char, offset, charWidth, size.height)
        }
    }
}