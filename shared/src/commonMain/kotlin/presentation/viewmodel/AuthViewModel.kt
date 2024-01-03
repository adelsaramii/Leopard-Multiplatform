package com.attendace.leopard.presentation.viewmodel


import com.attendace.leopard.data.repository.auth.AuthRepository
import com.attendace.leopard.data.repository.workplace.WorkplaceRepository
import com.attendace.leopard.data.base.Failed
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.data.base.Loading
import com.attendace.leopard.data.base.NotLoaded
import com.attendace.leopard.util.error.Failure
import com.attendace.leopard.util.helper.persianToEnglish
import com.attendace.leopard.util.localization.LanguageTypeEnum
import com.attendace.leopard.util.theme.language
import com.attendance.leopard.data.model.Initialize
import com.attendance.leopard.data.model.Login
import com.attendance.leopard.data.model.User
import com.attendace.leopard.data.source.remote.model.dto.AddDeviceInput
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.attendace.leopard.data.base.BaseViewModel
import com.attendace.leopard.di.loge

class AuthViewModel constructor(
    private val repository: AuthRepository,
    private val workplaceRepository: WorkplaceRepository
) : BaseViewModel<AuthViewModel.State>(State()) {

    data class State(
        val userInfo: LoadableData<User> = NotLoaded,
        val initializeFailure: LoadableData<Failure> = NotLoaded,

        val initializeResponse: LoadableData<Initialize> = NotLoaded,
        val loginResponse: LoadableData<Login> = NotLoaded,

        val loginFlowPage: Int = 0,
        val snackbarError: Failure? = null,

        val currentLanguageTag: LanguageTypeEnum = LanguageTypeEnum.English,
        val currentLanguageName: String = "",
        val baseUrl: String = "",
        val deviceToken: String = ""
    )

    init {
        getBaseUrl()
        getLocale()
        getDeviceToken()
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


    private val _baseUrl = repository.getBaseUrl()
        .stateIn(
            scope = viewModelScope,
            initialValue = "",
            started = SharingStarted.WhileSubscribed(5_000)
        )


    fun initialize(url: String) {
        updateState { copy(initializeResponse = Loading) }
        viewModelScope.launch {
            repository.initialize(url).fold(
                ifRight = {
                    updateState {
                        copy(
                            loginFlowPage = 1,
                            initializeResponse = Loaded(it)
                        )
                    }
                }, ifLeft = {
                    var finalUrl = ""
                    when {
                        !url.lowercase().startsWith("https") && url.lowercase()
                            .startsWith("http") -> {
                            finalUrl = url.lowercase().replace("http", "https")
                            initialize(finalUrl)
                        }

                        !url.lowercase().startsWith("https") -> {
                            finalUrl = "https://$url"
                            initialize(finalUrl)
                        }

                        else -> {
                            updateState {
                                copy(
                                    snackbarError = it,
                                    initializeResponse = Failed(it)
                                )
                            }
                        }
                    }

                }
            )
        }
    }

    fun login(username: String, password: String) {
        updateState { copy(loginResponse = Loading) }
        viewModelScope.launch {
            repository.login(username, password).fold(
                ifRight = {
                    getUserInfo()
                    fetchWorkplaceChanges()
                    updateState { copy(loginResponse = Loaded(it)) }
                }, ifLeft = {
                    updateState {
                        copy(
                            snackbarError = it,
                            loginResponse = Failed(it)
                        )
                    }
                }
            )
        }
    }

    private fun fetchWorkplaceChanges() {
        viewModelScope.launch {
            workplaceRepository.getWorkplaceChanges().fold(
                ifLeft = {},
                ifRight = {}
            )
        }
    }

    fun setLocale(tag: LanguageTypeEnum, name: String) {
        viewModelScope.launch {
            repository.setLocale(tag.name, name)
            repository.changeUserLocale()

            updateState {
                copy(
                    currentLanguageName = name,
                    currentLanguageTag = tag
                )
            }
        }
    }

    private fun getUserInfo() {
        updateState { copy(userInfo = Loading) }
        viewModelScope.launch {
            repository.getUserInfo().fold(
                ifRight = {
                    updateState { copy(userInfo = Loaded(it)) }
                },
                ifLeft = {
                    updateState { copy(userInfo = Failed(it)) }
                }
            )
        }
    }

    fun getBaseUrl() {
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

    fun setLoginFlowPage(index: Int) {
        updateState { copy(loginFlowPage = index) }
    }

    fun addDeviceInfo(deviceInput: AddDeviceInput) {
        viewModelScope.launch {
            repository.addDeviceInfo(deviceInput)
        }
    }

    private fun getDeviceToken() {
        viewModelScope.launch {
            repository.getDeviceToken().collect {
                updateState { copy(deviceToken = it) }
            }
        }
    }
}

