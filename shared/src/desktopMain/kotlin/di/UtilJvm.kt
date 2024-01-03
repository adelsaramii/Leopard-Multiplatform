package com.attendace.leopard.di

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import org.koin.core.annotation.KoinReflectAPI
import org.koin.core.definition.Definition
import org.koin.core.definition.KoinDefinition
import org.koin.core.instance.newInstance
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier

@KoinReflectAPI
@Deprecated("API is deprecated in favor of factoryOf DSL")
actual inline fun <reified T : ViewModel> Module.viewModelDefinition(
    qualifier: Qualifier?,
    noinline definition: Definition<T>
): KoinDefinition<T> {
    return single(qualifier) { params -> newInstance(T::class, params) }
}

actual fun loge(message : String) {}