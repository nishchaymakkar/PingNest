package com.chatapp.pingnest.ui.components

import androidx.annotation.Size
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.absoluteValue

@Composable
fun ProfileIcon(size: Dp, name: String, modifier: Modifier = Modifier) {
    val initials = name.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
    val backgroundColor = remember { getRandomColor(name) }
    
    val fontSize = (size.value * 0.4f).sp

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .then(modifier)  // Apply custom modifier first
            .background(backgroundColor.copy(.3f), CircleShape)  // Apply background with shape first
            .clip(CircleShape)
            .size(size),  // Then clip to ensure perfect circle
    ) {
        Text(
            text = initials.uppercase(),
            color = backgroundColor,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = fontSize
            ),
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}

fun getRandomColor(name: String): Color {
    val colors = listOf(
        Color(0xFFFF4141), // Red
        Color(0xFFFF7A50), // Orange
        Color(0xFF62FD67), // Green
        Color(0xFF37A6FF), // Blue
        Color(0xFFDF5DFF)  // Purple
    )
    return colors[name.hashCode().absoluteValue % colors.size]
}