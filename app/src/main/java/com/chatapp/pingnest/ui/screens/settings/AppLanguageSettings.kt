@file:OptIn(ExperimentalMaterial3Api::class)

package com.chatapp.pingnest.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.chatapp.pingnest.R
import com.chatapp.pingnest.ui.components.FunctionalityNotAvailablePopup

@Composable
fun AppLanguageSettings(modifier: Modifier = Modifier, onNavBackClicked: () -> Unit) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var popUp by remember { mutableStateOf(false) }
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SettingBar(
                text = "App Language",
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
        ){
            if (popUp){
                FunctionalityNotAvailablePopup(onDismiss = { popUp = !popUp })
            }
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().padding(vertical = 16.dp)
                    .verticalScroll(rememberScrollState())
            ){
                AppLanguageOption(
                    selected = true,
                    onClick = {},
                    language = "English",
                    languageSubTitle = "English (device's language)"
                )
                AppLanguageOption(
                    selected = false,
                    onClick = {popUp = !popUp},
                    language = stringResource(R.string.hindi),
                    languageSubTitle = "Hindi"

                )
                AppLanguageOption(
                    selected = false,
                    onClick = {popUp = !popUp},
                    language = "ਪੰਜਾਬੀ",
                    languageSubTitle = "Punjabi"

                )
                AppLanguageOption(
                    selected = false,
                    onClick = {popUp = !popUp},
                    language = "ગુજરાતી",
                    languageSubTitle = "Gujarati"

                )
            }
        }
    }

}


@Composable
fun AppLanguageOption(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    language: String,
    languageSubTitle: String
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 8.dp).clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            onClick = onClick,
            selected = selected,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
        )
        Column (
            Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
        ){
            Text(
                text = language,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = languageSubTitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(.7f)
            )
        }

    }

}