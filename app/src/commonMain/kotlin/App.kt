import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.fsryan.ui.segments.Hexagonal7SegmentDisplay
import com.fsryan.ui.segments.HexagonalSegmentParams
import com.fsryan.ui.segments.classic7AsymmetricParamsFun
import com.fsryan.ui.segments.classic7SymmetricParamsFun
import com.fsryan.ui.segments.evenFun
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

            val drawDebugLinesState = remember { mutableStateOf(false) }

            val hexagonalSegmentParamsState = remember {
                mutableStateOf("Classic Asymmetric" to HexagonalSegmentParams.classic7AsymmetricParamsFun())
            }
            val shearPctState = remember {
                mutableFloatStateOf(0F)
            }
            val thicknessMultiplierState = remember {
                mutableFloatStateOf(1F)
            }
            val topHeightPercentageState = remember {
                mutableFloatStateOf(0.495F)
            }

            val gapSizeMultiplierState = remember { mutableFloatStateOf(1F) }
            Column(modifier = Modifier.fillMaxWidth()) {
                ShowDisplays(
                    modifier = Modifier.weight(1F),
                    charWidth = charWidth,
                    charHeight = charHeight,
                    activatedColor = Color.Red,
                    gapSizeMultiplier = gapSizeMultiplierState.value,
                    hexagonalSegmentParams = hexagonalSegmentParamsState.value.second,
                    shearPct = shearPctState.value,
                    thicknessMultiplier = thicknessMultiplierState.value,
                    topHeightPercentage = topHeightPercentageState.value,
                    drawDebugLines = drawDebugLinesState.value
                )
                ControlAssembly(
                    modifier = Modifier.fillMaxWidth().weight(1F),
                    shearPctState = shearPctState,
                    thicknessMultiplierState = thicknessMultiplierState,
                    gapSizeMultiplierState = gapSizeMultiplierState,
                    topHeightPercentageState = topHeightPercentageState,
                    drawDebugLinesState = drawDebugLinesState,
                    hexagonalSegmentParamsState = hexagonalSegmentParamsState
                )
            }
        }
    }
}

@Composable
fun ShowDisplays(
    modifier: Modifier = Modifier,
    charWidth: Dp,
    charHeight: Dp,
    gapSizeMultiplier: Float,
    hexagonalSegmentParams: (index: Int, leftTop: Boolean) -> HexagonalSegmentParams,
    shearPct: Float,
    activatedColor: Color,
    thicknessMultiplier: Float,
    topHeightPercentage: Float,
    drawDebugLines: Boolean
) {
    Column(
        modifier = modifier.fillMaxWidth()
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
                gapSizeMultiplier = gapSizeMultiplier,
                hexagonalSegmentParams = hexagonalSegmentParams,
                shearPct = shearPct,
                thicknessMultiplier = thicknessMultiplier,
                topHeightPercentage = topHeightPercentage,
                debuggingEnabled = drawDebugLines
            )
        }
        Box(
            modifier = Modifier.width(8 * charWidth)
                .height(charHeight)
        ) {
            Hexagonal7SegmentDisplay(
                text = "89ABCDEF",
                activatedColor = activatedColor,
                gapSizeMultiplier = gapSizeMultiplier,
                hexagonalSegmentParams = hexagonalSegmentParams,
                shearPct = shearPct,
                thicknessMultiplier = thicknessMultiplier,
                topHeightPercentage = topHeightPercentage,
                debuggingEnabled = drawDebugLines
            )
        }
    }
}

@Composable
fun ControlAssembly(
    modifier: Modifier = Modifier,
    shearPctState: MutableFloatState,
    thicknessMultiplierState: MutableFloatState,
    gapSizeMultiplierState: MutableFloatState,
    topHeightPercentageState: MutableFloatState,
    drawDebugLinesState: MutableState<Boolean>,
    hexagonalSegmentParamsState: MutableState<Pair<String, (idx: Int, leftTop: Boolean) -> HexagonalSegmentParams>>
) {
    Column(
        modifier = modifier.padding(all = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    drawDebugLinesState.value = !drawDebugLinesState.value
                }
            ) {
                Text("Debug Lines: ${if (drawDebugLinesState.value) "ON" else "OFF"}")
            }
            HexagonalSegmentParamsPresetDropDown(hexagonalSegmentParamsState)
            Button(
                onClick = {
                    shearPctState.value = 0F
                    thicknessMultiplierState.value = 1F
                    gapSizeMultiplierState.value = 1F
                    topHeightPercentageState.value = .495F
                    drawDebugLinesState.value = false
                    hexagonalSegmentParamsState.value = "Classic Asymmetric" to HexagonalSegmentParams.classic7AsymmetricParamsFun()
                }
            ) {
                Text("Restore Defaults")
            }
        }
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
        FloatValueSliderControl(
            valueRange = 0F .. 10F,
            steps = 1000,
            state = gapSizeMultiplierState
        ) { value ->
            "Gap Size Multiplier: ${value.roundToDecimals(3)}"
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HexagonalSegmentParamsPresetDropDown(
    hexagonalSegmentParamsState: MutableState<Pair<String, (idx: Int, leftTop: Boolean) -> HexagonalSegmentParams>>
) {
    var isExpanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        modifier = Modifier,
        expanded = isExpanded,
        onExpandedChange = { newValue ->
            isExpanded = newValue
        }
    ) {
        TextField(
            modifier = Modifier.menuAnchor(),
            value = hexagonalSegmentParamsState.value.first,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(isExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                isExpanded = false
            }
        ) {
            sequenceOf(
                "Classic Symmetric Uneven" to HexagonalSegmentParams.classic7SymmetricParamsFun(),
                "Classic Symmetric Even" to HexagonalSegmentParams.evenFun(),
                "Classic Asymmetric" to HexagonalSegmentParams.classic7AsymmetricParamsFun()
            ).forEach { pair ->
                DropdownMenuItem(
                    text = {
                        Text(pair.first)
                    },
                    onClick = {
                        isExpanded = false
                        hexagonalSegmentParamsState.value = pair
                    }
                )
            }
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