package com.fsryan.ui.segments

/**
 * Describes how the typical rectangle is adjusted into a hexagon on one end.
 * There should be 14 of these per character
 */
interface HexagonalSegmentParams {
    /**
     * The percent of the length of a typical rectangular segment that should
     * be used for the _OUTER_ length of the hexagonal segment. The outer
     * length is closer to the outside of the character. When the segment is
     * the middle horizontal segment, it is the
     */
    val outerLengthPct: Float

    /**
     * The percent of the length of a typical rectangular segment that should
     * be used for the _INNER_ length of the hexagonal segment. The inner
     * length is closer to the inside of the character.
     */
    val innerLengthPct: Float

    /**
     * The percent of the extended area, oriented such that the vertical
     * direction is relative to the thickness of the segment and the horizontal
     * direction is perpendicular to the thickness of the segment, this
     * controls how far toward the vertical that the hexagon extends.
     */
    val extThicknessVertPct: Float

    /**
     * The percent of the extended area, oriented such that the horizontal
     * direction is perpendicular to the thickness of the segment and the
     * vertical direction is relative to the thickness of the segment, this
     * controls how far toward the horizontal that the hexagon extends.
     */
    val extThicknessHorizPct: Float

    companion object {
        val EVEN = HexagonalSegmentParams(
            outerLengthPct = 1F,
            innerLengthPct = 1F,
            extThicknessVertPct = 0.5F,
            extThicknessHorizPct = 0.5F
        )
    }
}

fun HexagonalSegmentParams(
    outerLengthPct: Float,
    innerLengthPct: Float,
    extThicknessVertPct: Float,
    extThicknessHorizPct: Float
): HexagonalSegmentParams = HexagonalSegmentParamsData(
    outerLengthPct = outerLengthPct,
    innerLengthPct = innerLengthPct,
    extThicknessVertPct = extThicknessVertPct,
    extThicknessHorizPct = extThicknessHorizPct
)

private data class HexagonalSegmentParamsData(
    override val outerLengthPct: Float,
    override val innerLengthPct: Float,
    override val extThicknessVertPct: Float,
    override val extThicknessHorizPct: Float
): HexagonalSegmentParams