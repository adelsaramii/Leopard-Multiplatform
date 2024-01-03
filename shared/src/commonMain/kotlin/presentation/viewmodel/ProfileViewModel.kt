package com.attendace.leopard.presentation.viewmodel

import com.attendace.leopard.data.repository.auth.AuthRepository
import com.attendace.leopard.data.base.BaseViewModel
import com.attendace.leopard.data.base.Failed
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.data.base.Loading
import com.attendace.leopard.data.base.NotLoaded
import com.attendance.leopard.data.model.User
import com.attendace.leopard.data.repository.portfolio.PortfolioRepository
import com.attendace.leopard.util.localization.LanguageTypeEnum
import com.attendace.leopard.util.theme.language
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: AuthRepository,
    val portfolioRepository: PortfolioRepository,
) : com.attendace.leopard.data.base.BaseViewModel<ProfileViewModel.State>(State()) {
    data class State(
        val userInfo: com.attendace.leopard.data.base.LoadableData<User> = com.attendace.leopard.data.base.NotLoaded,
        val changePassword: com.attendace.leopard.data.base.LoadableData<Unit> = com.attendace.leopard.data.base.NotLoaded,
        val currentLanguageTag: LanguageTypeEnum = LanguageTypeEnum.English,
        val currentLanguageName: String = "",
        val accessToken: String = "",
        val baseUrl: String = "",
    )

    init {
        getBaseUrl()
        getToken()
        getUserInfo()
        getLocale()
    }

    private val currentLanguageTag = authRepository.getLocale().stateIn(
        scope = viewModelScope,
        initialValue = "en",
        started = SharingStarted.WhileSubscribed(5_000),
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

    private val currentLanguageName = authRepository.getLocaleName().stateIn(
        scope = viewModelScope,
        initialValue = "en",
        started = SharingStarted.WhileSubscribed(5_000),
    ).onEach {
        updateState { copy(currentLanguageName = it) }
    }

    private fun getUserInfo() {
        updateState { copy(userInfo = com.attendace.leopard.data.base.Loading) }
        viewModelScope.launch {
            authRepository.getOfflineUserInfo().collect {
                updateState { copy(userInfo = com.attendace.leopard.data.base.Loaded(it)) }
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

    fun setLocale(tag: LanguageTypeEnum, name: String) {
        viewModelScope.launch {
            authRepository.setLocale(tag.name, name)
            authRepository.changeUserLocale()

            updateState {
                copy(
                    currentLanguageName = name,
                    currentLanguageTag = tag
                )
            }
        }
    }

    private fun getToken() {
        viewModelScope.launch {
            portfolioRepository.getAccessToken().collect {
                updateState {
                    copy(
                        accessToken = it
                    )
                }
            }
        }
    }

    private fun getBaseUrl() {
        viewModelScope.launch {
            portfolioRepository.getBaseUrl().collect {
                updateState {
                    copy(
                        baseUrl = it
                    )
                }
            }
        }
    }

    fun changePassword(oldPassword: String, newPassword: String) {
        updateState { copy(changePassword = com.attendace.leopard.data.base.Loading) }
        viewModelScope.launch {
            authRepository.changePassword(oldPassword, newPassword)
                .fold(ifRight = {
                    updateState { copy(changePassword = com.attendace.leopard.data.base.Loaded(it)) }
                }, ifLeft = { failure ->
                    updateState { copy(changePassword = com.attendace.leopard.data.base.Failed(
                        failure
                    )
                    ) }
                })
        }
    }

    fun resetChangePasswordState() {
        updateState { copy(changePassword = com.attendace.leopard.data.base.NotLoaded) }
    }

}
