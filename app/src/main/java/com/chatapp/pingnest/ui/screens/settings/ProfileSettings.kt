@file:OptIn(ExperimentalMaterial3Api::class)

package com.chatapp.pingnest.ui.screens.settings

import androidx.collection.floatSetOf
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.LocalPhone
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chatapp.pingnest.ui.components.AnimatingFabContent
import com.chatapp.pingnest.ui.components.FunctionalityNotAvailablePopup
import com.chatapp.pingnest.ui.components.ProfileIcon

@Composable
fun ProfileSettingsScreen(onNavBackClicked: () -> Unit,
                          modifier: Modifier = Modifier) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scrollState = rememberScrollState()
    val fabExtended by remember { derivedStateOf { scrollState.value == 0 } }
    var popUp by remember { mutableStateOf(false) }
    Scaffold (
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SettingBar(
                text = "Profile",
                onNavBackClicked = onNavBackClicked,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            ProfileFab(
                extended = fabExtended,
                onFabClicked = {popUp = !popUp}
            )
        }
    ) {
        Box(
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            if (popUp){
                FunctionalityNotAvailablePopup(onDismiss = {popUp = !popUp})
            }
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp, horizontal = 8.dp)
                    .verticalScroll(scrollState)
            ){
                ProfileIcon(
                    name = "John Doe",
                    size = 150.dp,
                    modifier = Modifier.padding(16.dp)
                )

                ProfileSettingsItem(
                    icon = Icons.Outlined.PersonOutline,
                    settingsTitle = "Name",
                    settingsSubTitle = "John Doe",
                )
                ProfileSettingsItem(
                    icon = Icons.Outlined.PersonOutline,
                    settingsTitle = "Name",
                    settingsSubTitle = "John Doe",
                )
                ProfileSettingsItem(
                    icon = Icons.Outlined.LocalPhone,
                    settingsTitle = "Phone",
                    settingsSubTitle = "+91 12345 67890"
                )
            }
        }

    }
}
@Composable
private fun ProfileSettingsItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    settingsTitle: String,
    settingsSubTitle: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(72.dp)
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
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

@Composable
fun ProfileFab(extended: Boolean, modifier: Modifier = Modifier, onFabClicked: () -> Unit = { }) {


        FloatingActionButton(
            onClick = onFabClicked,
            modifier = modifier
                .padding(16.dp)
                .navigationBarsPadding()
                .height(48.dp)
                .widthIn(min = 48.dp),
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
        ) {
            AnimatingFabContent(
                icon = {
                    Icon(
                        imageVector =  Icons.Outlined.Create ,
                        contentDescription = null,
                    )
                },
                text = {
                    Text(
                        text = "Edit",
                    )
                },
                extended = extended,
            )
        }

}
