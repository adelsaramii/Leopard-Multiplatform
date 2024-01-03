package  com.attendace.leopard.di

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.PreferencesSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import org.koin.dsl.module
import io.ktor.client.engine.java.*
import java.util.prefs.Preferences

@OptIn(ExperimentalSettingsApi::class)
actual fun dataPlatformModule(context: Any?) = module {

    single { Java.create() }

    single {
        PreferencesSettings(Preferences.userRoot().node("LEOPARD_SETTING")).toFlowSettings()
    }

}