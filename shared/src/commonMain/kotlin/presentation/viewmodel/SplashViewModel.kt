package com.attendace.leopard.presentation.viewmodel

import com.attendace.leopard.data.repository.auth.AuthRepository
import com.attendace.leopard.data.repository.workplace.WorkplaceRepository
import com.attendace.leopard.data.base.BaseViewModel
import com.attendace.leopard.data.base.Failed
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.data.base.NotLoaded
import com.attendace.leopard.data.source.remote.model.dto.AddDeviceInput
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SplashViewModel(
    private val authRepository: AuthRepository,
    private val workplaceRepository: WorkplaceRepository,
) : BaseViewModel<SplashViewModel.State>(State()) {

    data class State(
        val isLoggedIn: Boolean? = null,
        val baseUrl: String = "",
        val workplaceChangesResult: LoadableData<Unit> = NotLoaded,
    )

    init {
        fetchSplashData()
    }

    private fun fetchSplashData() {
        viewModelScope.launch {
            authRepository.isLoggedIn().collect { isLoggedIn ->
                if (!isLoggedIn) {
                    updateState {
                        copy(isLoggedIn = isLoggedIn)
                    }
                    return@collect
                }
                val workplaceChangesResultDeferred = async {
                    workplaceRepository.getWorkplaceChanges()
                }

                val workplaceChangesResult = workplaceChangesResultDeferred.await()
                workplaceChangesResult.fold(
                    ifLeft = {
                        updateState {
                            copy(
                                isLoggedIn = isLoggedIn,
                                baseUrl = baseUrl,
                                workplaceChangesResult = Failed(it)
                            )
                        }
                    },
                    ifRight = {
                        updateState {
                            copy(
                                isLoggedIn = isLoggedIn,
                                baseUrl = baseUrl,
                                workplaceChangesResult = Loaded(it)
                            )
                        }
                    }
                )
            }
        }
    }

    fun retry() {
        updateState {
            copy(
                workplaceChangesResult = NotLoaded
            )
        }
        fetchSplashData()
    }

    fun addDeviceInfo(deviceInput: AddDeviceInput){
        viewModelScope.launch {
            authRepository.addDeviceInfo(deviceInput)
        }
    }

}