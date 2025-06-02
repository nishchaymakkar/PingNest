import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Preview(
    apiLevel = 33,
)
@Composable
fun EnterChatRoomDialog(
    onDismiss: () -> Unit = {},
    onEnter: (nickname: String, realName: String) -> Unit = { _, _ -> }
) {
    var nickname by remember { mutableStateOf("") }
    var realName by remember { mutableStateOf("") }

    AlertDialog(
        containerColor = MaterialTheme.colorScheme.background,
        onDismissRequest = onDismiss,
        title = { },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Enter Chatroom",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge)
                OutlinedTextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    label = { Text("Nickname") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                
                OutlinedTextField(
                    value = realName,
                    onValueChange = { realName = it },
                    label = { Text("Real Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onEnter(nickname, realName) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Enter Chatroom")
            }
        },
        properties = DialogProperties(
            dismissOnClickOutside =false
        )
    )
}