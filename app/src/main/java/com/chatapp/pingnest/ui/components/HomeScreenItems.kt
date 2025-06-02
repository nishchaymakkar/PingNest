package com.chatapp.pingnest.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chatapp.pingnest.R

@Composable
fun DrawerItemHeader(text: String) {
    Box(
        modifier = Modifier
            .heightIn(min = 52.dp)
            .padding(horizontal = 24.dp),
        contentAlignment = CenterStart,
    ) {
        Text(
            text,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
@Composable
fun DrawerHeader() {
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = CenterVertically) {
        Icon(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(50.dp),
        )
        Text(
            text = "PingNest",
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primaryContainer,
            style = MaterialTheme.typography.titleLarge
        )

    }
}

@Composable
fun ChatItem(text: String, selected: Boolean, onChatClicked: () -> Unit) {
    val background = if (selected) {
        Modifier.background(MaterialTheme.colorScheme.primaryContainer)
    } else {
        Modifier
    }
    Row(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(horizontal = 6.dp)
            .clip(CircleShape)
            .then(background)
            .clickable(onClick = onChatClicked),
        verticalAlignment = CenterVertically,
    ) {
        val iconTint = if (selected) {
            MaterialTheme.colorScheme.onPrimaryContainer
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
        Icon(
            imageVector = Icons.Default.AccountCircle,
            tint = iconTint,
            modifier = Modifier.size(50.dp)
                .padding(start = 16.dp),
            contentDescription = null,
        )
        Text(
            text,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge,
            color = if (selected) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier.padding(start = 12.dp),
        )
    }
}
@Composable
fun DividerItem(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier.padding(horizontal = 16.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
    )
}

