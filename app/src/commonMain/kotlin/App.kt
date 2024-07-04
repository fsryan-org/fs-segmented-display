import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.fsryan.ui.segments.Hexagonal7SegmentDisplay
import com.fsryan.ui.segments.HexagonalSegmentParams
import com.fsryan.ui.segments.classic7AsymmetricParamsFun
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt

@Composable
@Preview
fun App() {
    MaterialTheme {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            val charWidth = maxWidth / 8
            val charHeight = charWidth * 2
            val hexagonalSegmentParamsState = remember {
                mutableStateOf(HexagonalSegmentParams.classic7AsymmetricParamsFun())
            }
            val shearPctState = remember {
                mutableFloatStateOf(0F)
            }
            val thicknessMultiplierState = remember {
                mutableFloatStateOf(1F)
            }
            val topHeightPercentageState = remember {
                mutableFloatStateOf(105F / 212)
            }

            val density = LocalDensity.current.density
            val gapSizeState = remember { mutableFloatStateOf(3 * density) }
            Column(modifier = Modifier.fillMaxWidth()) {
                ShowDisplays(
                    charWidth = charWidth,
                    charHeight = charHeight,
                    activatedColor = Color.Red,
                    gapSize = (gapSizeState.value / density).dp,
                    hexagonalSegmentParams = hexagonalSegmentParamsState.value,
                    shearPct = shearPctState.value,
                    thicknessMultiplier = thicknessMultiplierState.value,
                    topHeightPercentage = topHeightPercentageState.value
                )
                ControlAssembly(
                    modifier = Modifier.fillMaxWidth(),
                    shearPctState = shearPctState,
                    thicknessMultiplierState = thicknessMultiplierState,
                    gapSizePxState = gapSizeState,
                    topHeightPercentageState = topHeightPercentageState
                )
            }
        }
    }
}

@Composable
fun ShowDisplays(
    charWidth: Dp,
    charHeight: Dp,
    gapSize: Dp,
    hexagonalSegmentParams: (index: Int, leftTop: Boolean) -> HexagonalSegmentParams,
    shearPct: Float,
    activatedColor: Color,
    thicknessMultiplier: Float,
    topHeightPercentage: Float
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.width(8 * charWidth)
                .height(charHeight)
        ) {
            Hexagonal7SegmentDisplay(
                text = "01234567",
                activatedColor = activatedColor,
                gapSize = gapSize,
                hexagonalSegmentParams = hexagonalSegmentParams,
                shearPct = shearPct,
                thicknessMultiplier = thicknessMultiplier,
                topHeightPercentage = topHeightPercentage
            )
        }
        Box(
            modifier = Modifier.width(8 * charWidth)
                .height(charHeight)
        ) {
            Hexagonal7SegmentDisplay(
                text = "89ABCDEF",
                activatedColor = activatedColor,
                gapSize = gapSize,
                hexagonalSegmentParams = hexagonalSegmentParams,
                shearPct = shearPct,
                thicknessMultiplier = thicknessMultiplier,
                topHeightPercentage = topHeightPercentage
            )
        }
    }
}

@Composable
fun ControlAssembly(
    modifier: Modifier = Modifier,
    shearPctState: MutableFloatState,
    thicknessMultiplierState: MutableFloatState,
    gapSizePxState: MutableFloatState,
    topHeightPercentageState: MutableFloatState
) {
    Column(modifier = modifier.padding(horizontal = 16.dp)) {
        FloatValueSliderControl(
            valueRange = -1F .. 1F,
            steps = 2000,
            state = shearPctState
        ) { value ->
            "Shear: ${value.roundToDecimals(3) * 100}%"

        }
        FloatValueSliderControl(
            valueRange = 0F .. 2F,
            steps = 2000,
            state = thicknessMultiplierState
        ) { value ->
            "Thickness Multiplier: ${value.roundToDecimals(3)}"
        }

        val density = LocalDensity.current.density
        FloatValueSliderControl(
            valueRange = 0F .. 20F,
            steps = 2000,
            state = gapSizePxState
        ) { value ->
            "Gap Size (dp): ${(value / density).roundToDecimals(3)}"
        }
        FloatValueSliderControl(
            valueRange = 0F .. 1F,
            steps = 1000,
            state = topHeightPercentageState
        ) {
            value ->
            "Top Section: ${value.roundToDecimals(3) * 100}%"
        }
    }
}

@Composable
fun FloatValueSliderControl(
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    state: MutableFloatState,
    renderLabel: (value: Float) -> String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        var label by remember { mutableStateOf(renderLabel(state.value)) }
        Text(modifier = Modifier.weight(1F), text = label)
        Slider(
            modifier = Modifier.weight(3F, fill = true),
            value = state.value,
            valueRange = valueRange,
            onValueChange = { newValue ->
                state.value = newValue
            },
            steps = steps,
            onValueChangeFinished = {
                label = renderLabel(state.value)
            }
        )
    }
}

private fun Float.roundToDecimals(decimals: Int): Float {
    var dotAt = 1
    repeat(decimals) { dotAt *= 10 }
    val roundedValue = (this * dotAt).roundToInt()
    return (roundedValue / dotAt) + (roundedValue % dotAt).toFloat() / dotAt
}