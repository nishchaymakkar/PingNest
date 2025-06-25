@file:OptIn(ExperimentalMaterial3Api::class)

package com.chatapp.pingnest.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.CompareArrows
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.chatapp.pingnest.ui.components.FunctionalityNotAvailablePopup

@Composable
fun AccountSettings(modifier: Modifier = Modifier,onNavBackClicked: () -> Unit,onLogOutClick: () -> Unit ) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var popUp by remember { mutableStateOf(false) }
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SettingBar(
                text = "Account",
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().padding(vertical = 16.dp, horizontal = 8.dp).verticalScroll(rememberScrollState())
            ) {
                SettingsRow(
                    icon = Icons.Outlined.Email,
                    text = "Email address",
                    modifier = Modifier.clickable(onClick = {
                        popUp = !popUp
                    })
                )
                SettingsRow(
                    icon = Icons.AutoMirrored.Outlined.CompareArrows,
                    text = "Change Number",
                    modifier = Modifier.clickable(onClick = {  popUp = !popUp})
                )
                SettingsRow(
                    icon = Icons.Outlined.Description,
                    text = "Request account info",
                    modifier = Modifier.clickable(onClick = {  popUp = !popUp})
                )
                SettingsRow(
                    icon = Icons.Outlined.Delete,
                    text = "Delete account",
                    modifier = Modifier.clickable(onClick = {  popUp = !popUp})
                )
                SettingsRow(
                    icon = Icons.AutoMirrored.Outlined.ExitToApp,
                    text = "Sign Out",
                    modifier = Modifier.clickable(onClick = onLogOutClick)
                )

            }
        }
    }

}