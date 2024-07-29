package com.fsryan.ui.segments

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.abs
import kotlin.math.sign

@Composable
fun SingleLineSegmentedDisplay(
    modifier: Modifier = Modifier,
    text: String,
    shearPct: Float = 0F,
    renderCharOnCanvas: DrawScope.(idx: Int, char: Char, offset: Offset, charWidth: Float, charHeight: Float) -> Unit
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        if (shearPct == 0F) {
            val charWidth = size.width / text.length
            text.forEachIndexed { idx, char ->
                val offset = Offset(x = idx * charWidth, y = 0F)
                renderCharOnCanvas(idx, char, offset, charWidth, size.height)
            }
        } else {
            val shearPx = abs(shearPct) * size.height
            withTransform(
                transformBlock = {
                    transform(
                        matrix = Matrix(
                            floatArrayOf(
                                1F,     0F, 0F, 0F,
                                -shearPct,  1F, 0F, 0F,
                                0F,         0F, 1F, 0F,
                                0F,    0F, 0F, 1F
                            )
                        )
                    )
                    inset(
                        left = if (shearPct < 0) 0F else shearPx,
                        top = 0F,
                        right = if (shearPct < 0) shearPx else 0F,
                        bottom = 0F
                    )
                }
            ) {
                val charWidth = size.width / text.length
                text.forEachIndexed { idx, char ->
                    val offset = Offset(x = idx * charWidth, y = 0F)
                    renderCharOnCanvas(idx, char, offset, charWidth, size.height)
                }
            }
        }
    }
}