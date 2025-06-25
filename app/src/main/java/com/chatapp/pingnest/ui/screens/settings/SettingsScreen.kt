@file:OptIn(ExperimentalMaterial3Api::class)

package com.chatapp.pingnest.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chatapp.pingnest.R
import com.chatapp.pingnest.ui.components.ProfileIcon
import com.chatapp.pingnest.ui.theme.PingNestTheme
import java.nio.file.WatchEvent


@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onNavBackClicked: () -> Unit,
    onAccountClicked: ()-> Unit,
    onHelpClicked: ()-> Unit,
    onAppLanguageClicked: ()-> Unit,
    onPrivacyClicked: ()-> Unit,
    onChatThemeClicked: ()-> Unit,
    onProfileClicked: ()-> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold (
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SettingBar(
                text = stringResource(R.string.settings),
                onNavBackClicked = onNavBackClicked,
                scrollBehavior = scrollBehavior
            )
        }
    ){
        Box(
            modifier
                .fillMaxSize()
                .padding(it)
                .background(MaterialTheme.colorScheme.surface)
        ){
            Column(
                modifier= Modifier.verticalScroll(rememberScrollState())
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                        .height(88.dp)
                        .fillMaxWidth()
                        .clickable(onClick = onProfileClicked)

                ){
                    ProfileIcon(
                       modifier =  Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                        name = "John Doe",
                        size = 64.dp
                    )
                    Column(
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = "John Doe",
                            style = MaterialTheme.typography.titleLarge,
                        )
                        Text(
                            text = "Unavailable",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(.7f)
                        )
                    }

                }
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(.1f)
                )

                SettingsItem(
                    icon = Icons.Outlined.Key,
                    settingsTitle = "Account",
                    settingsSubTitle = "Security, change number",
                    onClicked = onAccountClicked
                )
                SettingsItem(
                    icon = Icons.Outlined.Lock,
                    settingsTitle = "Privacy",
                    settingsSubTitle = "Block contacts",
                    onClicked = onPrivacyClicked
                )
                SettingsItem(
                    icon = Icons.AutoMirrored.Outlined.Chat,
                    settingsTitle = "Chats",
                    settingsSubTitle = "Theme, wallpapers",
                    onClicked = onChatThemeClicked
                )
                SettingsItem(
                    icon = Icons.Outlined.Language,
                    settingsTitle = "App Language",
                    settingsSubTitle = "English (device's language)",
                    onClicked = onAppLanguageClicked
                )
                SettingsItem(
                    icon = Icons.AutoMirrored.Outlined.HelpOutline,
                    settingsTitle = "Help",
                    settingsSubTitle = "Help Center, contact us, privacy policy",
                    onClicked = onHelpClicked
                )
            }
        }
    }

}

@Composable
internal fun SettingBar(
    text: String,
    onNavBackClicked:()-> Unit,
    scrollBehavior: TopAppBarScrollBehavior
){
    Column {
        TopAppBar(
            modifier = Modifier,
            title = {
                Text(text = text)
            },
            navigationIcon = {
                IconButton(
                    onClick = onNavBackClicked
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            scrollBehavior = scrollBehavior
        )
        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(.1f))
    }
}

@Composable
private fun SettingsItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    settingsTitle: String,
    settingsSubTitle: String,
    onClicked:()-> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(72.dp)
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clickable(onClick = onClicked)
        ,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(.7f),
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
        )
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = settingsTitle,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = settingsSubTitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(.7f)
            )
        }

    }
}

@Preview
@Composable
private fun SettingsPreview() {
    PingNestTheme {
        SettingsScreen(
            onNavBackClicked = {},
            onAccountClicked = {},
            onHelpClicked = {},
            onAppLanguageClicked = {},
            onPrivacyClicked = {},
            onChatThemeClicked = {},
            onProfileClicked = {}
        )
    }
}



@Composable
fun SettingsRow(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.height(56.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ){
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface.copy(.7f),
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        )
    }
}