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
            val shearingMatrix = getOrCreateShearingMatrix(shearPct)
            val shearPx = abs(shearPct) * size.height
            withTransform(
                transformBlock = {
                    transform(shearingMatrix)
                    val leftInset = if (shearPct < 0) 0F else shearPx
                    val rightInset = if (shearPct < 0) shearPx else 0F
                    inset(left = leftInset, top = 0F, right = rightInset, bottom = 0F)
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

// access should be bound to a single thread
private fun getOrCreateShearingMatrix(shearPct: Float): Matrix {
    return shearingMatrixPool[shearPct] ?: Matrix(
        floatArrayOf(
            1F,         0F, 0F, 0F,
            -shearPct,  1F, 0F, 0F,
            0F,         0F, 1F, 0F,
            0F,         0F, 0F, 1F
        )
    ).also {
        shearingMatrixPool[shearPct] = it
        shearingMatrixKeyList.add(shearPct)
        while (shearingMatrixKeyList.size > 10) {
            val toRemove = shearingMatrixKeyList.removeFirst()
            shearingMatrixPool.remove(toRemove)
        }
    }
}
private val shearingMatrixKeyList = mutableListOf<Float>()
private val shearingMatrixPool = mutableMapOf<Float, Matrix>()