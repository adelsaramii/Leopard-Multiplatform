package com.attendace.leopard.data.repository.workplace

import arrow.core.Either
import com.attendace.leopard.data.local.database.workplace.WorkplaceDao
import com.attendace.leopard.data.local.setting.AuthSettings
import com.attendace.leopard.data.source.remote.service.workplace.WorkplaceService
import com.attendace.leopard.util.error.Failure
import com.attendace.leopard.util.helper.CommonFlow
import com.attendace.leopard.util.helper.asCommonFlow
import com.attendace.leopard.data.model.Workplace
import com.attendace.leopard.data.source.remote.model.dto.toWorkplace
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class WorkplaceRepositoryImpl(
    private val workplaceService: WorkplaceService,
    private val dao: WorkplaceDao,
    private val settings: AuthSettings
) : WorkplaceRepository {

    override suspend fun getWorkplaceChanges(): Either<Failure, Unit> {
        return workplaceService.getWorkplaceChanges(
            settings.getWorkplacesLastModifiedTime().first()
        ).map {
            dao.insertWorkplaces(it)
            settings.setWorkplacesLastModifiedTime(
                Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).let {// template : 2010-10-12 08:31:44
                    val hour = if (it.hour < 10) "0${it.hour}" else "${it.hour}"
                    "${it.year}-${it.monthNumber}-${it.dayOfMonth} $hour:${it.minute}:${it.second}"
                }
            )
        }
    }

    override fun getWorkplaces(): CommonFlow<List<Workplace>> {
        return dao.getAllWorkplaces().map {
            it.map { it.toWorkplace() }
        }.asCommonFlow()
    }
}