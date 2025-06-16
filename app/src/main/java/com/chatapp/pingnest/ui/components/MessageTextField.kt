package com.chatapp.pingnest.ui.components


import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.input.KeyboardType
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
//        MessageTextField()
    }
}

@Composable
fun MessageTextField(
    modifier: Modifier = Modifier,
    onSendMessage: (TextFieldValue) -> Unit
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

    Column(modifier = modifier) {
        OutlinedTextField(
            value = messageText,
            onValueChange = { messageText = it},
            minLines = 1,
            maxLines = 3,
            modifier = Modifier
                .imePadding()
                .fillMaxWidth()
                .padding(8.dp)
                .shadow(4.dp, RoundedCornerShape(50)),
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
            trailingIcon = {
                IconButton(
                    onClick = {
                        onSendMessage(messageText)
                        focusManager.clearFocus()
                        messageText = TextFieldValue()
                        keyboardController?.hide()
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
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
            },
            shape = RoundedCornerShape(30.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = MaterialTheme.colorScheme.surfaceBright,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceBright,
                cursorColor = MaterialTheme.colorScheme.primary,
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

        if (emojiSelector) {
            AnimatedVisibility(visible = emojiSelector) {
            EmojiSelector(
                onTextAdded = { messageText = messageText.addText(it) },
                focusRequester = focusRequest
            )
        }}
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

