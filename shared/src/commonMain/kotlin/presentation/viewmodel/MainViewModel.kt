package com.attendace.leopard.presentation.viewmodel

import com.attendace.leopard.data.repository.auth.AuthRepository
import com.attendace.leopard.data.base.BaseViewModel
import com.attendace.leopard.data.base.Failed
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.data.base.NotLoaded
import com.attendace.leopard.data.model.RolePermissionModel
import com.attendace.leopard.util.localization.LanguageTypeEnum
import com.attendace.leopard.util.theme.language
import com.attendance.leopard.data.model.User
import com.attendace.leopard.data.source.remote.model.dto.AddDeviceInput
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: AuthRepository
) : BaseViewModel<MainViewModel.State>(State()) {

    data class State(
        val userInfo: LoadableData<User> = NotLoaded,
        val baseUrl: String? = null,
        val accessToken: String? = null,
        val rolePermissionModel: LoadableData<RolePermissionModel> = NotLoaded,
        val currentLanguageTag: LanguageTypeEnum = LanguageTypeEnum.English,
        val currentLanguageName: String = "",
        val addDeviceInfoCalled: Boolean = false
    )

    init {
        getAddDeviceInfoCalled()
        getLocale()
        getBaseUrl()
        getAccessToken()
        getUserInfo()
        getRolePermissionModel()
    }

    private val currentLanguageTag =
        repository.getLocale()
            .stateIn(
                scope = viewModelScope,
                initialValue = LanguageTypeEnum.English.name,
                started = SharingStarted.WhileSubscribed(5_000)
            ).onEach {
                updateState {
                    when (it) {
                        LanguageTypeEnum.English.name -> {
                            copy(currentLanguageTag = LanguageTypeEnum.English)
                        }

                        LanguageTypeEnum.Arabic.name -> {
                            copy(currentLanguageTag = LanguageTypeEnum.Arabic)
                        }

                        LanguageTypeEnum.Kurdish.name -> {
                            copy(currentLanguageTag = LanguageTypeEnum.Kurdish)
                        }

                        else -> {
                            copy(currentLanguageTag = LanguageTypeEnum.English)
                        }
                    }
                }
            }

    private val currentLanguageName =
        repository.getLocaleName()
            .stateIn(
                scope = viewModelScope,
                initialValue = LanguageTypeEnum.English.name,
                started = SharingStarted.WhileSubscribed(5_000)
            ).onEach {
                updateState { copy(currentLanguageName = it) }
            }

    private fun getUserInfo() {
        viewModelScope.launch {
            repository.getUserInfo().fold(ifRight = {
                updateState { copy(userInfo = Loaded(it)) }
            }, ifLeft = {
                repository.getOfflineUserInfo().collect {
                    updateState { copy(userInfo = Loaded(it)) }
                }
            })
        }
    }

    private fun getBaseUrl() {
        viewModelScope.launch {
            repository.getBaseUrl().collect {
                updateState {
                    copy(
                        baseUrl = it
                    )
                }
            }
        }
    }

    private fun getLocale() {
        viewModelScope.launch {
            currentLanguageName.collect {
                updateState {
                    copy(currentLanguageName = it)
                }
            }
        }
        viewModelScope.launch {
            currentLanguageTag.collect {
                updateState {
                    when (it) {
                        LanguageTypeEnum.English.name -> {
                            language.value = LanguageTypeEnum.English
                            copy(currentLanguageTag = LanguageTypeEnum.English)
                        }

                        LanguageTypeEnum.Arabic.name -> {
                            language.value = LanguageTypeEnum.Arabic
                            copy(currentLanguageTag = LanguageTypeEnum.Arabic)
                        }

                        LanguageTypeEnum.Kurdish.name -> {
                            language.value = LanguageTypeEnum.Kurdish
                            copy(currentLanguageTag = LanguageTypeEnum.Kurdish)
                        }

                        else -> {
                            copy(currentLanguageTag = LanguageTypeEnum.English)
                        }
                    }
                }
            }
        }
    }

    private fun getAccessToken() {
        viewModelScope.launch {
            repository.getAccessToken().collect {
                updateState {
                    copy(
                        accessToken = it
                    )
                }
            }
        }
    }

    fun getRolePermissionModel() {
        viewModelScope.launch {
            repository.getRolePermissionModel().fold(ifRight = {
                updateState { copy(rolePermissionModel = Loaded(it)) }
            }, ifLeft = {
                updateState { copy(rolePermissionModel = Failed(it)) }
            })
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun addDeviceInfo(deviceInput: AddDeviceInput) {
        viewModelScope.launch {
            repository.addDeviceInfo(deviceInput).fold(
                ifLeft = { setAddDeviceInfoCalled(false) },
                ifRight = { setAddDeviceInfoCalled(true) }
            )
        }
    }

    private fun getAddDeviceInfoCalled() {
        viewModelScope.launch {
            repository.getAddDeviceInfoCalled().collect {
                updateState { copy(addDeviceInfoCalled = it) }
            }
        }
    }

    private fun setAddDeviceInfoCalled(value: Boolean) {
        viewModelScope.launch {
            repository.setAddDeviceInfoCalled(value)
        }
    }

}