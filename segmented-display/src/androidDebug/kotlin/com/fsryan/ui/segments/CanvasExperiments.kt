package com.fsryan.ui.segments

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Preview
@Composable
internal fun RenderACanvas() {
    Canvas(
        modifier = Modifier
            .width(50.dp)
            .height(100.dp)
            .background(Color.Red)
    ) {

    }
}

@Preview
@Composable
internal fun RenderAVerticalLine() {
    Canvas(
        modifier = Modifier
            .width(50.dp)
            .height(100.dp)
    ) {
        drawLine(
            color = Color.Blue,
            start = Offset(x = size.width / 2, y = 0F),
            end = Offset(x = size.width / 2, y = size.height)
        )
    }
}

@Preview
@Composable
internal fun RenderAHorizontalLine() {
    Canvas(
        modifier = Modifier
            .width(50.dp)
            .height(100.dp)
    ) {
        drawLine(
            color = Color.Green,
            start = Offset(x = 0F, y = size.height / 2),
            end = Offset(x = size.width, y = size.height / 2)
        )
    }
}

@Preview
@Composable
internal fun RenderARectangle() {
    Canvas(
        modifier = Modifier
            .width(50.dp)
            .height(100.dp)
    ) {
        drawRect(
            color = Color.Magenta,
            topLeft = Offset(x = 10F, y = 10F),
            size = size.copy(width = size.width - 20, height = size.height - 20)
        )
    }
}

@Preview
@Composable
internal fun RenderACircle() {
    Canvas(
        modifier = Modifier
            .width(50.dp)
            .height(100.dp)
    ) {
        drawCircle(
            color = Color.Yellow,
            center = Offset(x = size.width / 2, y = size.height / 2),
            radius = size.width / 3
        )
    }
}

@Preview
@Composable
internal fun RenderARegularPentagon() {
    Canvas(
        modifier = Modifier
            .width(50.dp)
            .height(100.dp)
    ) {
        // https://en.wikipedia.org/wiki/Regular_polygon#Cartesian_coordinates
        val radius = size.width / 3
        val centerX = size.width / 2
        val centerY = size.height / 2
        val points = (0..4).map { idx ->
            // All the points of a regular polygon lie on a circle. The number
            // of points is the number of sides of the polygon. Each point is
            // equally-spaced along the circle. Thus, the x value will always
            // be cos(angle) * radius plus the center X and the y value will
            // always be sin(angle) * radius plus the center Y.
            val angle = PI * 2 / 5 * idx    // in radians.
            Offset(
                x = centerX + radius * cos(angle).toFloat(),
                y = centerY + radius * sin(angle).toFloat()
            )
        }
        drawPath(
            path = Path().apply {
                points.forEachIndexed { idx, point ->
                    if (idx == 0) {
                        moveTo(point.x, point.y)
                    } else {
                        lineTo(point.x, point.y)
                    }
                }
                close()
            },
            color = Color.Cyan
        )
    }
}

@Preview
@Composable
internal fun RenderARegularNGon() {
    var sideCount by remember { mutableIntStateOf(5) }  // side count is the parameter
    val textMeasurer = rememberTextMeasurer()

    // The column wraps the canvas and the buttons
    Column(
        modifier = Modifier
            .width(50.dp)
            .height(100.dp)
    ) {
        Canvas(
            modifier = Modifier
                .width(50.dp)
                .height(75.dp)
        ) {
            // This is the same drawing code as before, just with a larger radius
            val radius = size.width / 2.3F
            val centerX = size.width / 2
            val centerY = size.height / 2
            val points = (0..sideCount).map { idx ->
                val angle = PI * 2 / sideCount * idx    // in radians.
                Offset(
                    x = centerX + radius * cos(angle).toFloat(),
                    y = centerY + radius * sin(angle).toFloat()
                )
            }
            drawPath(
                path = Path().apply {
                    points.forEachIndexed { idx, point ->
                        if (idx == 0) {
                            moveTo(point.x, point.y)
                        } else {
                            lineTo(point.x, point.y)
                        }
                    }
                    close()
                },
                color = Color.Cyan
            )

            // Here, were drawing text that has the current side count
            val measuredSize = textMeasurer.measure("$sideCount")
            drawText(
                textLayoutResult = measuredSize,
                brush = SolidColor(Color.Black),
                // Here, we center the text
                topLeft = Offset(
                    x = centerX - measuredSize.size.width.toFloat() / 2,
                    y = centerY - measuredSize.size.height.toFloat() / 2
                )
            )
        }
        // Below is our basic control surface for incrementing/decrementing the
        // count
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(25.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1F)
                    .fillMaxHeight()
                    .background(Color.Red)
                    .clickable { // red button reduces the number of sides
                        if (sideCount > 3) {
                            sideCount--
                        }
                    }
            ) {
            }
            Box(
                modifier = Modifier
                    .weight(1F)
                    .fillMaxHeight()
                    .background(Color.Green)
                    .clickable { sideCount++ }  // green button increases the number of sides
            ) {
            }
        }
    }
}