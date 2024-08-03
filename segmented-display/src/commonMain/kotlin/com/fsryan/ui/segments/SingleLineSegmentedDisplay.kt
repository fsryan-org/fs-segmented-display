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

/**
 * A lower-level function that draws a single line of segmented characters on a
 * [Canvas]. This function _DOES NOT_ handle conversion of the characters or
 * how the individual characters should be drawn. Rather, it delegates the
 * responsibility of drawing a single character to the [renderCharOnCanvas]
 * function, providing the offset, width, and height of the character to be
 * drawn.
 *
 * > *Note*:
 * > You should use this function only when you have a custom function for
 * > rendering characters on a [Canvas]. Do not use this function if you want
 * > to render a [Rect7SegmentDisplay] or a [Classic7SegmentDisplay].
 *
 * @param modifier the [Modifier] applied to the [Canvas]
 * @param text the [String] whose characters should be drawn
 * @param shearPct serves to transform the x-axis as though it is skewed to the
 * right/left as a percentage of the height. Thus, a value of 1 will skew the
 * output to the right as much as the view is tall. A value of -1 will do the
 * same, but will skew to the left instead of the right.
 * @param renderCharOnCanvas the function responsible for actually drawing the
 * characters on the canvas
 *
 * @see Rect7SegmentDisplay
 * @see Classic7SegmentDisplay
 * @author fsryan
 */
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