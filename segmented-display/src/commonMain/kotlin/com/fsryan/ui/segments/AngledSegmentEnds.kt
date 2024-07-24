package com.fsryan.ui.segments

import kotlin.js.JsName

interface AngledSegmentEnds {
    /**
     * The percentage of the open area on the left side a normal rectangular
     * segment that should be taken by the inner edge of this segment.
     *
     * > *Note*: The convention for any middle horizontal segment is that
     * "inner edge" is the bottom edge and the "outer edge" is the top edge.
     */
    val innerEdgeLeftArea: Float

    /**
     * The percentage of the open area on the right side a normal rectangular
     * segment that should be taken by the inner edge of this segment.
     *
     * > *Note*: The convention for any middle horizontal segment is that
     * "inner edge" is the bottom edge and the "outer edge" is the top edge.
     */
    val innerEdgeRightArea: Float

    /**
     * The percentage of the open area on the left side a normal rectangular
     * segment that should be taken by the outer edge of this segment.
     *
     * > *Note*: The convention for any middle horizontal segment is that
     * "inner edge" is the bottom edge and the "outer edge" is the top edge.
     */
    val outerEdgeLeftArea: Float

    /**
     * The percentage of the open area on the right side a normal rectangular
     * segment that should be taken by the outer edge of this segment.
     *
     * > *Note*: The convention for any middle horizontal segment is that
     * "inner edge" is the bottom edge and the "outer edge" is the top edge.
     */
    val outerEdgeRightArea: Float

    /**
     * The percentage of the distance between between the outer left vertex of
     * this segment and the outer right vertex of an adjacent segment that the
     * line of intersection between two segments should be drawn.
     *
     * > *Note*: This value should be between 0 and 1
     *
     * > *Note*: The adjacent [AngledSegmentEnds] should have a
     * [rightIntersectionPct] of 1 - [leftIntersectionPct] to avoid
     * overlapping segments
     */
    val leftIntersectionPct: Float

    /**
     * The percentage of the distance between between the outer right vertex of
     * this segment and the outer left vertex of an adjacent segment that the
     * line of intersection between two segments should be drawn.
     *
     * > *Note*: This value should be between 0 and 1
     *
     * > *Note*: The adjacent [AngledSegmentEnds] should have a
     * [leftIntersectionPct] of 1 - [rightIntersectionPct] to avoid
     * overlapping segments
     *
     */
    val rightIntersectionPct: Float

    companion object {
        val EVEN = AngledSegmentEnds(
            innerEdgeLeftArea = 0F,
            innerEdgeRightArea = 0F,
            outerEdgeLeftArea = 0F,
            outerEdgeRightArea = 0F,
            leftIntersectionPct = 0.5F,
            rightIntersectionPct = 0.5F
        )
    }
}





@JsName("createAngledSegmentEnd")
fun AngledSegmentEnds(
    innerEdgeLeftArea: Float,
    innerEdgeRightArea: Float,
    outerEdgeLeftArea: Float,
    outerEdgeRightArea: Float,
    leftIntersectionPct: Float,
    rightIntersectionPct: Float
): AngledSegmentEnds = AngledSegmentEndsData(
    innerEdgeLeftArea = innerEdgeLeftArea,
    innerEdgeRightArea = innerEdgeRightArea,
    outerEdgeLeftArea = outerEdgeLeftArea,
    outerEdgeRightArea = outerEdgeRightArea,
    leftIntersectionPct = leftIntersectionPct,
    rightIntersectionPct = rightIntersectionPct
)

private data class AngledSegmentEndsData(
    override val innerEdgeLeftArea: Float,
    override val innerEdgeRightArea: Float,
    override val outerEdgeLeftArea: Float,
    override val outerEdgeRightArea: Float,
    override val leftIntersectionPct: Float,
    override val rightIntersectionPct: Float
): AngledSegmentEnds