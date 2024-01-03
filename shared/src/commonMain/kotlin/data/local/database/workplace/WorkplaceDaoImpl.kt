package com.attendace.leopard.data.local.database.workplace

import com.attendance.leopard.WorkplaceQueries
import com.attendace.leopard.data.source.remote.model.dto.RecorderDto
import com.attendace.leopard.data.source.remote.model.dto.WorkplaceDto
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WorkplaceDaoImpl(
    private val workplaceQueries: WorkplaceQueries,
) : WorkplaceDao {

    override fun insertWorkplace(workplace: WorkplaceDto) {
        workplaceQueries.transaction {
            workplaceQueries.insertWorkplace(
                id = workplace.id,
                name = workplace.name,
                isDeleted = workplace.isDeleted,
                latitude = workplace.latitude,
                longitude = workplace.longitude,
                radius = workplace.radius,
            )
            workplace.recorders.forEach {
                insertRecorder(recorderDto = it, workplaceId = workplace.id)
            }
        }

    }

    override fun insertRecorder(recorderDto: RecorderDto, workplaceId: String) {
        workplaceQueries.insertRecorder(
            id = recorderDto.id,
            name = recorderDto.name,
            radius = recorderDto.radius,
            latitude = recorderDto.latitude,
            longitude = recorderDto.longitude,
            isDeleted = recorderDto.isDeleted,
            isPrimary = recorderDto.isPrimary,
            recorderType = recorderDto.recorderType,
            workplaceId = workplaceId
        )
    }

    override fun insertWorkplaces(workplaces: List<WorkplaceDto>) {
        workplaceQueries.transaction {
            workplaces.forEach {
                insertWorkplace(it)
            }
        }
    }

    override fun deleteWorkplaces() {
        workplaceQueries.deleteWorkplaces()
        workplaceQueries.deleteRecorders()
    }

    override fun getAllWorkplaces(): Flow<List<WorkplaceDto>> {
        return workplaceQueries.selectAllWorkplaces().asFlow()
            .mapToList(context = Dispatchers.Default)
            .map {
                it.map {
                    val workplaceThings = WorkplaceDto(
                        id = it.id,
                        name = it.name,
                        isDeleted = it.isDeleted,
                        latitude = it.latitude,
                        longitude = it.longitude,
                        radius = it.radius,
                        recorders = listOf()
                    )
                    val recorderThings =
                        if (it.id_ != null && it.name_ != null && it.radius_ != null && it.latitude_ != null && it.longitude_ != null && it.isDeleted_ != null && it.isPrimary != null && it.recorderType != null)
                            RecorderDto(
                                id = it.id_,
                                name = it.name_,
                                radius = it.radius_,
                                latitude = it.latitude_,
                                longitude = it.longitude_,
                                isDeleted = it.isDeleted_,
                                isPrimary = it.isPrimary,
                                recorderType = it.recorderType
                            )
                        else null
                    Triple(it.id, workplaceThings, recorderThings)
                }
                    .groupBy({ it.first }, { it.second to it.third })
                    .map {
                        val workplace = it.value.first().first
                        val recorders = it.value.mapNotNull { it.second }
                        workplace.copy(recorders = recorders)
                    }
            }
    }

}


