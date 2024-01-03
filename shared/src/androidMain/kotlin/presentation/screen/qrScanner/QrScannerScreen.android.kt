package com.attendace.leopard.presentation.screen.qrScanner

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@Composable
actual fun QrScannerScreen(modifier: Modifier, onQrCodeScanned: (String) -> Unit) {
    QrScanner(onQrCodeScanned)
}

@Composable
private fun QrScanner(
    onQrCodeScanned: (String) -> Unit
) {
    val scanLauncher = rememberLauncherForActivityResult(
        contract = ScanContract(),
        onResult = { result ->
            if (result.contents == null || result.contents == "") {
                onQrCodeScanned("")
            } else {
                onQrCodeScanned(result.contents)
            }
        }
    )

    LaunchedEffect(key1 = true) {
        scanLauncher.launch(ScanOptions())
    }
}