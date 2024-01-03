package com.attendace.leopard.presentation.screen.qrScanner

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun QrScannerScreen(modifier: Modifier, onQrCodeScanned: (String) -> Unit)