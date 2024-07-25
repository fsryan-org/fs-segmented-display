import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.fsryan.ui.segments.AngledSegmentEnds
import com.fsryan.ui.segments.Classic7SegmentDisplay
import com.fsryan.ui.segments.createSymmetricAngled7SegmentEndsFun
import org.jetbrains.compose.ui.tooling.preview.Preview

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
                gapSizeMultiplier = gapSizeMultiplierState.value,
                angledSegmentEnds = angledSegmentEndsFunState.value.second,
                topAreaPercentage = topAreaPercentageState.value,
                activatedColor = activatedColorState.value,
                thicknessMultiplier = thicknessMultiplierState.value,
                debuggingEnabled = debuggingEnabledState.value
            )
        }
    }
}

@Composable
fun ShowDisplays(
    gapSizeMultiplier: Float,
    angledSegmentEnds: (index: Int) -> AngledSegmentEnds,
    topAreaPercentage: Float,
    activatedColor: Color,
    thicknessMultiplier: Float,
    debuggingEnabled: Boolean
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
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