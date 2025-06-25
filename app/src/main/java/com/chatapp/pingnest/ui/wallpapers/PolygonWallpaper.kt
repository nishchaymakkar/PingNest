package com.chatapp.pingnest.ui.wallpapers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.asComposePath
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath

@Composable
private fun PolygonShape(
    sides: Int,
    spec: StarSpec,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .blur(spec.blurRadius)
            .drawWithCache {
                val radius = size.width * spec.size / 2
                if (radius <= 0f || sides < 3) {
                    onDrawBehind {}
                } else {
                    val polygon = RoundedPolygon(
                        numVertices = sides,
                        radius = radius,
                        rounding = CornerRounding(size.width * 0.05f),
                        centerX = size.width * spec.offset.x,
                        centerY = size.height * spec.offset.y,
                    )
                    val path = polygon.toPath().asComposePath()

                    onDrawBehind {
                        drawPath(path, color = spec.color)
                    }
                }
            }
    )
}
@Composable
fun PolygonWallpaper(
    modifier: Modifier = Modifier,
    sides: Int = 6, // Hexagon
    colors: StarColors = StarColors.defaults()
) {
    val specs = remember(colors) { createStarSpecs(colors) }
    Box(modifier = modifier) {
        specs.forEach { spec ->
            PolygonShape(sides = sides, spec = spec, modifier = Modifier.fillMaxSize())
        }
    }
}