package com.chatapp.pingnest.ui.wallpapers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

@Composable
private fun WaveShape(
    spec: CurveSpec,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .blur(spec.blurRadius)
            .drawWithCache {
                val path = Path()
                val amplitude = size.height * 0.05f
                val frequency = 2f
                val step = size.width / 100

                path.moveTo(0f, size.height / 2)
                for (x in 0..100) {
                    val px = x * step
                    val py = size.height / 2 + amplitude * sin((x * frequency) * PI / 180).toFloat()
                    path.lineTo(px, py)
                }

                onDrawBehind {
                    drawPath(
                        path = path,
                        color = spec.color,
                        style = Stroke(width = spec.strokeWidth)
                    )
                }
            }
    )
}
@Composable
fun WaveWallpaper(
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(Color.Cyan, Color.Magenta, Color.Yellow, Color.Green)
) {
    val specs = remember {
        List(10) {
            CurveSpec(
                color = colors.random(),
                start = Offset.Zero,
                control1 = Offset.Zero,
                control2 = Offset.Zero,
                end = Offset.Zero,
                strokeWidth = Random.nextDouble(2.0, 6.0).toFloat(),
                blurRadius = Random.nextDouble(0.0, 10.0).dp
            )
        }
    }

    Box(modifier = modifier.background(Color.Unspecified)) {
        specs.forEach { spec ->
            WaveShape(spec = spec, modifier = Modifier.fillMaxSize())
        }
    }
}
