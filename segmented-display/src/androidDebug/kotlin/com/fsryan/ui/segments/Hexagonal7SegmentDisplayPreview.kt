package com.fsryan.ui.segments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
                activatedColor = Color.Black
            )
        }
        Box(modifier = Modifier.height(179.dp)) {
            Hexagonal7SegmentDisplay(
                modifier = Modifier.background(Color.LightGray),
                text = "CDEFGHIJKLMN",
                activatedColor = Color.Black
            )
        }
        Box(modifier = Modifier.height(179.dp)) {
            Hexagonal7SegmentDisplay(
                modifier = Modifier.background(Color.LightGray),
                text = "OPQRSTUVWXYZ",
                activatedColor = Color.Black
            )
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 1256, heightDp = 536)
fun ClassicSymmetricHexagonal7SegmentsAllHex() {
    val createHexagonalSegmentParams = HexagonalSegmentParams.classic7SymmetricParamsFun(
        outerLengthPct = 1.3F,
        extThicknessVertPct = 0.3125F
    )
    Column {
        Box(modifier = Modifier.height(179.dp)) {
            Hexagonal7SegmentDisplay(
                modifier = Modifier.background(Color.LightGray),
                text = "0123456789AB",
                activatedColor = Color.Black,
                debuggingEnabled = true,
                hexagonalSegmentParams = createHexagonalSegmentParams
            )
        }
        Box(modifier = Modifier.height(179.dp)) {
            Hexagonal7SegmentDisplay(
                modifier = Modifier.background(Color.LightGray),
                text = "CDEFGHIJKLMN",
                activatedColor = Color.Black,
                debuggingEnabled = true,
                hexagonalSegmentParams = createHexagonalSegmentParams
            )
        }
        Box(modifier = Modifier.height(179.dp)) {
            Hexagonal7SegmentDisplay(
                modifier = Modifier.background(Color.LightGray),
                text = "OPQRSTUVWXYZ",
                activatedColor = Color.Black,
                debuggingEnabled = true,
                hexagonalSegmentParams = createHexagonalSegmentParams
            )
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 1256, heightDp = 536)
fun ClassicAsymmetricHexagonal7SegmentsAllHex() {
    val createHexagonalSegmentParams = HexagonalSegmentParams.classic7AsymmetricParamsFun()
    Column {
        Box(modifier = Modifier.height(179.dp)) {
            Hexagonal7SegmentDisplay(
                modifier = Modifier.background(Color.LightGray),
                text = "0123456789AB",
                thicknessMultiplier = 1.3F, // This thickness is pretty good
                activatedColor = Color.Black,
                hexagonalSegmentParams = createHexagonalSegmentParams
            )
        }
        Box(modifier = Modifier.height(179.dp)) {
            Hexagonal7SegmentDisplay(
                modifier = Modifier.background(Color.LightGray),
                text = "CDEFGHIJKLMN",
                thicknessMultiplier = 1.5F, // Thisi s probably too thick
                activatedColor = Color.Black,
                hexagonalSegmentParams = createHexagonalSegmentParams
            )
        }
        Box(modifier = Modifier.height(179.dp)) {
            Hexagonal7SegmentDisplay(
                modifier = Modifier.background(Color.LightGray),
                text = "OPQRSTUVWXYZ",
                thicknessMultiplier = 0.5F, // This thickness does not look good
                activatedColor = Color.Black,
                hexagonalSegmentParams = createHexagonalSegmentParams
            )
        }
    }
}

@Composable
@Preview(showBackground = true, widthDp = 1256, heightDp = 536)
fun ClassicAsymmetricHexagonal7SegmentsAllHexSheared() {
    val createHexagonalSegmentParams = HexagonalSegmentParams.classic7AsymmetricParamsFun()
    Column {
        Box(modifier = Modifier.height(179.dp)) {
            Hexagonal7SegmentDisplay(
                modifier = Modifier.background(Color.LightGray),
                text = "0123456789AB",
                thicknessMultiplier = 1.3F, // This thickness is pretty good
                shearPct = 0.33F,
                activatedColor = Color.Black,
                debuggingEnabled = true,
                hexagonalSegmentParams = createHexagonalSegmentParams
            )
        }
        Box(modifier = Modifier.height(179.dp)) {
            Hexagonal7SegmentDisplay(
                modifier = Modifier.background(Color.LightGray),
                text = "CDEFGHIJKLMN",
                thicknessMultiplier = 1.3F, // This thickness is pretty good
                shearPct = 0.33F,
                activatedColor = Color.Black,
                debuggingEnabled = true,
                hexagonalSegmentParams = createHexagonalSegmentParams
            )
        }
        Box(modifier = Modifier.height(179.dp)) {
            Hexagonal7SegmentDisplay(
                modifier = Modifier.background(Color.LightGray),
                text = "OPQRSTUVWXYZ",
                thicknessMultiplier = 1.3F, // This thickness is pretty good
                shearPct = 0.33F,           // This shear is a pretty nice
                activatedColor = Color.Black,
                debuggingEnabled = true,
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
        activatedColor = Color.Black,
        hexagonalSegmentParams = HexagonalSegmentParams.classic7SymmetricParamsFun()
    )
}

@Composable
@Preview(showBackground = true, widthDp = 1675, heightDp = 90)
fun ClassicSymmetric7SegmentsAllHexShort() {
    Hexagonal7SegmentDisplay(
        modifier = Modifier.background(Color.LightGray),
        text = "0123456789ABCDEF",
        activatedColor = Color.Black,
        hexagonalSegmentParams = HexagonalSegmentParams.classic7SymmetricParamsFun()
    )
}