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
import kotlin.random.Random

@Composable
private fun BezierCurve(
    spec: CurveSpec,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .blur(spec.blurRadius)
            .drawWithCache {
                val path = Path().apply {
                    moveTo(spec.start.x * size.width, spec.start.y * size.height)
                    cubicTo(
                        spec.control1.x * size.width, spec.control1.y * size.height,
                        spec.control2.x * size.width, spec.control2.y * size.height,
                        spec.end.x * size.width, spec.end.y * size.height
                    )
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
fun CurveWallpaper(
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(Color.Cyan, Color.Magenta, Color.Yellow, Color.Green)
) {
    val specs = remember {
        List(15) {
            CurveSpec(
                color = colors.random(),
                start = Offset(Random.nextFloat(), Random.nextFloat()),
                control1 = Offset(Random.nextFloat(), Random.nextFloat()),
                control2 = Offset(Random.nextFloat(), Random.nextFloat()),
                end = Offset(Random.nextFloat(), Random.nextFloat()),
                strokeWidth = Random.nextDouble(2.0, 6.0).toFloat(),
                blurRadius = Random.nextDouble(0.0, 10.0).dp
            )
        }
    }

    Box(modifier = modifier.background(Color.Unspecified)) {
        specs.forEach { spec ->
            BezierCurve(
                spec = spec,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
