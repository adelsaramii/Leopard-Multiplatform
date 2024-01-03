package com.attendace.leopard.data.repository.attendance

import arrow.core.Either
import arrow.core.computations.either
import com.attendace.leopard.data.local.database.log.LogDao
import com.attendace.leopard.data.local.setting.AuthSettings
import com.attendace.leopard.data.source.remote.model.dto.toLog
import com.attendace.leopard.data.source.remote.model.dto.AddLogInput
import com.attendance.leopard.data.model.DayLog
import com.attendance.leopard.data.model.Log
import com.attendance.leopard.data.model.PairLog
import com.attendace.leopard.data.model.Workplace
import com.attendance.leopard.data.source.remote.model.dto.*
import com.attendace.leopard.data.source.remote.service.attendance.AttendanceService
import com.attendace.leopard.util.error.Failure
import com.attendace.leopard.util.helper.CommonFlow
import com.attendace.leopard.util.helper.asCommonFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

class AttendanceRepositoryImpl constructor(
    private val attendanceService: AttendanceService,
    private val dao: LogDao,
    private val settings: AuthSettings
) : AttendanceRepository {

    override suspend fun fetchTodayLogs(date: Instant): Either<Failure.NetworkFailure, DayLog> {
        attendanceService.getTodayLogs(date.toString()).fold(ifLeft = {
            //todo
        }, ifRight = {
            dao.deleteAllLogs()
            dao.insertLogs(it.flattenToLog())
        })
        return attendanceService.getTodayLogs(date.toString()).map { it.toDomainModel() }
    }

    override suspend fun getDayLogFlow(): Either<Failure.NetworkFailure, CommonFlow<DayLog>> { //todo add either
        return either {
            dao.getAllLogs().combine(dao.getAllAddLogInputs()) { logs, logInputs ->
                val onlineLogs = logs.map { it.toLog() }
                val offlineLogs = logInputs.map { it.toLog() }
                onlineLogs + offlineLogs
            }.map {
                it.parseToDayLog()
            }.asCommonFlow()
        }
    }


    override suspend fun sendOfflineSavedLogs() {
        val savedOfflineAddLogInputs = dao.getAllAddLogInputs().firstOrNull().orEmpty()

        if (savedOfflineAddLogInputs.isNotEmpty()) attendanceService.addLogs(
            isOffline = true, logs = savedOfflineAddLogInputs
        ).fold(ifRight = {
//                fetchTodayLogs(Clock.System.now())
            dao.deleteAllAddLogInputs()
        }, ifLeft = {
            //todo
        })
    }


    override suspend fun sendOnlineLog(log: AddLogInput): Either<Failure, Unit> {


        return attendanceService.addLogs(
            isOffline = false, logs = listOf(log)
        ).fold(ifRight = {
            fetchTodayLogs(Clock.System.now())
            sendOfflineSavedLogs()
            Either.Right(Unit)
        }, ifLeft = {
            when (it) {
                is Failure.NetworkFailure.UnknownException -> { // assumption: no internet access
                    dao.insertAddLogInput(log)
                    Either.Right(Unit)
                }

                else -> {
                    Either.Left(it)
                }
            }
        })
    }

}

fun List<Log>.parseToDayLog(): DayLog {
    return DayLog(
        this.count { !it.isMissed }, this.count(), true, logs = pairLog(this)
    )
}

fun pairLog(logs: List<Log>): List<PairLog> {

    val pairList = mutableListOf<PairLog>()
    if (logs.isNotEmpty()) {
        val date = logs.first().date
        var firstEnter = false
        var escapeNextIndex = false
        logs.forEachIndexed lit@{ index, a ->
            if (escapeNextIndex) {
                escapeNextIndex = false
                return@lit
            }
            if (!firstEnter) {
                if (a.logType == LogType.Exit) {
                    pairList.add(
                        PairLog(
                            date = date,
                            hasMissedLog = true,
                            workplace = Workplace(
                                recorder = a.recorder,
                                latitude = a.recorder?.latitude ?: 0.0,
                                longitude = a.recorder?.longitude ?: 0.0,
                                radius = a.recorder?.radius ?: 0.0,
                                name = a.recorder?.name ?: "",
                                primaryRecorderId = a.recorder?.id ?: ""
                            ),
                            enter = Log(
                                isMissed = true,
                                logType = LogType.Enter,
                                date = date,
                                isOffline = false,
                                latitude = a.latitude,
                                longitude = a.longitude
                            ),
                            exit = a,
                        )
                    )
                } else {
                    firstEnter = true
                }
            }
            if (firstEnter && index + 1 < logs.size && a.logType == LogType.Enter && a.recorder?.id == logs[index + 1].recorder?.id && logs[index + 1].logType == LogType.Exit) {
                escapeNextIndex = true
                pairList.add(
                    PairLog(
                        date = date,
                        hasMissedLog = true,
                        workplace = Workplace(
                            recorder = a.recorder,
                            latitude = a.recorder?.latitude ?: 0.0,
                            longitude = a.recorder?.longitude ?: 0.0,
                            radius = a.recorder?.radius ?: 0.0,
                            name = a.recorder?.name ?: "",
                            primaryRecorderId = a.recorder?.id ?: ""
                        ),
                        enter = a,
                        exit = logs[index + 1],
                    )
                )
            } else {
                if (a.logType == LogType.Enter) {
                    pairList.add(
                        PairLog(
                            date = date,
                            hasMissedLog = true,
                            workplace = Workplace(
                                recorder = a.recorder,
                                latitude = a.recorder?.latitude ?: 0.0,
                                longitude = a.recorder?.longitude ?: 0.0,
                                radius = a.recorder?.radius ?: 0.0,
                                name = a.recorder?.name ?: "",
                                primaryRecorderId = a.recorder?.id ?: ""
                            ),
                            enter = a,
                            exit = Log(
                                isMissed = true,
                                logType = LogType.Enter,
                                date = date,
                                isOffline = false,
                                latitude = a.latitude,
                                longitude = a.longitude
                            ),
                        )
                    )
                } else {
                    pairList.add(
                        PairLog(
                            date = date,
                            hasMissedLog = true,
                            workplace = Workplace(
                                recorder = a.recorder,
                                latitude = a.recorder?.latitude ?: 0.0,
                                longitude = a.recorder?.longitude ?: 0.0,
                                radius = a.recorder?.radius ?: 0.0,
                                name = a.recorder?.name ?: "",
                                primaryRecorderId = a.recorder?.id ?: ""
                            ),
                            exit = a,
                            enter = Log(
                                isMissed = true,
                                logType = LogType.Enter,
                                date = date,
                                isOffline = false,
                                latitude = a.latitude,
                                longitude = a.longitude
                            ),
                        )
                    )
                }
            }
        }
    }


    return pairList
}