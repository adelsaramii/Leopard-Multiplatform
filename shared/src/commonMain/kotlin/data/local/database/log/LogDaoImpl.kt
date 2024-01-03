package com.attendace.leopard.data.local.database.log

import com.attendace.leopard.data.source.remote.model.dto.AddLogInput
import com.attendance.leopard.LogEntity
import com.attendance.leopard.LogQueries
import com.attendace.leopard.data.local.database.toAddLogInput
import com.attendace.leopard.data.local.database.toLogDto
import com.attendance.leopard.data.source.remote.model.dto.LogDto
import com.attendace.leopard.data.source.remote.model.dto.RecorderDto
import com.attendace.leopard.data.source.remote.model.dto.Type
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LogDaoImpl(
    private val logQueries: LogQueries
) : LogDao {

    override fun insertLog(logDto: LogDto) {
        insertLog(
            id = logDto.id,
            isMissed = logDto.isMissed,
            latitude = logDto.latitude,
            longitude = logDto.longitude,
            isOffline = logDto.isOffline,
            needAction = logDto.needAction,
            recorder = logDto.recorder,
            type = logDto.type,
            date = logDto.date,
            workflowStatus = logDto.workflowStatus,
            workplaceStatus = logDto.workplaceStatus
        )
    }

    override fun insertLog(
        id: String,
        isMissed: Boolean,
        latitude: Double,
        longitude: Double,
        isOffline: Boolean,
        needAction: Boolean,
        recorder: RecorderDto?,
        type: Type,
        date: String,
        workflowStatus: Type?,
        workplaceStatus: Type?
    ) {
        logQueries.insertLog(
            id = id,
            type = type,
            date = date,
            workflowStatus = workflowStatus,
            workplaceStatus = workplaceStatus,
            isMissed = isMissed,
            isOffline = isOffline,
            latitude = latitude,
            longitude = longitude,
            needAction = needAction,
            recorder = recorder
        )
    }

    override fun getAllLogs(): Flow<List<LogDto>> {
        return logQueries.selectAllLogs().asFlow().mapToList()
            .map {
                it.map {
                    it.toLogDto()
                }
            }
    }

    override fun getAllAddLogInputs(): Flow<List<AddLogInput>> {
        return logQueries.getAllAddLogInputs()
            .asFlow()
            .mapToList()
            .map {
                it.map {
                    it.toAddLogInput()
                }
            }
    }

    override fun insertAddLogInput(log: AddLogInput) {
        return logQueries.insertAddLogInput(
            date = log.date,
            latitude = log.latitude,
            longitude = log.longitude,
            isOffline = log.isOffline,
            recorder = log.recorder,
            type = log.type,
            userId = log.userId
        )
    }

    override fun insertAddLogInputs(logs: List<AddLogInput>) {
        logQueries.transaction {
            logs.forEach {
                insertAddLogInput(it)
            }
        }
    }

    override fun deleteAllAddLogInputs() {
        return logQueries.deleteAllAddLogInputs()
    }
    override fun deleteAllLogs() {
        return logQueries.deleteAllLogs()
    }

    override fun getLogById(id: String): LogEntity? {
        return logQueries.selectLogById(id).executeAsOneOrNull()
    }

    override fun insertLogs(logs: List<LogDto>) {
        logQueries.transaction {
            logs.forEach {
                insertLog(it)
            }
        }
    }
}


