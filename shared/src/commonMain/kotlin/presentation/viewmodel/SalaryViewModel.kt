package com.attendace.leopard.presentation.viewmodel

import com.attendace.leopard.data.repository.auth.AuthRepository
import com.attendace.leopard.data.base.BaseViewModel
import com.attendace.leopard.data.base.Failed
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.data.base.Loading
import com.attendace.leopard.data.base.NotLoaded
import com.attendance.leopard.data.model.Salary
import com.attendance.leopard.data.model.Subordinate
import com.attendance.leopard.data.model.User
import com.attendance.leopard.data.model.WorkPeriod
import com.attendance.leopard.data.model.toSubordinate
import com.attendace.leopard.data.repository.monthly.MonthRepository
import com.attendace.leopard.data.repository.salary.SalaryRepository
import kotlinx.coroutines.launch

class SalaryViewModel(
    private val salaryRepository: SalaryRepository,
    private val authRepository: AuthRepository,
    private val monthRepository: MonthRepository
) : com.attendace.leopard.data.base.BaseViewModel<SalaryViewModel.State>(State()) {

    data class State(
        val data: com.attendace.leopard.data.base.LoadableData<String> = com.attendace.leopard.data.base.NotLoaded,
        val subordinates: com.attendace.leopard.data.base.LoadableData<List<Subordinate>> = com.attendace.leopard.data.base.NotLoaded,
        val selectedSubordinate: com.attendace.leopard.data.base.LoadableData<Subordinate?> = com.attendace.leopard.data.base.NotLoaded,
        val baseUrl: String? = null,
        val accessToken: String? = null,
        val userInfo: com.attendace.leopard.data.base.LoadableData<User> = com.attendace.leopard.data.base.NotLoaded,
        val selectedWorkPeriod: WorkPeriod? = null,
        val salary: com.attendace.leopard.data.base.LoadableData<Salary> = com.attendace.leopard.data.base.NotLoaded,
        val workPeriods: com.attendace.leopard.data.base.LoadableData<List<WorkPeriod>> = com.attendace.leopard.data.base.NotLoaded,

        )

    init {
        getUserInfo()
        getBaseUrl()
        getAccessToken()
        getWorkPeriods()
    }


    fun getSalary() {
        updateState { copy(salary = com.attendace.leopard.data.base.Loading) }
        viewModelScope.launch {
            currentState.selectedWorkPeriod?.id?.let {
                salaryRepository.getSalary(
                    personId = currentState.selectedSubordinate.data?.personId,
                    workPeriodId = it
                ).fold(ifLeft = {
                    updateState { copy(salary = com.attendace.leopard.data.base.Failed(it)) }
                }, ifRight = {
                    updateState { copy(salary = com.attendace.leopard.data.base.Loaded(it)) }
                })
            }
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

    fun selectSubordinate(subordinate: Subordinate) {
        updateState { copy(selectedSubordinate = com.attendace.leopard.data.base.Loaded(subordinate)) }
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


    fun getWorkPeriods() {
        updateState {
            copy(workPeriods = com.attendace.leopard.data.base.Loading)
        }
        viewModelScope.launch {
            monthRepository.getWorkPeriods().fold(ifRight = { response ->
                updateState {
                    copy(workPeriods = com.attendace.leopard.data.base.Loaded(response))
                }
                if (response.isNotEmpty() && currentState.selectedWorkPeriod == null) {
                    val toShowWorkPeriod =
                        response.firstOrNull { it.isCurrentWorkPeriod } ?: response.first()
                    selectWorkPeriod(toShowWorkPeriod)
                }
            }, ifLeft = {
                updateState {
                    copy(workPeriods = com.attendace.leopard.data.base.Failed(it))
                }
            })
        }
    }

    fun selectWorkPeriod(workPeriod: WorkPeriod) {
        updateState { copy(selectedWorkPeriod = workPeriod) }
        getSalary()
    }

}