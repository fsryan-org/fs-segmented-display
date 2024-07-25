package com.fsryan.ui.segments

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview(showBackground = true)
fun DrawClassic7SegmentEvenChar() {
    SegmentActivator { activatedSegments ->
        Canvas(
            modifier = Modifier
                .width(200.dp)
                .height(400.dp)
        ) {
            drawClassic7SegmentChar(
                origin = Offset(x = 0F, y = 0F),
                activatedSegments = activatedSegments,
                width = size.width,
                height = size.height,
                topLeftPadding = Offset(x = 40F, y = 40F),
                bottomRightPadding = Offset(x = 40F, y = 40F),
                topAreaPercentage = 0.495F,
                thicknessMultiplier = 1F,
                gapSizeMultiplier = 2F,
                activatedColor = Color.Black,
                debuggingEnabled = true,
                angledSegmentEndsOf = ::symmetricEvenAngledSegmentEnds
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DrawClassic7SegmentSymmetricChar() {
    SegmentActivator { activatedSegments ->
        Canvas(
            modifier = Modifier
                .width(200.dp)
                .height(400.dp)
        ) {
            drawClassic7SegmentChar(
                origin = Offset(x = 0F, y = 0F),
                activatedSegments = activatedSegments,
                width = size.width,
                height = size.height,
                topLeftPadding = Offset(x = 40F, y = 40F),
                bottomRightPadding = Offset(x = 40F, y = 40F),
                topAreaPercentage = 0.495F,
                thicknessMultiplier = 1F,
                gapSizeMultiplier = 2F,
                activatedColor = Color.Black,
                debuggingEnabled = true,
                angledSegmentEndsOf = createSymmetricAngled7SegmentEndsFun()
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DrawClassic7SegmentAsymmetricChar() {
    SegmentActivator { activatedSegments ->
        Canvas(
            modifier = Modifier
                .width(200.dp)
                .height(400.dp)
        ) {
            drawClassic7SegmentChar(
                origin = Offset(x = 0F, y = 0F),
                activatedSegments = activatedSegments,
                width = size.width,
                height = size.height,
                topLeftPadding = Offset(x = 40F, y = 40F),
                bottomRightPadding = Offset(x = 40F, y = 40F),
                topAreaPercentage = 0.495F,
                thicknessMultiplier = 1F,
                gapSizeMultiplier = 2F,
                activatedColor = Color.Black,
                debuggingEnabled = true,
                angledSegmentEndsOf = createAsymmetricAngled7SegmentEndsFun()
            )
        }
    }
}