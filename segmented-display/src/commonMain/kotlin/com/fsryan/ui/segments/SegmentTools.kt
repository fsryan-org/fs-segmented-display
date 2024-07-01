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
    'G', 'g' -> 0b01110011
    'H', 'h' -> 0b00111010
    'I', 'i' -> 0b00010010
    'J', 'j' -> 0b01100100
    'K', 'k' -> 0b00111011
    'L', 'l' -> 0b01010010
    'M', 'm' -> 0b00111001
    'N', 'n' -> 0b00111000
    'O', 'o' -> 0b01111000
    'P', 'p' -> 0b00011111
    'Q', 'q' -> 0b00101111
    'R', 'r' -> 0b00011000
    'S', 's' -> 0b01100011
    'T', 't' -> 0b01011010
    'U', 'u' -> 0b01110000
    'V', 'v' -> 0b01000110
    'W', 'w' -> 0b01001110
    'X', 'x' -> 0b00111110
    'Y', 'y' -> 0b01101110
    'Z', 'z' -> 0b01010101
    '-' -> 0b00001000
    else -> 0   // <-- cannot render
}

fun createSymmetricHexagonal7SegmentParamsFun(
    outerLengthPct: Float = 1.375F,
    innerLengthPct: Float = 1F,
    extThicknessVertPct: Float = 0.25F,
    extThicknessHorizPct: Float = 0.75F
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
            1 -> if (leftTop) angled else HexagonalSegmentParams.EVEN
            2 -> if (leftTop) angled else HexagonalSegmentParams.EVEN
            4 -> if (leftTop) HexagonalSegmentParams.EVEN else angled
            5 -> if (leftTop) HexagonalSegmentParams.EVEN else angled
            6 -> angled
            else -> HexagonalSegmentParams.EVEN
        }
    }
}
