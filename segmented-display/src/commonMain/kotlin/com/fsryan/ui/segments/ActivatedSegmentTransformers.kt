package com.fsryan.ui.segments

/**
 * A basic transformation function that translates a character to the active
 * segments that can be used for determining the segments of a 7-segment
 * display that are active. The active segments are described withing the 7
 * least significant bits of the returned integer and are mapped as follows
 * (where the number shown be low is the index of the bit from least to most
 * significant):
 *
 * ```
 *  1111
 * 2    3
 * 2    3
 * 2    3
 *  4444
 * 5    6
 * 5    6
 * 5    6
 *  7777
 * ```
 *
 * > *Note*:
 * > If the character is not in 0-9a-zA-Z\-, then 0 will be returned
 *
 * @param char the character to translate
 * @return the active segments for the character
 * @author fsryan
 */
fun transformCharToActiveSegments(char: Char): Int = when (char) {
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