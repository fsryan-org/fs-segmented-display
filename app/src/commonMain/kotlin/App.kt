import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.fsryan.ui.segments.Hexagonal7SegmentDisplay
import com.fsryan.ui.segments.HexagonalSegmentParams
import com.fsryan.ui.segments.classic7AsymmetricParamsFun
import com.fsryan.ui.segments.classic7SymmetricParamsFun
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val charWidth = maxWidth / 12
            val charHeight = charWidth * 2
            val hexagoanlSegementParams = HexagonalSegmentParams.classic7AsymmetricParamsFun()
            Column(
                modifier = Modifier.fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.width(12 * charWidth)
                        .height(charHeight)
                ) {
                    Hexagonal7SegmentDisplay(
                        text = "0123456789AB",
                        shearPct = 0.33F,
                        thicknessMultiplier = 1.3F,
                        activatedColor = MaterialTheme.colors.primary,
                        hexagonalSegmentParams = hexagoanlSegementParams
                    )
                }
                Box(
                    modifier = Modifier.width(12 * charWidth)
                        .height(charHeight)
                ) {
                    Hexagonal7SegmentDisplay(
                        text = "CDEFGHIJKLMN",
                        shearPct = 0.33F,
                        thicknessMultiplier = 1.3F,
                        activatedColor = MaterialTheme.colors.primary,
                        hexagonalSegmentParams = hexagoanlSegementParams
                    )
                }
                Box(
                    modifier = Modifier.width(12 * charWidth)
                        .height(charHeight)
                ) {
                    Hexagonal7SegmentDisplay(
                        text = "OPQRSTUVWXYZ",
                        shearPct = 0.33F,
                        thicknessMultiplier = 1.3F,
                        activatedColor = MaterialTheme.colors.primary,
                        hexagonalSegmentParams = hexagoanlSegementParams
                    )
                }
            }
        }
    }
}