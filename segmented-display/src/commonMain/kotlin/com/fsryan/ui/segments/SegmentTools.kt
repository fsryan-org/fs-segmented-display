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