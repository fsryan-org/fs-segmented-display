package com.fsryan.ui.segments

fun translateHexToActiveSegments(char: Char): Int = when (char) {
    '0' -> 0b01110111
    '1' -> 0b00100100
    '2' -> 0b01011101
    '3' -> 0b01101101
    '4' -> 0b00101110
    '5' -> 0b01101011
    '6' -> 0b01111011
    '7' -> 0b00100101
    '8' -> 0b01111111
    '9' -> 0b01101111
    'A', 'a' -> 0b00111111
    'B', 'b' -> 0b01111010
    'C', 'c' -> 0b01010011
    'D', 'd' -> 0b01111100
    'E', 'e' -> 0b01011011
    'F', 'f' -> 0b00011011
    else -> 0   // <-- cannot render
}

fun createSymmetricHexagonal7SegmentParamsFun(
    outerLengthPct: Float = 1.3614F,
    innerLengthPct: Float = 1F,
    extThicknessVertPct: Float = 0.2852F,
    extThicknessHorizPct: Float = 0.7151F
): (idx: Int, leftTop: Boolean) -> HexagonalSegmentParams {
    val angled = HexagonalSegmentParams(
        outerLengthPct = outerLengthPct,
        innerLengthPct = innerLengthPct,
        extThicknessVertPct = extThicknessVertPct,
        extThicknessHorizPct = extThicknessHorizPct
    )
    val angledInverse = HexagonalSegmentParams(
        outerLengthPct = outerLengthPct,
        innerLengthPct = innerLengthPct,
        extThicknessVertPct = 1 - extThicknessVertPct,
        extThicknessHorizPct = extThicknessHorizPct
    )
    return { idx, leftTop ->
        when (idx) {
            0 -> angled
            1 -> if (leftTop) angled else HexagonalSegmentParams.EVEN
            2 -> if (leftTop) angledInverse else HexagonalSegmentParams.EVEN
            4 -> if (leftTop) HexagonalSegmentParams.EVEN else angled
            5 -> if (leftTop) HexagonalSegmentParams.EVEN else angledInverse
            6 -> angledInverse
            else -> HexagonalSegmentParams.EVEN
        }
    }
}
