package com.attendace.leopard.presentation.viewmodel

import com.attendace.leopard.data.repository.auth.AuthRepository
import com.attendace.leopard.data.base.BaseViewModel
import com.attendace.leopard.data.base.Failed
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.data.base.Loading
import com.attendace.leopard.data.base.NotLoaded
import com.attendace.leopard.data.model.Bulk
import com.attendance.leopard.data.model.User
import com.attendance.leopard.data.model.WorkPeriod
import com.attendace.leopard.data.repository.bulk.BulkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BulkViewModel(
    val repository: BulkRepository,
    val authRepository: AuthRepository,
) : com.attendace.leopard.data.base.BaseViewModel<BulkViewModel.State>(
    State()
) {

    data class State(
        val bulks: com.attendace.leopard.data.base.LoadableData<List<Bulk>> = com.attendace.leopard.data.base.NotLoaded,
        val personId: String? = null,
        val workPeriodId: String = "",
        val codeId: String = "",
        val isRefreshing: Boolean = false,
        val workPeriods: com.attendace.leopard.data.base.LoadableData<List<WorkPeriod>> = com.attendace.leopard.data.base.NotLoaded,
        val selectedWorkPeriod: WorkPeriod? = null,
        val userInfo: com.attendace.leopard.data.base.LoadableData<User> = com.attendace.leopard.data.base.NotLoaded,
        var selectedCount: Int = 0
    )

    init {
        getUserInfo()
    }


    private val selectedWorkPeriod = MutableStateFlow<WorkPeriod?>(null)

    private fun getMonthlyDetails() {
        val oldItems = currentState.bulks.data
        if (currentState.bulks is com.attendace.leopard.data.base.NotLoaded)
            updateState {
                copy(bulks = com.attendace.leopard.data.base.Loading)
            }
        viewModelScope.launch {
            repository.getMonthlyDetails(
                personId = currentState.personId,
                workPeriodId = currentState.workPeriodId,
                codeId = currentState.codeId
            ).fold(ifRight = {
                updateState { copy(isRefreshing = false) }
                val newItems = it
                newItems.forEach { item ->
                    item.isSelected =
                        oldItems?.firstOrNull { it.id == item.id }?.isSelected
                            ?: false
                }
                updateState {
                    copy(
                        bulks = com.attendace.leopard.data.base.Loaded(newItems)
                    )
                }
                print("selected bulk item :::${newItems.filter { it.isSelected }.size}")
            }, ifLeft = {
                updateState { copy(isRefreshing = false) }
                updateState {
                    copy(
                        bulks = com.attendace.leopard.data.base.Failed(it)
                    )
                }
            })
        }
    }

    fun changeAllSelection(b: Boolean) {
        val newItems = currentState.bulks.data ?: emptyList()
        updateState { copy(bulks = com.attendace.leopard.data.base.Loaded(newItems.map {
            it.copy(
                isSelected = b
            )
        })) }
    }

    fun changeSelection(selectedItem: Bulk) {
        val newItems = currentState.bulks.data ?: emptyList()
        updateState {
            copy(bulks = com.attendace.leopard.data.base.Loaded(newItems.map {
                if (it.id == selectedItem.id)
                    it.copy(isSelected = !selectedItem.isSelected)
                else it
            }))
        }
    }

    fun refresh() {
        viewModelScope.launch {
            updateState {
                copy(
                    isRefreshing = true
                )
            }
            getMonthlyDetails()
        }
    }

    fun setInitialState(workPeriodId: String, codeId: String,personId: String?=null) {
        updateState {
            copy(codeId = codeId, workPeriodId = workPeriodId , personId = personId)
        }
        getMonthlyDetails()

    }

    fun selectWorkPeriod(workPeriod: WorkPeriod) {
        updateState {
            copy(
                workPeriodId = workPeriod.id,
                selectedWorkPeriod = workPeriod,
            )
        }
        getMonthlyDetails()
    }

    fun getWorkPeriods() {
        viewModelScope.launch {
            repository.getWorkPeriods().fold(
                ifRight = { response ->
                    updateState {
                        copy(workPeriods = com.attendace.leopard.data.base.Loaded(response))
                    }
                    if (response.isNotEmpty() && selectedWorkPeriod.value == null) {
                        val toShowWorkPeriod =
                            response.firstOrNull { it.isCurrentWorkPeriod } ?: response.first()

                        selectedWorkPeriod.update { toShowWorkPeriod }
                    }
                },
                ifLeft = {
                    updateState {
                        copy(workPeriods = com.attendace.leopard.data.base.Failed(it))
                    }
                }
            )
        }
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            authRepository.getOfflineUserInfo().collect { userInfo ->
                updateState { copy(userInfo = com.attendace.leopard.data.base.Loaded(userInfo)) }
            }
        }
    }


}
