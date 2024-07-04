package com.fsryan.ui.segments

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.abs
import kotlin.math.sign

/**
 * Provides a [Canvas] on which to draw characters of the [text]. This is a low
 * level composable function that provides a lot of flexibility for how the
 * single line of characters are drawn. Each char is monospaced, and the
 * [renderCharOnCanvas] function gets called with the proper offset for that
 * char.
 *
 * This function also handles applying a shearing function to the output via
 * transforming what the [renderCharOnCanvas] function rendered. It also
 * handles the transformations necessary to resize and offset the output
 * (because shearing would otherwise increase the width) Thus, Your
 * [renderCharOnCanvas] function should assume that it is drawing a vertical
 * character.
 *
 * @param modifier the [Modifier] applied to the [Canvas]
 * @param text the [String] whose characters should be drawn
 * @param shearPct serves to transform the x axis as though it is skewed to the
 * right/left as a percentage of the height. Thus, a value of 1 will skew the
 * output to the right as much as the view is tall. A value of -1 will do the
 * same, but will skew to the left instead of the right.
 * @param renderCharOnCanvas the function responsible for actually drawing the
 * characters on the canvas
 */
@Composable
fun SingleLineSegmentedDisplay(
    modifier: Modifier = Modifier,
    text: String,
    shearPct: Float = 0F,
    renderCharOnCanvas: DrawScope.(char: Char, offset: Offset, charWidth: Float, charHeight: Float) -> Unit
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        if (shearPct == 0F) {
            val charWidth = size.width / text.length
            text.forEachIndexed { idx, char ->
                val offset = Offset(x = idx * charWidth, y = 0F)
                renderCharOnCanvas(char, offset, charWidth, size.height)
            }
        } else {
            // Chances are extremely high that the exact same shearing matrix
            // is desired by the end user in a real-world scenario, thus, we
            // leverage a pool of matrices
            val shearingMatrix = getOrCreateShearingMatrix(shearPct)

            val shearPx = abs(shearPct) * size.height
            val halfShearPx = shearPx / 2
            withTransform(
                transformBlock = {
                    transform(shearingMatrix)
                    translate(left = shearPct.sign * halfShearPx)
                    inset(left = halfShearPx, top = 0F, right = halfShearPx, bottom = 0F)
                }
            ) {
                val charWidth = size.width / text.length
                text.forEachIndexed { idx, char ->
                    val offset = Offset(x = idx * charWidth, y = 0F)
                    renderCharOnCanvas(char, offset, charWidth, size.height)
                }
            }
        }
    }
}

// access should already be bound to the main thread
private fun getOrCreateShearingMatrix(shearPct: Float): Matrix {
    return shearingMatrixPool[shearPct] ?: Matrix(
        floatArrayOf(
            1F, 0F, 0F, 0F,
            -shearPct, 1F, 0F, 0F,
            0F, 0F, 1F, 0F,
            0F, 0F, 0F, 1F
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