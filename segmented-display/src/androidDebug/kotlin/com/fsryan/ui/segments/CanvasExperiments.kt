package com.fsryan.ui.segments

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Preview
@Composable
fun RenderACanvas() {
    Canvas(
        modifier = Modifier.width(50.dp).height(100.dp)
            .background(Color.Red)
    ) {

    }
}

@Preview
@Composable
fun RenderAVerticalLine() {
    Canvas(
        modifier = Modifier.width(50.dp).height(100.dp)
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
fun RenderAHorizontalLine() {
    Canvas(
        modifier = Modifier.width(50.dp).height(100.dp)
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
fun RenderARectangle() {
    Canvas(
        modifier = Modifier.width(50.dp).height(100.dp)
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
fun RenderACircle() {
    Canvas(
        modifier = Modifier.width(50.dp).height(100.dp)
    ) {
        drawCircle(
            color = Color.Yellow,
            center = Offset(x = size.width / 2, y = size.height / 2),
            radius = size.width / 3
        )
    }
}