package com.fsryan.ui.segments

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.abs

/**
 * Provides a [Canvas] on which to draw characters of the [text]. This is a low
 * level composable function that provides a lot of flexibility for how the
 * single line of characters are drawn. Each char is monospaced, and the
 * [renderCharOnCanvas] function gets called with the proper offset for that
 * char.
 *
 * > *Note*
 * > Shearing the characters makes them take up more width than they would
 * > otherwise take. This function will automatically adjust the width per char
 * > so that after the shearing has been applied correctly, the resulting width
 * > will be the same as if the characters were not sheared.
 *
 * @param modifier the [Modifier] applied to the [Canvas]
 * @param text the [String] whose characters should be drawn
 * @param shearPct 0 means the [renderCharOnCanvas] renders the characters
 * straight vertically. A positive number means that the characters are
 * rendered with the bottom sheared to the left and the top sheared to the
 * right.
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
        val positiveShearPct = abs(shearPct)
        val allocatedCharWidth = size.width / text.length
        val actualCharWidth = allocatedCharWidth / (1 + positiveShearPct / text.length)
        val shearPx = actualCharWidth * positiveShearPct
        val xOffset = shearPx / 2                   // If we shear the
        text.forEachIndexed { idx, char ->
            val offset = Offset(x = idx * actualCharWidth + xOffset, y = 0F)
            renderCharOnCanvas(char, offset, actualCharWidth, size.height)
        }
    }
}