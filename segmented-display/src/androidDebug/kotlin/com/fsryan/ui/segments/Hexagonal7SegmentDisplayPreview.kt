package com.fsryan.ui.segments

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
@Preview(showBackground = true, widthDp = 134, heightDp = 240)
fun Hexagonal7Segments0() {
    Canvas(
        modifier = Modifier.fillMaxSize()
            .background(Color.LightGray)
    ) {
        drawHex7SegmentChar(
            activatedSegments = 0b01110111,
            origin = Offset(x = 0F, y = 0F),
            width = size.width,
            height = size.height,
            gapSize = 5F,
            topLeftPadding = Offset(x = 10F, y = 10F),
            bottomRightPadding = Offset(x = 10F, y = 10F),
            activatedColor = Color.Black
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 134, heightDp = 240)
fun Hexagonal7Segments1() {
    Canvas(
        modifier = Modifier.fillMaxSize()
            .background(Color.LightGray)
    ) {
        drawHex7SegmentChar(
            activatedSegments = 0b00100100,
            origin = Offset(x = 0F, y = 0F),
            width = size.width,
            height = size.height,
            gapSize = 5F,
            topLeftPadding = Offset(x = 10F, y = 10F),
            bottomRightPadding = Offset(x = 10F, y = 10F),
            activatedColor = Color.Black
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 134, heightDp = 240)
fun Hexagonal7Segments2() {
    Canvas(
        modifier = Modifier.fillMaxSize()
            .background(Color.LightGray)
    ) {
        drawHex7SegmentChar(
            activatedSegments = 0b01011101,
            origin = Offset(x = 0F, y = 0F),
            width = size.width,
            height = size.height,
            gapSize = 5F,
            topLeftPadding = Offset(x = 10F, y = 10F),
            bottomRightPadding = Offset(x = 10F, y = 10F),
            activatedColor = Color.Black
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 134, heightDp = 240)
fun Hexagonal7Segments3() {
    Canvas(
        modifier = Modifier.fillMaxSize()
            .background(Color.LightGray)
    ) {
        drawHex7SegmentChar(
            activatedSegments = 0b01101101,
            origin = Offset(x = 0F, y = 0F),
            width = size.width,
            height = size.height,
            gapSize = 5F,
            topLeftPadding = Offset(x = 10F, y = 10F),
            bottomRightPadding = Offset(x = 10F, y = 10F),
            activatedColor = Color.Black
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 134, heightDp = 240)
fun Hexagonal7Segments4() {
    Canvas(
        modifier = Modifier.fillMaxSize()
            .background(Color.LightGray)
    ) {
        drawHex7SegmentChar(
            activatedSegments = 0b00101110,
            origin = Offset(x = 0F, y = 0F),
            width = size.width,
            height = size.height,
            gapSize = 5F,
            topLeftPadding = Offset(x = 10F, y = 10F),
            bottomRightPadding = Offset(x = 10F, y = 10F),
            activatedColor = Color.Black
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 134, heightDp = 240)
fun Hexagonal7Segments5() {
    Canvas(
        modifier = Modifier.fillMaxSize()
            .background(Color.LightGray)
    ) {
        drawHex7SegmentChar(
            activatedSegments = 0b01101011,
            origin = Offset(x = 0F, y = 0F),
            width = size.width,
            height = size.height,
            gapSize = 5F,
            topLeftPadding = Offset(x = 10F, y = 10F),
            bottomRightPadding = Offset(x = 10F, y = 10F),
            activatedColor = Color.Black
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 134, heightDp = 240)
fun Hexagonal7Segments6() {
    Canvas(
        modifier = Modifier.fillMaxSize()
            .background(Color.LightGray)
    ) {
        drawHex7SegmentChar(
            activatedSegments = 0b01111011,
            origin = Offset(x = 0F, y = 0F),
            width = size.width,
            height = size.height,
            gapSize = 5F,
            topLeftPadding = Offset(x = 10F, y = 10F),
            bottomRightPadding = Offset(x = 10F, y = 10F),
            activatedColor = Color.Black
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 134, heightDp = 240)
fun Hexagonal7Segments7() {
    Canvas(
        modifier = Modifier.fillMaxSize()
            .background(Color.LightGray)
    ) {
        drawHex7SegmentChar(
            activatedSegments = 0b00100101,
            origin = Offset(x = 0F, y = 0F),
            width = size.width,
            height = size.height,
            gapSize = 5F,
            topLeftPadding = Offset(x = 10F, y = 10F),
            bottomRightPadding = Offset(x = 10F, y = 10F),
            activatedColor = Color.Black
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 134, heightDp = 240)
fun Hexagonal7Segments8() {
    Canvas(
        modifier = Modifier.fillMaxSize()
            .background(Color.LightGray)
    ) {
        drawHex7SegmentChar(
            activatedSegments = 0b01111111,
            origin = Offset(x = 0F, y = 0F),
            width = size.width,
            height = size.height,
            gapSize = 5F,
            topLeftPadding = Offset(x = 10F, y = 10F),
            bottomRightPadding = Offset(x = 10F, y = 10F),
            activatedColor = Color.Black
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 134, heightDp = 240)
fun Hexagonal7Segments9() {
    Canvas(
        modifier = Modifier.fillMaxSize()
            .background(Color.LightGray)
    ) {
        drawHex7SegmentChar(
            activatedSegments = 0b01101111,
            origin = Offset(x = 0F, y = 0F),
            width = size.width,
            height = size.height,
            gapSize = 5F,
            topLeftPadding = Offset(x = 10F, y = 10F),
            bottomRightPadding = Offset(x = 10F, y = 10F),
            activatedColor = Color.Black
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 134, heightDp = 240)
fun Hexagonal7SegmentsA() {
    Canvas(
        modifier = Modifier.fillMaxSize()
            .background(Color.LightGray)
    ) {
        drawHex7SegmentChar(
            activatedSegments = 0b00111111,
            origin = Offset(x = 0F, y = 0F),
            width = size.width,
            height = size.height,
            gapSize = 5F,
            topLeftPadding = Offset(x = 10F, y = 10F),
            bottomRightPadding = Offset(x = 10F, y = 10F),
            activatedColor = Color.Black
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 134, heightDp = 240)
fun Hexagonal7SegmentsB() {
    Canvas(
        modifier = Modifier.fillMaxSize()
            .background(Color.LightGray)
    ) {
        drawHex7SegmentChar(
            activatedSegments = 0b01111010,
            origin = Offset(x = 0F, y = 0F),
            width = size.width,
            height = size.height,
            gapSize = 5F,
            topLeftPadding = Offset(x = 10F, y = 10F),
            bottomRightPadding = Offset(x = 10F, y = 10F),
            activatedColor = Color.Black
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 134, heightDp = 240)
fun Hexagonal7SegmentsC() {
    Canvas(
        modifier = Modifier.fillMaxSize()
            .background(Color.LightGray)
    ) {
        drawHex7SegmentChar(
            activatedSegments = 0b01010011,
            origin = Offset(x = 0F, y = 0F),
            width = size.width,
            height = size.height,
            gapSize = 5F,
            topLeftPadding = Offset(x = 10F, y = 10F),
            bottomRightPadding = Offset(x = 10F, y = 10F),
            activatedColor = Color.Black
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 134, heightDp = 240)
fun Hexagonal7SegmentsD() {
    Canvas(
        modifier = Modifier.fillMaxSize()
            .background(Color.LightGray)
    ) {
        drawHex7SegmentChar(
            activatedSegments = 0b01111100,
            origin = Offset(x = 0F, y = 0F),
            width = size.width,
            height = size.height,
            gapSize = 5F,
            topLeftPadding = Offset(x = 10F, y = 10F),
            bottomRightPadding = Offset(x = 10F, y = 10F),
            activatedColor = Color.Black
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 134, heightDp = 240)
fun Hexagonal7SegmentsE() {
    Canvas(
        modifier = Modifier.fillMaxSize()
            .background(Color.LightGray)
    ) {
        drawHex7SegmentChar(
            activatedSegments = 0b01011011,
            origin = Offset(x = 0F, y = 0F),
            width = size.width,
            height = size.height,
            gapSize = 5F,
            topLeftPadding = Offset(x = 10F, y = 10F),
            bottomRightPadding = Offset(x = 10F, y = 10F),
            activatedColor = Color.Black
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 134, heightDp = 240)
fun Hexagonal7SegmentsF() {
    Canvas(
        modifier = Modifier.fillMaxSize()
            .background(Color.LightGray)
    ) {
        drawHex7SegmentChar(
            activatedSegments = 0b00011011,
            origin = Offset(x = 0F, y = 0F),
            width = size.width,
            height = size.height,
            gapSize = 5F,
            topLeftPadding = Offset(x = 10F, y = 10F),
            bottomRightPadding = Offset(x = 10F, y = 10F),
            activatedColor = Color.Black
        )
    }
}

@Composable
@Preview(showBackground = true, widthDp = 1675, heightDp = 179)
fun Hexagonal7SegmentsAllHex() {
    Hexagonal7SegmentDisplay(
        modifier = Modifier.background(Color.LightGray),
        text = "0123456789ABCDEF",
        gapSize = 3F,
        activatedColor = Color.Black
    )
}


