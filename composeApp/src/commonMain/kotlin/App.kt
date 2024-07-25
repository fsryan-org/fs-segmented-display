
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.fsryan.ui.segments.Rect7SegmentDisplay
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                val width = maxWidth
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Rect7SegmentDisplay(
                        modifier = Modifier.background(Color.LightGray)
                            .fillMaxWidth()
                            .height(width / 4), // <-- aspect ratio of each character is 1:2
                        text = "01234567",
                        thicknessMultiplier = 0.6F,
                        gapSizeMultiplier = 0.75F,
                        activatedColor = Color.Red
                    )
                    Rect7SegmentDisplay(
                        modifier = Modifier.background(Color.LightGray)
                            .fillMaxWidth()
                            .height(width / 4), // <-- aspect ratio of each character is 1:2
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