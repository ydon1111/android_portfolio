package com.yeongdon.bluetoothstudy.presentation

import com.yeongdon.bluetoothstudy.domain.chat.BluetoothDevice
import com.yeongdon.bluetoothstudy.domain.chat.BluetoothMessage

data class BluetoothUiState(
    val scannedDevices: List<BluetoothDevice> = emptyList(),
    val pairedDevices: List<BluetoothDevice> = emptyList(),
    val isConnected: Boolean = false,
    val isConnecting: Boolean = false,
    val errorMessage: String? = null,
    val messages: List<BluetoothMessage> = emptyList()
)