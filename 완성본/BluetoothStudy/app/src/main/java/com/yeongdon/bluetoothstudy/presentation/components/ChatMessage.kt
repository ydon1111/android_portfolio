package com.yeongdon.bluetoothstudy.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yeongdon.bluetoothstudy.domain.chat.BluetoothMessage
import com.yeongdon.bluetoothstudy.ui.theme.BluetoothStudyTheme
import com.yeongdon.bluetoothstudy.ui.theme.OldRose
import com.yeongdon.bluetoothstudy.ui.theme.Vanilla


@Composable
fun ChatMessage(
    message: BluetoothMessage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(
                RoundedCornerShape(
                    topStart = if (message.isFromLocalUser) 15.dp else 0.dp,
                    topEnd = 15.dp,
                    bottomStart = 15.dp,
                    bottomEnd = if (message.isFromLocalUser) 0.dp else 15.dp
                )
            )
            .background(
                if (message.isFromLocalUser) OldRose else Vanilla
            )
            .padding(16.dp)
    ) {
        Text(
            text = message.message,
            color = Color.Black,
            modifier = Modifier.widthIn(max = 250.dp)
        )
    }
}

@Preview
@Composable
fun ChatMessagePreview(){
    BluetoothStudyTheme{
        ChatMessage(message = BluetoothMessage(
            message = "테스트 메시지",
            senderName = "Note20",
            isFromLocalUser = false
        ))
    }
}