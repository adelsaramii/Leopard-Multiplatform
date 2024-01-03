package com.attendace.leopard.data.local.database.workplace

import com.attendace.leopard.data.source.remote.model.dto.RecorderDto
import com.attendace.leopard.data.source.remote.model.dto.WorkplaceDto
import kotlinx.coroutines.flow.Flow

interface WorkplaceDao {

    fun insertWorkplace(workplace: WorkplaceDto)

    fun insertRecorder(recorderDto: RecorderDto, workplaceId: String)

    fun insertWorkplaces(workplaces: List<WorkplaceDto>)

    fun getAllWorkplaces(): Flow<List<WorkplaceDto>>

    fun deleteWorkplaces()
}