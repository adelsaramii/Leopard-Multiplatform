package com.attendace.leopard.presentation.viewmodel

import com.attendace.leopard.data.repository.auth.AuthRepository
import com.attendace.leopard.data.base.BaseViewModel
import com.attendace.leopard.data.base.Failed
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.data.base.Loading
import com.attendace.leopard.data.base.NotLoaded
import com.attendance.leopard.data.model.Attendance
import com.attendace.leopard.data.model.Day
import com.attendance.leopard.data.model.Subordinate
import com.attendance.leopard.data.model.SummaryCategory
import com.attendance.leopard.data.model.User
import com.attendance.leopard.data.model.WorkPeriod
import com.attendace.leopard.data.repository.daily.DailyRepository
import com.attendace.leopard.data.repository.monthly.MonthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel constructor(
    private val authRepository: AuthRepository,
    private val monthRepository: MonthRepository,
    private val dailyRepository: DailyRepository,
) : BaseViewModel<HomeViewModel.State>(State()) {

    data class State(
        val shouldNavigateToAuth: Boolean = false,
        val isRefreshing: Boolean = false,
        val selectedWorkPeriod: WorkPeriod? = null,
        val selectedSubordinate: LoadableData<Subordinate?> = NotLoaded,
        val selectedDay: Day? = null,

        val userInfo: LoadableData<User> = NotLoaded,
        val subordinates: LoadableData<List<Subordinate>> = NotLoaded,
        val workPeriods: LoadableData<List<WorkPeriod>> = NotLoaded,
        val monthlySummary: LoadableData<SummaryCategory> = NotLoaded,
        val monthlyCalendarItems: LoadableData<List<Day>> = NotLoaded,

        val dailySummary: LoadableData<SummaryCategory> = NotLoaded,
        val dailyAttendance: LoadableData<List<Pair<Attendance, Attendance>>> = NotLoaded,

        val selectWeekPosition: Int = 0,

        val baseUrl: String? = null,
        val accessToken: String? = null,
    )

    init {
        setupIsLoggedInCollector()
        getBaseUrl()
        getAccessToken()
    }

    fun getBaseUrl() {
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


    fun getAccessToken() {
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

    fun setupIsLoggedInCollector() {
        updateState {
            copy(monthlySummary = Loading, monthlyCalendarItems = Loading)
        }
        authRepository.isLoggedIn().stateIn(
            scope = viewModelScope,
            initialValue = null,
            started = SharingStarted.WhileSubscribed(5_000)
        ).onEach { isLoggedIn ->
            isLoggedIn?.let {
                if (isLoggedIn) {
                    getWorkPeriods()
                    getUserInfo()
                    getSubordinates()
                } else {
                    updateState { copy(shouldNavigateToAuth = true) }
                }
            }
        }.launchIn(viewModelScope)
    }

    fun refresh() { //todo add a refresh for daily too?
        updateState { copy(isRefreshing = true) }
        getData()
        viewModelScope.launch {
            delay(500)
            updateState { copy(isRefreshing = false) }
        }
        selectWeekPosition(0)
    }

    private fun getData() {
        getMonthlySummary(
            personId = currentState.selectedSubordinate.data?.personId,
            workperiodId = currentState.selectedWorkPeriod?.id ?: ""
        )
        getWorkperiodCalendar()
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            authRepository.getUserInfo().fold(ifRight = { userInfo ->
                updateState { copy(userInfo = Loaded(userInfo)) }
            }, ifLeft = {
                authRepository.getOfflineUserInfo().collect { userInfo ->
                    updateState { copy(userInfo = Loaded(userInfo)) }
                }
                updateState {
                    copy(monthlySummary = Failed(it), monthlyCalendarItems = Failed(
                        it
                    )
                    )
                }
            })
        }
    }

    private fun getMonthlySummary(personId: String? = null, workperiodId: String) {
        updateState { copy(monthlySummary = Loading) }
        viewModelScope.launch {
            monthRepository.getMonthlySummary(personId, workperiodId)
                .fold(ifRight = { monthlySummary ->
                    updateState { copy(monthlySummary = Loaded(
                        monthlySummary
                    )
                    ) }
                }, ifLeft = { failure ->
                    updateState { copy(monthlySummary = Failed(
                        failure
                    )
                    ) }
                })
        }
    }

    fun selectSubordinate(subordinate: Subordinate) {
        updateState { copy(selectedSubordinate = Loaded(subordinate)) }
        getData()
    }


    fun selectWorkPeriod(workPeriod: WorkPeriod) {
        updateState { copy(selectedWorkPeriod = workPeriod) }
        getData()
    }

    fun selectDay(day: Day) {
        updateState { copy(selectedDay = day) }
        getDailySummary()
        getDailyAttendance()
    }

    fun getWorkPeriods() {
        viewModelScope.launch {
            monthRepository.getWorkPeriods().fold(ifRight = { response ->
                updateState {
                    copy(workPeriods = Loaded(response))
                }
                if (response.isNotEmpty() && currentState.selectedWorkPeriod == null) {
                    val toShowWorkPeriod =
                        response.firstOrNull { it.isCurrentWorkPeriod } ?: response.first()
                    selectWorkPeriod(toShowWorkPeriod)
                }
            }, ifLeft = {
                updateState {
                    copy(workPeriods = Failed(it))
                }
            })
        }
    }

    fun getSubordinates(
        searchValue: String = "", pageNumber: Int = 1, pageSize: Int = 1000
    ) {
        viewModelScope.launch {
            monthRepository.getSubordinates(searchValue, pageNumber, pageSize)
                .fold(ifRight = { subordinates ->
                    updateState {
                        copy(subordinates = Loaded(subordinates))
                    }
                }, ifLeft = {
                    updateState {
                        copy(subordinates = Failed(it))
                    }
                })
        }
    }

    fun getWorkperiodCalendar() {
        updateState { copy(monthlyCalendarItems = Loading) }
        viewModelScope.launch {
            currentState.selectedWorkPeriod?.id?.let {
                monthRepository.getWorkperiodCalendar(
                    personId = currentState.selectedSubordinate.data?.personId, workperiodId = it
                ).fold(ifRight = { calendarItems ->
                    updateState { copy(monthlyCalendarItems = Loaded(
                        calendarItems
                    )
                    ) }
                }, ifLeft = { failure ->
                    updateState { copy(monthlyCalendarItems = Failed(
                        failure
                    )
                    ) }
                })
            }
        }
    }

    fun getDailySummary() {
        updateState { copy(dailySummary = Loading) }
        viewModelScope.launch {
            dailyRepository.getDailySummery(
                date = currentState.selectedDay?.date ?: "",
                personId = currentState.selectedSubordinate.data?.personId,
                workperiodId = currentState.selectedWorkPeriod?.id ?: "",
            ).fold(ifRight = { summaryCategory ->
                updateState { copy(dailySummary = Loaded(
                    summaryCategory
                )
                ) }
            }, ifLeft = { failure ->
                updateState { copy(dailySummary = Failed(failure)) }
            })
        }
    }

    fun getDailyAttendance() {
        updateState { copy(dailyAttendance = Loading) }
        viewModelScope.launch {
            dailyRepository.getDailyAttndance(
                date = stateStateFlow.value.selectedDay?.date ?: "",
                personId = currentState.selectedSubordinate.data?.personId,
                workperiodId = currentState.selectedWorkPeriod?.id ?: ""
            ).fold(ifRight = { dailyAttendance ->
                updateState { copy(dailyAttendance = Loaded(
                    dailyAttendance
                )
                ) }
            }, ifLeft = { failure ->
                updateState { copy(dailyAttendance = Failed(failure)) }
            })
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun selectWeekPosition(weekPosition: Int) {
        updateState { copy(selectWeekPosition = weekPosition) }
        print("week position ---> $weekPosition")
    }

    override fun onCleared() {
        super.onCleared()

    }

    fun run(refresh: () -> Unit) {
        refresh.invoke()
    }

}