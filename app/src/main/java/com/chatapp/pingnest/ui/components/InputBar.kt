package com.chatapp.pingnest.ui.components


import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chatapp.pingnest.ui.conversation.EmojiSelector
import com.chatapp.pingnest.ui.theme.PingNestTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
private fun MessageTextFiledPreview() {
    PingNestTheme {
        InputBar(
            onSendMessage = {},
            onCameraClick = {},
            onGalleryClick = {}, primaryColor = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun InputBar(
    modifier: Modifier = Modifier,
    onSendMessage: (TextFieldValue) -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    primaryColor: Color
) {
    val focusManager = LocalFocusManager.current
    val focusRequest = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    var emojiSelector by remember { mutableStateOf(false) }
    val isKeyboardOpen by rememberKeyboardOpenState()
    val scope = rememberCoroutineScope()
    var messageText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    // Auto-close emoji selector if keyboard opens
    LaunchedEffect(isKeyboardOpen) {
        if (isKeyboardOpen) emojiSelector = false
    }

    if (emojiSelector) {
        BackHandler {
            emojiSelector = false
        }
    }

    Surface(
        modifier = modifier
            .imePadding(),
        tonalElevation = 3.dp,
    ) {



    Column(modifier = Modifier) {
        Row (
            modifier = Modifier .background(MaterialTheme.colorScheme.surface).padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ){
            IconButton(
                onClick = onCameraClick
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null,
                    tint = primaryColor
                )
            }
            IconButton(
                onClick = onGalleryClick
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoLibrary,
                    contentDescription = null,
                    tint = primaryColor
                )
            }
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                minLines = 1,
                maxLines = 3,
                modifier = Modifier.shadow(elevation = 4.dp, shape = RoundedCornerShape(30.dp))
                    .weight(1f),
                leadingIcon = {
                    IconButton(onClick = {
                        if (isKeyboardOpen) {
                            scope.launch {
                                keyboardController?.hide()
                                delay(100) // Wait for keyboard to hide
                                emojiSelector = true
                            }
                        } else {
                            emojiSelector = !emojiSelector
                        }
                    }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.EmojiEmotions,
                            contentDescription = null
                        )
                    }
                },
                placeholder = {
                    Text("Message")
                },
                shape = RoundedCornerShape(30.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceBright,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceBright,
                    cursorColor = primaryColor,
                    focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        onSendMessage(messageText)
                        messageText = TextFieldValue()
                    }
                )
            )
            FilledIconButton(
                onClick = {
                    onSendMessage(messageText)
                    focusManager.clearFocus()
                    messageText = TextFieldValue()
                    keyboardController?.hide()
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = primaryColor,
                    contentColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .size(56.dp)
                    .shadow(6.dp, CircleShape)
                    .clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = null
                )
            }
        }


        if (emojiSelector) {
            AnimatedVisibility(visible = emojiSelector) {
                EmojiSelector(
                    onTextAdded = { messageText = messageText.addText(it) },
                    focusRequester = focusRequest
                )
            }
        }
    }
    }
}
private fun TextFieldValue.addText(newString: String): TextFieldValue {
    val newText = this.text.replaceRange(
        this.selection.start,
        this.selection.end,
        newString,
    )
    val newSelection = TextRange(
        start = newText.length,
        end = newText.length,
    )
    return this.copy(text = newText, selection = newSelection)
}
@Composable
fun rememberKeyboardOpenState(): State<Boolean> {
    val ime = WindowInsets.ime
    val density = LocalDensity.current
    return remember {
        derivedStateOf { ime.getBottom(density) > 0 }
    }
}

