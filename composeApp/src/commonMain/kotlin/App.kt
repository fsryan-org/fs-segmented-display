import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.fsryan.ui.segments.AngledSegmentEnds
import com.fsryan.ui.segments.Classic7SegmentDisplay
import com.fsryan.ui.segments.createSymmetricAngled7SegmentEndsFun
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt

@Composable
@Preview
fun App() {
    MaterialTheme {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val debuggingEnabledState = remember { mutableStateOf(false) }
            val angledSegmentEndsFunState = remember {
                mutableStateOf("Classic Symmetric" to createSymmetricAngled7SegmentEndsFun())
            }
            val thicknessMultiplierState = remember { mutableFloatStateOf(1F) }
            val topAreaPercentageState = remember { mutableFloatStateOf(0.495F) }
            val gapSizeMultiplierState = remember { mutableFloatStateOf(1F) }
            val activatedColorState = remember { mutableStateOf(Color.Red) }
            ShowDisplays(
                modifier = Modifier.weight(2F),
                gapSizeMultiplier = gapSizeMultiplierState.value,
                angledSegmentEnds = angledSegmentEndsFunState.value.second,
                topAreaPercentage = topAreaPercentageState.value,
                activatedColor = activatedColorState.value,
                thicknessMultiplier = thicknessMultiplierState.value,
                debuggingEnabled = debuggingEnabledState.value
            )
            ControlAssembly(
                modifier = Modifier.weight(1F),
                thicknessMultiplierState = thicknessMultiplierState,
                gapSizeMultiplierState = gapSizeMultiplierState,
                topAreaPercentageState = topAreaPercentageState,
                activatedColorState = activatedColorState,
                debuggingEnabledState = debuggingEnabledState,
                angledSegmentEndsFunState = angledSegmentEndsFunState
            )
        }
    }
}

@Composable
fun ShowDisplays(
    modifier: Modifier,
    gapSizeMultiplier: Float,
    angledSegmentEnds: (index: Int) -> AngledSegmentEnds,
    topAreaPercentage: Float,
    activatedColor: Color,
    thicknessMultiplier: Float,
    debuggingEnabled: Boolean
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val width = maxWidth
        Column(
            modifier = Modifier.fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .background(Color.LightGray),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Classic7SegmentDisplay(
                // aspect ratio of each character is 1:2
                modifier = Modifier.fillMaxWidth().height(width / 4),
                text = "01234567",
                topAreaPercentage = topAreaPercentage,
                thicknessMultiplier = thicknessMultiplier,
                gapSizeMultiplier = gapSizeMultiplier,
                activatedColor = activatedColor,
                debuggingEnabled = debuggingEnabled,
                angledSegmentEndsOf = angledSegmentEnds
            )
            Classic7SegmentDisplay(
                // aspect ratio of each character is 1:2
                modifier = Modifier.fillMaxWidth().height(width / 4),
                topAreaPercentage = topAreaPercentage,
                thicknessMultiplier = thicknessMultiplier,
                gapSizeMultiplier = gapSizeMultiplier,
                text = "89ABCDEF",
                activatedColor = activatedColor,
                debuggingEnabled = debuggingEnabled,
                angledSegmentEndsOf = angledSegmentEnds
            )
        }
    }
}

@Composable
fun ControlAssembly(
    modifier: Modifier = Modifier,
    thicknessMultiplierState: MutableFloatState,
    gapSizeMultiplierState: MutableFloatState,
    topAreaPercentageState: MutableFloatState,
    activatedColorState: MutableState<Color>,
    debuggingEnabledState: MutableState<Boolean>,
    angledSegmentEndsFunState: MutableState<Pair<String, (Int) -> AngledSegmentEnds>>
) {
    Column(
        modifier = modifier.fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ColorSliders(activatedColorState = activatedColorState)
        FloatValueSliderControl(
            valueRange = 0F .. 1F,
            steps = 1000,
            state = topAreaPercentageState
        ) { value ->
            "Top Section: ${value.roundToDecimals(3) * 100}%"
        }
        FloatValueSliderControl(
            valueRange = 0F .. 2F,
            steps = 2000,
            state = thicknessMultiplierState
        ) { value ->
            "Thickness Multiplier: ${value.roundToDecimals(3)}"
        }
        FloatValueSliderControl(
            valueRange = 0F .. 10F,
            steps = 1000,
            state = gapSizeMultiplierState
        ) { value ->
            "Gap Size Multiplier: ${value.roundToDecimals(3)}"
        }
    }
}

@Composable
fun ColorSliders(activatedColorState: MutableState<Color>) {
    val redState = remember { mutableFloatStateOf(activatedColorState.value.red) }
    val greenState = remember { mutableFloatStateOf(activatedColorState.value.green) }
    val blueState = remember { mutableFloatStateOf(activatedColorState.value.blue) }
    Column(
        modifier = Modifier.fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.medium)
            .padding(all = 8.dp)
    ) {
        Text(
            text = "Colors",
            textDecoration = TextDecoration.Underline
        )
        FloatValueSliderControl(
            valueRange = 0F .. 1F,
            steps = 1000,
            state = redState
        ) { value ->
            "Red: ${value.roundToDecimals(3)}"
        }
        FloatValueSliderControl(
            valueRange = 0F .. 1F,
            steps = 1000,
            state = greenState
        ) { value ->
            "Green: ${value.roundToDecimals(3)}"
        }
        FloatValueSliderControl(
            valueRange = 0F .. 1F,
            steps = 1000,
            state = blueState
        ) { value ->
            "Blue: ${value.roundToDecimals(3)}"
        }
    }
    activatedColorState.value = Color(redState.value, greenState.value, blueState.value)
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