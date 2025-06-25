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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.chatapp.pingnest.ui.components.FunctionalityNotAvailablePopup

@Composable
fun PrivacySettings(modifier: Modifier = Modifier,onNavBackClicked: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var popUp by remember { mutableStateOf(false) }
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SettingBar(
                text = "Privacy",
                onNavBackClicked = onNavBackClicked,
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        Box(
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(it),
            contentAlignment = Alignment.Center
        ){ if (popUp){
            FunctionalityNotAvailablePopup(onDismiss = { popUp = !popUp })
        }
            Column (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 16.dp, horizontal = 8.dp)
                    .verticalScroll(rememberScrollState())
            ){
                ItemHeader(
                    text = "Who can see my personal info"
                )
                PrivacySettingsItem(
                    settingsTitle = "Last seen and online",
                    settingsSubTitle = "Nobody",
                    onClicked = {popUp = !popUp }
                )
                PrivacySettingsItem(
                    settingsTitle = "Profile Picture",
                    settingsSubTitle = "My contacts",
                    onClicked = {popUp = !popUp}
                )
                PrivacySettingsItem(
                    settingsTitle = "About",
                    settingsSubTitle = "Everyone",
                    onClicked = {popUp = !popUp}
                )

            }
        }
    }
}

@Composable
private fun PrivacySettingsItem(
    modifier: Modifier = Modifier,
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
            .clickable(onClick = onClicked),
    ) {

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