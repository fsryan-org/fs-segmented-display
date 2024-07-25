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
     * The percentage of the distance between the outer left vertex of this
     * segment and the outer right vertex of an adjacent segment that the
     * boundary between two segments should be drawn. The best way to
     * understand the `leftIntersectionPct` is to imagine the segment without
     * gaps. In the no-gaps case, this is the line where this segment starts
     * and the adjacent segment ends.
     *
     * > *Note*: This value should be between 0 and 1
     *
     * > *Note*: The adjacent [AngledSegmentEnds] should have a
     * [rightIntersectionPct] of 1 - [leftIntersectionPct] to avoid
     * overlapping segments
     */
    val leftIntersectionPct: Float

    /**
     * The percentage of the distance between the outer right vertex of this
     * segment and the outer left vertex of an adjacent segment that the
     * line of intersection between two segments should be drawn. The best way
     * to understand the `rightIntersectionPct` is to imagine the segment
     * without gaps. In the no-gaps case, this is the line where this segment
     * ends and the adjacent segment starts.
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

fun symmetricEvenAngledSegmentEnds(index: Int): AngledSegmentEnds = AngledSegmentEnds.EVEN

fun createSymmetricAngled7SegmentEndsFun(outerEdgeArea: Float = 0.33F): (Int) -> AngledSegmentEnds {
    if (outerEdgeArea == 0F) {
        return ::symmetricEvenAngledSegmentEnds
    }

    val topBottomHorizontal = AngledSegmentEnds(
        innerEdgeLeftArea = 0F,
        innerEdgeRightArea = 0F,
        outerEdgeLeftArea = outerEdgeArea,
        outerEdgeRightArea = outerEdgeArea,
        leftIntersectionPct = 0.5F,
        rightIntersectionPct = 0.5F
    )
    val topLeftBottomRightVertical = AngledSegmentEnds(
        innerEdgeLeftArea = 0F,
        innerEdgeRightArea = 0F,
        outerEdgeLeftArea = 0F,
        outerEdgeRightArea = outerEdgeArea,
        leftIntersectionPct = 0.5F,
        rightIntersectionPct = 0.5F
    )
    val topRightBottomLeftVertical = AngledSegmentEnds(
        innerEdgeLeftArea = 0F,
        innerEdgeRightArea = 0F,
        outerEdgeLeftArea = outerEdgeArea,
        outerEdgeRightArea = 0F,
        leftIntersectionPct = 0.5F,
        rightIntersectionPct = 0.5F
    )
    return { index ->
        when (index) {
            0, 6 -> topBottomHorizontal
            1, 5 -> topLeftBottomRightVertical
            2, 4 -> topRightBottomLeftVertical
            else -> AngledSegmentEnds.EVEN
        }
    }
}

fun createAsymmetricAngled7SegmentEndsFun(
    outerEdgeArea: Float = 0.33F,
    extremeIntersectionPct: Float = 1F
): (Int) -> AngledSegmentEnds {
    val topBottomHorizontal = AngledSegmentEnds(
        innerEdgeLeftArea = 0F,
        innerEdgeRightArea = 0F,
        outerEdgeLeftArea = outerEdgeArea,
        outerEdgeRightArea = outerEdgeArea,
        leftIntersectionPct = extremeIntersectionPct,
        rightIntersectionPct = 1 - extremeIntersectionPct
    )
    val topLeftVertical = AngledSegmentEnds(
        innerEdgeLeftArea = 0F,
        innerEdgeRightArea = 0F,
        outerEdgeLeftArea = 0F,
        outerEdgeRightArea = outerEdgeArea,
        leftIntersectionPct = 0.5F,
        rightIntersectionPct = 1 - extremeIntersectionPct
    )
    val topRightVertical = AngledSegmentEnds(
        innerEdgeLeftArea = 0F,
        innerEdgeRightArea = 0F,
        outerEdgeLeftArea = outerEdgeArea,
        outerEdgeRightArea = 0F,
        leftIntersectionPct = extremeIntersectionPct,
        rightIntersectionPct = 0.5F
    )
    val bottomLeftVertical = AngledSegmentEnds(
        innerEdgeLeftArea = 0F,
        innerEdgeRightArea = 0F,
        outerEdgeLeftArea = outerEdgeArea,
        outerEdgeRightArea = 0F,
        leftIntersectionPct = 1 - extremeIntersectionPct,
        rightIntersectionPct = 0.5F
    )
    val bottomRightVertical = AngledSegmentEnds(
        innerEdgeLeftArea = 0F,
        innerEdgeRightArea = 0F,
        outerEdgeLeftArea = 0F,
        outerEdgeRightArea = outerEdgeArea,
        leftIntersectionPct = 0.5F,
        rightIntersectionPct = extremeIntersectionPct
    )
    return { index ->
        when (index) {
            0, 6 -> topBottomHorizontal
            1 -> topLeftVertical
            2 -> topRightVertical
            4 -> bottomLeftVertical
            5 -> bottomRightVertical
            else -> AngledSegmentEnds.EVEN
        }
    }
}

private data class AngledSegmentEndsData(
    override val innerEdgeLeftArea: Float,
    override val innerEdgeRightArea: Float,
    override val outerEdgeLeftArea: Float,
    override val outerEdgeRightArea: Float,
    override val leftIntersectionPct: Float,
    override val rightIntersectionPct: Float
): AngledSegmentEnds