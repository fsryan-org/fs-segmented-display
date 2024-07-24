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
fun DrawClassic7SegmentChar() {
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
                debuggingEnabled = true
            ) { index ->
                when (index) {
                    0 -> AngledSegmentEnds(
                        innerEdgeLeftArea = 0F,
                        innerEdgeRightArea = 0F,
                        outerEdgeLeftArea = 0.33F,
                        outerEdgeRightArea = 0.33F,
                        leftIntersectionPct = 1F,
                        rightIntersectionPct = 0F
                    )
                    1 -> AngledSegmentEnds(
                        innerEdgeLeftArea = 0F,
                        innerEdgeRightArea = 0F,
                        outerEdgeLeftArea = 0F,
                        outerEdgeRightArea = 0.33F,
                        leftIntersectionPct = 0.5F,
                        rightIntersectionPct = 0F
                    )
                    2 -> AngledSegmentEnds(
                        innerEdgeLeftArea = 0F,
                        innerEdgeRightArea = 0F,
                        outerEdgeLeftArea = 0.33F,
                        outerEdgeRightArea = 0F,
                        leftIntersectionPct = 1F,
                        rightIntersectionPct = 0.5F
                    )
                    3 -> AngledSegmentEnds(
                        innerEdgeLeftArea = 0F,
                        innerEdgeRightArea = 0F,
                        outerEdgeLeftArea = 0F,
                        outerEdgeRightArea = 0F,
                        leftIntersectionPct = 0.5F,
                        rightIntersectionPct = 0.5F
                    )
                    4 -> AngledSegmentEnds(
                        innerEdgeLeftArea = 0F,
                        innerEdgeRightArea = 0F,
                        outerEdgeLeftArea = 0.33F,
                        outerEdgeRightArea = 0F,
                        leftIntersectionPct = 0F,
                        rightIntersectionPct = 0.5F
                    )
                    5 -> AngledSegmentEnds(
                        innerEdgeLeftArea = 0F,
                        innerEdgeRightArea = 0F,
                        outerEdgeLeftArea = 0F,
                        outerEdgeRightArea = 0.33F,
                        leftIntersectionPct = 0.5F,
                        rightIntersectionPct = 1F
                    )
                    6 -> AngledSegmentEnds(
                        innerEdgeLeftArea = 0F,
                        innerEdgeRightArea = 0F,
                        outerEdgeLeftArea = 0.33F,
                        outerEdgeRightArea = 0.33F,
                        leftIntersectionPct = 1F,
                        rightIntersectionPct = 0F
                    )
                    else -> error("Invalid segment index: $index")
                }
            }
        }
    }
}