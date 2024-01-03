package com.attendace.leopard.presentation.viewmodel

import com.attendace.leopard.data.repository.auth.AuthRepository
import com.attendace.leopard.data.base.BaseViewModel
import com.attendace.leopard.data.base.Failed
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.data.base.Loading
import com.attendace.leopard.data.base.NotLoaded
import com.attendance.leopard.data.model.IndexCard
import com.attendance.leopard.data.model.Subordinate
import com.attendance.leopard.data.model.User
import com.attendance.leopard.data.model.toSubordinate
import com.attendace.leopard.data.repository.index_card.IndexCardRepository
import kotlinx.coroutines.launch

class IndexCardViewModel(
    private val repository: IndexCardRepository,
    private val authRepository: AuthRepository,
) : com.attendace.leopard.data.base.BaseViewModel<IndexCardViewModel.State>(State()) {

    data class State(
        val indexCards: com.attendace.leopard.data.base.LoadableData<List<IndexCard>> = com.attendace.leopard.data.base.NotLoaded,
        val selectedSubordinate: com.attendace.leopard.data.base.LoadableData<Subordinate?> = com.attendace.leopard.data.base.NotLoaded,
        val userInfo: com.attendace.leopard.data.base.LoadableData<User> = com.attendace.leopard.data.base.NotLoaded,
        val subordinates: com.attendace.leopard.data.base.LoadableData<List<Subordinate>> = com.attendace.leopard.data.base.NotLoaded,
        val baseUrl: String? = null,
        val accessToken: String? = null,
    )

    init {
        getUserInfo()
        getIndexCards()
        getBaseUrl()
        getAccessToken()
    }

    private fun getBaseUrl() {
        viewModelScope.launch {
            authRepository.getBaseUrl().collect {
                updateState {
                    copy(
                        baseUrl = it
                    )
                }
            }
        }
    }


    private fun getAccessToken() {
        viewModelScope.launch {
            authRepository.getAccessToken().collect {
                updateState {
                    copy(
                        accessToken = it
                    )
                }
            }
        }
    }

    fun getIndexCards() {
        updateState {
            copy(
                indexCards = com.attendace.leopard.data.base.Loading
            )
        }
        viewModelScope.launch {
            repository.getIndexCards(currentState.selectedSubordinate.data?.personId)
                .fold(ifRight = {
                    updateState {
                        copy(
                            indexCards = com.attendace.leopard.data.base.Loaded(it)
                        )
                    }
                }, ifLeft = {
                    updateState {
                        copy(
                            indexCards = com.attendace.leopard.data.base.Failed(it)
                        )
                    }
                })
        }
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            authRepository.getUserInfo().fold(ifRight = { userInfo ->
                updateState { copy(userInfo = com.attendace.leopard.data.base.Loaded(userInfo), selectedSubordinate = com.attendace.leopard.data.base.Loaded(
                    userInfo.toSubordinate()
                )
                ) }
                if (userInfo.hasSubordinate) getSubordinates()
            }, ifLeft = {
                authRepository.getOfflineUserInfo().collect { userInfo ->
                    updateState { copy(userInfo = com.attendace.leopard.data.base.Loaded(userInfo)) }
                }
            })
        }
    }

    fun getSubordinates(
        searchValue: String = "", pageNumber: Int = 1, pageSize: Int = 1000
    ) {
        viewModelScope.launch {
            authRepository.getSubordinates(searchValue, pageNumber, pageSize)
                .fold(ifRight = { subordinates ->
                    updateState {
                        copy(subordinates = com.attendace.leopard.data.base.Loaded(subordinates))
                    }
                }, ifLeft = {
                    updateState {
                        copy(subordinates = com.attendace.leopard.data.base.Failed(it))
                    }
                })
        }
    }


    fun selectSubordinate(subordinate: Subordinate) {
        updateState { copy(selectedSubordinate = com.attendace.leopard.data.base.Loaded(subordinate)) }
        getIndexCards()
    }

}