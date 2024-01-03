package com.attendace.leopard

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.attendace.leopard.di.dataModule
import com.attendace.leopard.di.dataPlatformModule
import com.attendace.leopard.di.repositoryModule
import com.attendace.leopard.di.viewModelModule
import com.attendace.leopard.presentation.screen.navigation.AppNavigation
import com.attendace.leopard.util.localization.LanguageTypeEnum
import com.attendace.leopard.util.theme.LeopardTheme
import com.attendace.leopard.util.theme.language
import org.koin.compose.KoinApplication

@Composable
fun App(context: Any? = null) {
    KoinApplication(application = {
        modules(dataPlatformModule(context), dataModule, repositoryModule, viewModelModule)
    }) {
        CompositionLocalProvider(
            LocalLayoutDirection provides
                    if (language.value == LanguageTypeEnum.English) LayoutDirection.Ltr
                    else LayoutDirection.Rtl
        ) {
            LeopardTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AppNavigation()
                }
            }
        }
    }

}