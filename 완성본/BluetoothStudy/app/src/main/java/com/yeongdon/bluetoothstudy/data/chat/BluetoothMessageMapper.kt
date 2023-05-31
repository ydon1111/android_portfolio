package com.yeongdon.bluetoothstudy.data.chat

import com.yeongdon.bluetoothstudy.domain.chat.BluetoothMessage


fun String.toBluetoothMessage(isFromLocalUser: Boolean): BluetoothMessage{
    val name = substringBefore("#")
    val message = substringAfter("#")
    return BluetoothMessage(
        message = message,
        senderName = name,
        isFromLocalUser = isFromLocalUser
    )
}

fun BluetoothMessage.toByteArray(): ByteArray{
    return "$senderName#$message".encodeToByteArray()
}