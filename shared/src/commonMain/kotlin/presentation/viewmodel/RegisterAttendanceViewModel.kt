package com.attendace.leopard.presentation.viewmodel

import com.attendace.leopard.data.repository.workplace.WorkplaceRepository
import com.attendace.leopard.data.base.BaseViewModel
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.data.base.Loading
import com.attendace.leopard.data.base.NotLoaded
import com.attendace.leopard.data.model.Workplace
import com.attendace.leopard.data.model.distance
import com.attendace.leopard.data.model.toRecorderDto
import com.attendance.leopard.data.model.*
import com.attendace.leopard.data.repository.attendance.AttendanceRepository
import com.attendace.leopard.data.source.remote.model.dto.AddLogInput
import com.attendance.leopard.data.source.remote.model.dto.LogType
import com.attendace.leopard.data.source.remote.model.dto.Type
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class RegisterAttendanceViewModel(
    private val attendanceRepository: AttendanceRepository,
    private val workplaceRepository: WorkplaceRepository
) : com.attendace.leopard.data.base.BaseViewModel<RegisterAttendanceViewModel.State>(
    State()
) {

    data class Location(
        val lat: Double, val lang: Double
    )

    data class State(
        val selectedWorkplace: Workplace? = null,
        val nearWorkplaces: com.attendace.leopard.data.base.LoadableData<List<Workplace>> = com.attendace.leopard.data.base.NotLoaded,
        val todayLogs: com.attendace.leopard.data.base.LoadableData<DayLog> = com.attendace.leopard.data.base.NotLoaded,
        val currentLocation: Location? = null,
        val addLogErrorMessage: String? = null,
        val enterLoading: Boolean = false,
        val exitLoading: Boolean = false,
    )

    init {
//        getNearWorkplaces()
//        observeTodayLogs()
        getTodayLogs()

    }

    fun observeTodayLogs() {
        viewModelScope.launch {
            attendanceRepository.getDayLogFlow().fold(ifLeft = {},
                ifRight = {
                    it.collect {
                        updateState {
                            copy(todayLogs = com.attendace.leopard.data.base.Loaded(it))
                        }
                    }
                })
        }
    }

    fun getNearWorkplaces() {
        updateState {
            copy(nearWorkplaces = com.attendace.leopard.data.base.Loading)
        }
        viewModelScope.launch {
            workplaceRepository.getWorkplaces().collect { workplaces ->
                updateState {
                    copy(
                        nearWorkplaces = com.attendace.leopard.data.base.Loaded(workplaces
                            .filter {
                                (it.radius + 150 > it.distance(
                                    currentState.currentLocation?.lat ?: 0.0,
                                    currentState.currentLocation?.lang ?: 0.0
                                ) && !it.isDeleted)
                            }
                        )
                    )
                }
            }
        }
    }

    fun getTodayLogs() {
        updateState {
            copy(todayLogs = com.attendace.leopard.data.base.Loading)
        }
        viewModelScope.launch {
            attendanceRepository.fetchTodayLogs(Clock.System.now()).fold(
                ifRight = {
                    updateState {
                        copy(todayLogs = com.attendace.leopard.data.base.Loaded(it))
                    }
                },
                ifLeft = {
                    observeTodayLogs()
                }
            )
        }
    }


    fun addEnterLog(date: String) {
        updateState { copy(enterLoading = true) }
        currentState.selectedWorkplace?.let { workplace ->
            viewModelScope.launch {
                attendanceRepository.sendOnlineLog(
                    createAddLogInput(
                        workplace = workplace, logType = LogType.Enter, date
                    )
                ).fold(
                    ifLeft = {
                        updateState {
                            copy(
                                addLogErrorMessage = it.getErrorMessage(),
                                enterLoading = false
                            )
                        }
                        getTodayLogs()
                    },
                    ifRight = {
                        updateState { copy(enterLoading = false) }
                        getTodayLogs()
                    }
                )
            }
        } ?: run {
            //todo no workplace is selected
        }
    }

    fun addExitLog(date: String) {
        updateState { copy(exitLoading = true) }
        currentState.selectedWorkplace?.let { workplace ->
            viewModelScope.launch {
                attendanceRepository.sendOnlineLog(
                    createAddLogInput(
                        workplace = workplace, logType = LogType.Exit, date
                    )
                ).fold(
                    ifLeft = {
                        updateState {
                            copy(
                                addLogErrorMessage = it.getErrorMessage(),
                                exitLoading = false
                            )
                        }
                        getTodayLogs()
                    },
                    ifRight = {
                        updateState { copy(exitLoading = false) }
                        getTodayLogs()
                    }
                )
            }
        } ?: run {
            //todo no workplace is selected
        }
    }

    fun sendOfflineLogs() {
        viewModelScope.launch {
            attendanceRepository.sendOfflineSavedLogs()
        }
    }

    fun setCurrentLocation(lat: Double, lang: Double) {
        updateState {
            copy(currentLocation = Location(lat, lang))
        }
    }

    fun setSelectedWorkplace(workplace: Workplace) {
        updateState {
            copy(selectedWorkplace = workplace)
        }
    }

    private fun createAddLogInput(
        workplace: Workplace,
        logType: LogType,
        date: String
    ) = AddLogInput(
        date = date,
        latitude = workplace.latitude,
        longitude = workplace.longitude,
        isOffline = false,
        recorder = workplace.recorder?.toRecorderDto(),
        type = Type.fromLogType(logType),
        userId = ""
    )

    fun clearLogErrorMessage() {
        updateState { copy(addLogErrorMessage = null) }
    }

}