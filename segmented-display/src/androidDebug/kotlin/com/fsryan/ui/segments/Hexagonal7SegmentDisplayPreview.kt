package com.fsryan.ui.segments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
@Preview(showBackground = true, widthDp = 1256, heightDp = 536)
fun EvenHexagonal7SegmentsAllHex() {
    Column {
        Box(modifier = Modifier.height(179.dp)) {
            Hexagonal7SegmentDisplay(
                modifier = Modifier.background(Color.LightGray),
                text = "0123456789AB",
                gapSize = 3F,
                activatedColor = Color.Black
            )
        }
        Box(modifier = Modifier.height(179.dp)) {
            Hexagonal7SegmentDisplay(
                modifier = Modifier.background(Color.LightGray),
                text = "CDEFGHIJKLMN",
                gapSize = 3F,
                activatedColor = Color.Black
            )
        }
        Box(modifier = Modifier.height(179.dp)) {
            Hexagonal7SegmentDisplay(
                modifier = Modifier.background(Color.LightGray),
                text = "OPQRSTUVWXYZ",
                gapSize = 3F,
                activatedColor = Color.Black
            )
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 1256, heightDp = 536)
fun ClassicSymmetricHexagonal7SegmentsAllHex() {
    val createHexagonalSegmentParams = createSymmetricHexagonal7SegmentParamsFun()
    Column {
        Box(modifier = Modifier.height(179.dp)) {
            Hexagonal7SegmentDisplay(
                modifier = Modifier.background(Color.LightGray),
                text = "0123456789AB",
                gapSize = 3F,
                activatedColor = Color.Black,
                hexagonalSegmentParams = createHexagonalSegmentParams
            )
        }
        Box(modifier = Modifier.height(179.dp)) {
            Hexagonal7SegmentDisplay(
                modifier = Modifier.background(Color.LightGray),
                text = "CDEFGHIJKLMN",
                gapSize = 3F,
                activatedColor = Color.Black,
                hexagonalSegmentParams = createHexagonalSegmentParams
            )
        }
        Box(modifier = Modifier.height(179.dp)) {
            Hexagonal7SegmentDisplay(
                modifier = Modifier.background(Color.LightGray),
                text = "OPQRSTUVWXYZ",
                gapSize = 3F,
                activatedColor = Color.Black,
                hexagonalSegmentParams = createHexagonalSegmentParams
            )
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 1675, heightDp = 358)
fun ClassicSymmetric7SegmentsAllHexTall() {
    Hexagonal7SegmentDisplay(
        modifier = Modifier.background(Color.LightGray),
        text = "0123456789ABCDEF",
        gapSize = 3F,
        activatedColor = Color.Black,
        hexagonalSegmentParams = createSymmetricHexagonal7SegmentParamsFun()
    )
}

@Composable
@Preview(showBackground = true, widthDp = 1675, heightDp = 90)
fun ClassicSymmetric7SegmentsAllHexShort() {
    Hexagonal7SegmentDisplay(
        modifier = Modifier.background(Color.LightGray),
        text = "0123456789ABCDEF",
        gapSize = 3F,
        activatedColor = Color.Black,
        hexagonalSegmentParams = createSymmetricHexagonal7SegmentParamsFun()
    )
}