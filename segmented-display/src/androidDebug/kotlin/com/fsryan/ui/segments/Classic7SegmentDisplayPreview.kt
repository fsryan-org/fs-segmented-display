package com.fsryan.ui.segments

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
internal fun DrawClassic7SegmentEvenChar() {
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
internal fun DrawClassic7SegmentSymmetricChar() {
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
internal fun DrawClassic7SegmentAsymmetricChar() {
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

@Preview(widthDp = 800)
@Composable
internal fun Classic7SegmentEvenHexCharacters() {
    Column {
        Classic7SegmentDisplay(
            modifier = Modifier
                .background(Color.LightGray)
                .width(800.dp)
                .height(200.dp),
            text = "01234567",
            thicknessMultiplier = 1.2F,
            gapSizeMultiplier = 2F,
            activatedColor = Color.Black
        )
        Classic7SegmentDisplay(
            modifier = Modifier
                .background(Color.LightGray)
                .width(800.dp)
                .height(200.dp),
            thicknessMultiplier = 1.2F,
            gapSizeMultiplier = 2F,
            text = "89ABCDEF",
            activatedColor = Color.Black
        )
    }
}

@Preview(widthDp = 800)
@Composable
internal fun Classic7SegmentSymmetricHexCharacters() {
    val angledSegmentEnds = createSymmetricAngled7SegmentEndsFun(0.5F)
    Column {
        Classic7SegmentDisplay(
            modifier = Modifier
                .background(Color.LightGray)
                .width(800.dp)
                .height(200.dp),
            text = "01234567",
            thicknessMultiplier = 1.2F,
            gapSizeMultiplier = 2F,
            activatedColor = Color.Black,
            angledSegmentEndsOf = angledSegmentEnds
        )
        Classic7SegmentDisplay(
            modifier = Modifier
                .background(Color.LightGray)
                .width(800.dp)
                .height(200.dp),
            thicknessMultiplier = 1.2F,
            gapSizeMultiplier = 2F,
            text = "89ABCDEF",
            activatedColor = Color.Black,
            angledSegmentEndsOf = angledSegmentEnds
        )
    }
}

@Preview(widthDp = 800)
@Composable
internal fun Classic7SegmentAsymmetricHexCharacters() {
    val angledSegmentEnds = createAsymmetricAngled7SegmentEndsFun()
    Column {
        Classic7SegmentDisplay(
            modifier = Modifier
                .background(Color.LightGray)
                .width(800.dp)
                .height(200.dp),
            text = "01234567",
            thicknessMultiplier = 1.25F,
            gapSizeMultiplier = 1F,
            activatedColor = Color.Black,
            angledSegmentEndsOf = angledSegmentEnds
        )
        Classic7SegmentDisplay(
            modifier = Modifier
                .background(Color.LightGray)
                .width(800.dp)
                .height(200.dp),
            thicknessMultiplier = 1.25F,
            gapSizeMultiplier = 1F,
            text = "89ABCDEF",
            activatedColor = Color.Black,
            angledSegmentEndsOf = angledSegmentEnds
        )
    }
}

// TODO: this is an 80's-looking, sharp-cornered display. It exposes a bug with
//  the center segment in that the center segment doesn't reach all the way to
//  the end. It also exposes an issue where, if you set the outer edge area to
//  1F, the segment disappears.
@Preview(widthDp = 800)
@Composable
internal fun Classic7SegmentSymmetricSharpAnglesHexCharacters() {
    val angledSegmentEnds: (Int) -> AngledSegmentEnds = { index ->
        when (index) {
            0 -> AngledSegmentEnds(
                innerEdgeLeftArea = 0F,
                innerEdgeRightArea = 0F,
                outerEdgeLeftArea = 0.99999F,
                outerEdgeRightArea = 0.99999F,
                leftIntersectionPct = 0.5F,
                rightIntersectionPct = 0.5F
            )
            1 -> AngledSegmentEnds(
                innerEdgeLeftArea = 0F,
                innerEdgeRightArea = 0F,
                outerEdgeLeftArea = 0.5F,
                outerEdgeRightArea = 0.99999F,
                leftIntersectionPct = 0F,
                rightIntersectionPct = 0.5F
            )
            2 -> AngledSegmentEnds(
                innerEdgeLeftArea = 0F,
                innerEdgeRightArea = 0F,
                outerEdgeLeftArea = 0.99999F,
                outerEdgeRightArea = 0.5F,
                leftIntersectionPct = 0.5F,
                rightIntersectionPct = 0F
            )
            // TODO: the below creates a weird shape
//            3 -> AngledSegmentEnds(
//                innerEdgeLeftArea = 0F,
//                innerEdgeRightArea = 0F,
//                outerEdgeLeftArea = 0.99999F,
//                outerEdgeRightArea = 0.99999F,
//                leftIntersectionPct = 0.5F,
//                rightIntersectionPct = 0.5F
//            )
            4 -> AngledSegmentEnds(
                innerEdgeLeftArea = 0F,
                innerEdgeRightArea = 0F,
                outerEdgeLeftArea = 0.99999F,
                outerEdgeRightArea = 0.5F,
                leftIntersectionPct = 0.5F,
                rightIntersectionPct = 0F
            )
            5 -> AngledSegmentEnds(
                innerEdgeLeftArea = 0F,
                innerEdgeRightArea = 0F,
                outerEdgeLeftArea = 0.5F,
                outerEdgeRightArea = 0.99999F,
                leftIntersectionPct = 0F,
                rightIntersectionPct = 0.5F
            )
            6 -> AngledSegmentEnds(
                innerEdgeLeftArea = 0F,
                innerEdgeRightArea = 0F,
                outerEdgeLeftArea = 0.99999F,
                outerEdgeRightArea = 0.99999F,
                leftIntersectionPct = 0.5F,
                rightIntersectionPct = 0.5F
            )
            else -> AngledSegmentEnds.EVEN
        }
    }
    Column {
        Classic7SegmentDisplay(
            modifier = Modifier
                .background(Color.LightGray)
                .width(800.dp)
                .height(200.dp),
            text = "01234567",
            thicknessMultiplier = 1F,
            gapSizeMultiplier = 2F,
            activatedColor = Color.Red,
            angledSegmentEndsOf = angledSegmentEnds
        )
        Classic7SegmentDisplay(
            modifier = Modifier
                .background(Color.LightGray)
                .width(800.dp)
                .height(200.dp),
            thicknessMultiplier = 1F,
            gapSizeMultiplier = 2F,
            text = "89ABCDEF",
            activatedColor = Color.Red,
            angledSegmentEndsOf = angledSegmentEnds
        )
    }
}