package com.attendace.leopard.data.local.database.log

import com.attendace.leopard.data.source.remote.model.dto.AddLogInput
import com.attendance.leopard.LogEntity
import com.attendance.leopard.data.source.remote.model.dto.LogDto
import com.attendace.leopard.data.source.remote.model.dto.RecorderDto
import com.attendace.leopard.data.source.remote.model.dto.Type
import kotlinx.coroutines.flow.Flow

interface LogDao {

    fun insertLog(logDto: LogDto)

    fun getAllLogs(): Flow<List<LogDto>>

    fun getAllAddLogInputs(): Flow<List<AddLogInput>>

    fun insertAddLogInput(log: AddLogInput)
    fun insertAddLogInputs(logs: List<AddLogInput>)

    fun deleteAllAddLogInputs()

    fun getLogById(id: String): LogEntity?

    fun insertLogs(logs: List<LogDto>)

    fun insertLog(
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
    )

    fun deleteAllLogs()
}