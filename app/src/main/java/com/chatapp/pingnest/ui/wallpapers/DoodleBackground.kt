package com.chatapp.pingnest.ui.wallpapers

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun DoodleBackground(modifier: Modifier = Modifier) {
    Box(modifier = modifier.background(MaterialTheme.colorScheme.background)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val spacing = 100.dp.toPx()
            val iconSize = 14.dp.toPx()
            val strokeWidth = 1.dp.toPx()

            val width = size.width
            val height = size.height

            for (x in 0..(width / spacing).toInt()) {
                for (y in 0..(height / spacing).toInt()) {
                    val offsetX = x * spacing
                    val offsetY = y * spacing

                    if (offsetX > width || offsetY > height) continue

                    when ((x + y) % 4) { // fewer variants
                        0 -> drawSimpleStar(offsetX, offsetY, iconSize, strokeWidth)
                        1 -> drawSimpleBubble(offsetX, offsetY, iconSize, strokeWidth)
                        2 -> drawSimpleHeart(offsetX, offsetY, iconSize, strokeWidth)
                        3 -> drawCircle(Color.Gray, iconSize / 3, Offset(offsetX, offsetY), style = Stroke(strokeWidth))
                    }
                }
            }
        }
    }
}
fun DrawScope.drawSimpleStar(x: Float, y: Float, size: Float, stroke: Float) {
    val half = size / 2
    drawLine(Color.Gray, Offset(x - half, y), Offset(x + half, y), stroke)
    drawLine(Color.Gray, Offset(x, y - half), Offset(x, y + half), stroke)
}

fun DrawScope.drawSimpleBubble(x: Float, y: Float, size: Float, stroke: Float) {
    val radius = size / 2
    drawCircle(Color.Gray, radius = radius, center = Offset(x, y), style = Stroke(stroke))
    drawLine(Color.Gray, Offset(x + radius / 2, y + radius / 2), Offset(x + radius, y + radius), stroke)
}

fun DrawScope.drawSimpleHeart(x: Float, y: Float, size: Float, stroke: Float) {
    val path = Path().apply {
        moveTo(x, y + size / 4)
        cubicTo(x - size, y - size / 2, x - size / 2, y - size, x, y - size / 3)
        cubicTo(x + size / 2, y - size, x + size, y - size / 2, x, y + size / 4)
        close()
    }
    drawPath(path, Color.Gray, style = Stroke(stroke))
}



fun DrawScope.drawMusicNote(x: Float, y: Float, size: Float, stroke: Float) {
    drawLine(Color.Gray, Offset(x, y), Offset(x, y - size), strokeWidth = stroke)
    drawCircle(Color.Gray, radius = size / 4, center = Offset(x, y - size))
}

fun DrawScope.drawStar(x: Float, y: Float, size: Float, stroke: Float) {
    val half = size / 2
    drawLine(Color.Gray, Offset(x - half, y), Offset(x + half, y), stroke)
    drawLine(Color.Gray, Offset(x, y - half), Offset(x, y + half), stroke)
    drawLine(Color.Gray, Offset(x - half, y - half), Offset(x + half, y + half), stroke)
    drawLine(Color.Gray, Offset(x - half, y + half), Offset(x + half, y - half), stroke)
}

fun DrawScope.drawChatBubble(x: Float, y: Float, size: Float, stroke: Float) {
    val rect = Rect(Offset(x - size / 2, y - size / 3), Size(size, size / 1.5f))
    drawRoundRect(Color.Gray, topLeft = rect.topLeft, size = rect.size, cornerRadius = CornerRadius(size / 4), style = Stroke(stroke))
    drawLine(Color.Gray, Offset(x, y + size / 3), Offset(x - size / 6, y + size / 2), stroke)
}

fun DrawScope.drawHeart(x: Float, y: Float, size: Float, stroke: Float) {
    val path = Path().apply {
        moveTo(x, y + size / 4)
        cubicTo(x - size, y - size / 2, x - size / 2, y - size, x, y - size / 3)
        cubicTo(x + size / 2, y - size, x + size, y - size / 2, x, y + size / 4)
        close()
    }
    drawPath(path, Color.Gray, style = Stroke(stroke))
}

fun DrawScope.drawCatFace(x: Float, y: Float, size: Float, stroke: Float) {
    drawCircle(Color.Gray, radius = size / 2, center = Offset(x, y), style = Stroke(stroke))
    drawLine(Color.Gray, Offset(x - size / 2, y - size / 2), Offset(x - size / 4, y - size / 1.5f), stroke) // left ear
    drawLine(Color.Gray, Offset(x + size / 2, y - size / 2), Offset(x + size / 4, y - size / 1.5f), stroke) // right ear
    drawCircle(Color.Gray, radius = 1.5f, center = Offset(x - size / 4, y), style = Fill)
    drawCircle(Color.Gray, radius = 1.5f, center = Offset(x + size / 4, y), style = Fill)
}

fun DrawScope.drawCloud(x: Float, y: Float, size: Float, stroke: Float) {
    drawCircle(Color.Gray, radius = size / 4, center = Offset(x - size / 3, y), style = Stroke(stroke))
    drawCircle(Color.Gray, radius = size / 3, center = Offset(x, y - size / 6), style = Stroke(stroke))
    drawCircle(Color.Gray, radius = size / 4, center = Offset(x + size / 3, y), style = Stroke(stroke))
}

fun DrawScope.drawDrum(x: Float, y: Float, size: Float, stroke: Float) {
    drawRect(Color.Gray, topLeft = Offset(x - size / 2, y - size / 4), size = Size(size, size / 2), style = Stroke(stroke))
    drawLine(Color.Gray, Offset(x - size / 2, y - size / 4), Offset(x + size / 2, y - size / 4), stroke)
}

fun DrawScope.drawBird(x: Float, y: Float, size: Float, stroke: Float) {
    drawArc(Color.Gray, 0f, 270f, useCenter = false, topLeft = Offset(x - size / 2, y - size / 2), size = Size(size, size), style = Stroke(stroke))
    drawLine(Color.Gray, Offset(x, y), Offset(x + size / 2, y - size / 4), stroke)
}

