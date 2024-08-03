package com.fsryan.ui.segments

import kotlin.js.JsName

/**
 * Contains the properties used to style the ends of an angled segment. This
 * works well on segments that are rectangular, pentagonal, or hexagonal,
 * however, in some situations, it could be used for septagonal or octagonal
 * segments.
 *
 * @see Classic7SegmentDisplay for an example of where this is used
 * @see createSymmetricAngled7SegmentEndsFun for a function that creates
 * [AngledSegmentEnds] for a symmetric 7-segment display
 * @see createAsymmetricAngled7SegmentEndsFun for a function that creates
 * [AngledSegmentEnds] for an asymmetric 7-segment display
 * @author fsryan
 */
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
        /**
         * Completely even segment ends where the inner and outer edges do not
         * extend into the right or left areas.
         */
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

/**
 * A fake constructor for [AngledSegmentEnds] that is used to create an
 * instance of [AngledSegmentEnds] with the given properties.
 *
 * @param innerEdgeLeftArea sets [AngledSegmentEnds.innerEdgeLeftArea]
 * @param innerEdgeRightArea sets [AngledSegmentEnds.innerEdgeRightArea]
 * @param outerEdgeLeftArea sets [AngledSegmentEnds.outerEdgeLeftArea]
 * @param outerEdgeRightArea sets [AngledSegmentEnds.outerEdgeRightArea]
 * @param leftIntersectionPct sets [AngledSegmentEnds.leftIntersectionPct]
 * @param rightIntersectionPct sets [AngledSegmentEnds.rightIntersectionPct]
 *
 * @return an instance of [AngledSegmentEnds] with the given properties
 */
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

/**
 * A function that returns [AngledSegmentEnds] for a symmetric 7-segment
 * display where all segments are completely even.
 *
 * @return [AngledSegmentEnds.EVEN]
 */
fun symmetricEvenAngledSegmentEnds(index: Int): AngledSegmentEnds = AngledSegmentEnds.EVEN

/**
 * A function that creates a function which returns [AngledSegmentEnds] for a
 * symmetric 7-segment display.
 *
 * > *Note*:
 * > If you pass in 0.5F for `outerEdgeArea`, then the function created will
 * > be exactly [symmetricEvenAngledSegmentEnds]
 *
 * @param outerEdgeArea the percentage of the open area on the left and right
 * sides of the top and bottom segments that should be taken by the outer edge
 *
 * @return a function that returns [AngledSegmentEnds] for a symmetric
 * 7-segment display
 */
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

/**
 * A function that creates a function which returns [AngledSegmentEnds] for an
 * asymmetric 7-segment display.
 *
 * @param outerEdgeArea the percentage of the open area on the left and right
 * for a segment that takes up more of the outer edge area than the inner edge
 * area
 * @param extremeIntersectionPct the percentage of the distance between the
 * outer right/left vertex of a segment and the outer left/right vertex of an
 * adjacent segment that the line of intersection between two segments should
 * be drawn. this affects the [AngledSegmentEnds.leftIntersectionPct] and
 * [AngledSegmentEnds.rightIntersectionPct] for some of the adjacent segments
 *
 * @return a function that returns [AngledSegmentEnds] for an asymmetric
 * 7-segment display
 */
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