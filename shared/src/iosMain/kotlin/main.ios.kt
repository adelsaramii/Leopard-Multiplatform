package com.attendace.leopard

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.attendace.leopard.presentation.screen.navigation.deviceInput
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.attendace.leopard.data.source.remote.model.dto.AddDeviceInput
import io.github.xxfast.decompose.LocalComponentContext

fun MainViewController() = ComposeUIViewController {

    val lifecycle = LifecycleRegistry()
    val backDispatcher = BackDispatcher()

    val rootComponentContext = DefaultComponentContext(
        lifecycle = lifecycle,
        backHandler = backDispatcher
    )
    CompositionLocalProvider(LocalComponentContext provides rootComponentContext) {
        App()
    }
}

fun setupAddDeviceInput(addDeviceInput: AddDeviceInput) {
    deviceInput.value = addDeviceInput
}