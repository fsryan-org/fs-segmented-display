import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.fsryan.ui.segments.Rect7SegmentDisplay
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import myuilibrary.composeapp.generated.resources.Res
import myuilibrary.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { showContent = !showContent }) {
                Text("Click me!")
            }
            AnimatedVisibility(showContent) {
                BoxWithConstraints {
                    val width = maxWidth
                    Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Rect7SegmentDisplay(
                            modifier = Modifier.background(Color.LightGray)
                                .fillMaxWidth()
                                .height(width / 4),
                            text = "01234567",
                            thicknessMultiplier = 0.6F,
                            gapSizeMultiplier = 0.75F,
                            activatedColor = Color.Red
                        )
                        Rect7SegmentDisplay(
                            modifier = Modifier.background(Color.LightGray)
                                .fillMaxWidth()
                                .height(width / 4),
                            thicknessMultiplier = 0.6F,
                            gapSizeMultiplier = 0.75F,
                            text = "89ABCDEF",
                            activatedColor = Color.Red
                        )
                    }
                }
            }
        }
    }
}