import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.fsryan.ui.segments.Hexagonal7SegmentDisplay
import com.fsryan.ui.segments.createSymmetricHexagonal7SegmentParamsFun
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import fs_segmented_display.app.generated.resources.Res
import fs_segmented_display.app.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val charWidth = maxWidth / 8
            val charHeight = charWidth * 2
            Column(
                modifier = Modifier.fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.width(6 * charWidth)
                        .height(charHeight)
                ) {
                    Hexagonal7SegmentDisplay(
                        text = "HELLO",
                        gapSize = 5F,
                        activatedColor = MaterialTheme.colors.primary,
                        hexagonalSegmentParams = createSymmetricHexagonal7SegmentParamsFun()
                    )
                }
                Box(
                    modifier = Modifier.width(5 * charWidth)
                        .height(charHeight)
                ) {
                    Hexagonal7SegmentDisplay(
                        text = "WORLD",
                        gapSize = 5F,
                        activatedColor = MaterialTheme.colors.primary,
                        hexagonalSegmentParams = createSymmetricHexagonal7SegmentParamsFun()
                    )
                }
                Box(
                    modifier = Modifier.width(8 * charWidth)
                        .height(charHeight)
                ) {
                    Hexagonal7SegmentDisplay(
                        text = "867-5309",
                        gapSize = 5F,
                        activatedColor = MaterialTheme.colors.primary,
                        hexagonalSegmentParams = createSymmetricHexagonal7SegmentParamsFun()
                    )
                }
            }
        }
    }
}