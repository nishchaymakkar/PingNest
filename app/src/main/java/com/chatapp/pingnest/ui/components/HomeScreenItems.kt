package com.chatapp.pingnest.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chatapp.pingnest.R
import com.chatapp.pingnest.data.models.Status

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
fun ChatItem(modifier: Modifier,nickname: String, fullname: String, selected: Boolean, status: Status?) {
    val background = if (selected) {
        MaterialTheme.colorScheme.background
    } else {
        MaterialTheme.colorScheme.surface
    }
    Row(
        modifier = modifier
            .height(72.dp)
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .background(background),
        verticalAlignment = CenterVertically,
    ) {
        Box {
            ProfileIcon(
                size = 48.dp,
                name = fullname,
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
            )
            // Enhanced status indicator
            if (status == Status.ONLINE) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .offset(x = 52.dp, y = 4.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,  // Using theme color
                            shape = CircleShape
                        )
                        .border(1.5.dp, MaterialTheme.colorScheme.surface, CircleShape)
                )
            }
            // ✅ Tick icon if selected
            if (selected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    modifier = Modifier
                        .size(20.dp)
                        .offset(x = 40.dp, y = 36.dp), // Position below profile
                    tint = MaterialTheme.colorScheme.primaryContainer
                )
            }
        }
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = fullname,
                fontWeight = FontWeight.SemiBold,  // Slightly reduced weight
                style = MaterialTheme.typography.bodyLarge,
                color = if (selected) {
                    MaterialTheme.colorScheme.onPrimaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
            )
            Text(
                text = "@" + nickname,
                fontWeight = FontWeight.Normal,  // Lighter weight for secondary text
                style = MaterialTheme.typography.bodyMedium,  // Slightly larger
                color = if (selected) {
                    MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
            )
        }
    }
}
@Composable
fun DividerItem(modifier: Modifier = Modifier) {
    HorizontalDivider(
        modifier = modifier.padding(horizontal = 16.dp),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
    )
}

