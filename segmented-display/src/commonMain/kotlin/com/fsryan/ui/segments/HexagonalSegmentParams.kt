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

/**
 * Creates a [HexagonalSegmentParams] instance
 */
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

/**
 * Creates a function that will properly configure each of the 14 tips of a
 * 7-segment display symmetrically following the classic 7-segment display
 * example.
 */
fun HexagonalSegmentParams.Companion.classic7SymmetricParamsFun(
    outerLengthPct: Float = 1.3F,
    innerLengthPct: Float = 1F,
    extThicknessVertPct: Float = 0.3125F,
    extThicknessHorizPct: Float = 1 - extThicknessVertPct
): (idx: Int, leftTop: Boolean) -> HexagonalSegmentParams {
    val angled = HexagonalSegmentParams(
        outerLengthPct = outerLengthPct,
        innerLengthPct = innerLengthPct,
        extThicknessVertPct = extThicknessVertPct,
        extThicknessHorizPct = extThicknessHorizPct
    )
    return { idx, leftTop ->
        when (idx) {
            0 -> angled
            1 -> if (leftTop) angled else EVEN
            2 -> if (leftTop) angled else EVEN
            4 -> if (leftTop) EVEN else angled
            5 -> if (leftTop) EVEN else angled
            6 -> angled
            else -> EVEN
        }
    }
}

fun HexagonalSegmentParams.Companion.evenFun(): (idx: Int, leftTop: Boolean) -> HexagonalSegmentParams {
    return { _, _ -> EVEN }
}

fun HexagonalSegmentParams.Companion.classic7AsymmetricParamsFun(
    sharpAngleOuterLengthPct: Float = 1.2F,
    sharpAngleExtThicknessVertPct: Float = 0.6F
): (idx: Int, leftTop: Boolean) -> HexagonalSegmentParams {
    val sharpAngle = HexagonalSegmentParams(
        outerLengthPct = sharpAngleOuterLengthPct,
        innerLengthPct = 1F,
        extThicknessVertPct = sharpAngleExtThicknessVertPct,
        extThicknessHorizPct = 1F
    )
    val pentagonalTip = HexagonalSegmentParams(
        outerLengthPct = 1F,
        innerLengthPct = 1F,
        extThicknessVertPct = 0F,
        extThicknessHorizPct = 1 - sharpAngleExtThicknessVertPct
    )
    return { idx, leftTop ->
        when (idx) {
            0, 6 -> if (leftTop) sharpAngle else pentagonalTip
            1 -> if (leftTop) pentagonalTip else EVEN
            2 -> if (leftTop) sharpAngle else EVEN
            4 -> if (leftTop) EVEN else pentagonalTip
            5 -> if (leftTop) EVEN else sharpAngle
            else -> EVEN
        }
    }
}

private data class HexagonalSegmentParamsData(
    override val outerLengthPct: Float,
    override val innerLengthPct: Float,
    override val extThicknessVertPct: Float,
    override val extThicknessHorizPct: Float
): HexagonalSegmentParams