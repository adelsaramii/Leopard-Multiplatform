package com.attendace.leopard.data.repository.my_request

import arrow.core.Either
import com.attendace.leopard.data.local.setting.AuthSettings
import com.attendance.leopard.data.model.LeopardTabBarTypes
import com.attendace.leopard.data.model.Request
import com.attendance.leopard.data.source.remote.model.dto.PortfolioRequestTypesDto
import com.attendance.leopard.data.source.remote.model.dto.toPortfolioRequestTypes
import com.attendance.leopard.data.source.remote.model.dto.toRequest
import com.attendace.leopard.data.source.remote.service.my_request.MyRequestService
import com.attendace.leopard.util.error.Failure

class MyRequestRepositoryImpl(
    private val myRequestService: MyRequestService,
    private val authSettings: AuthSettings,
) : MyRequestRepository {

    override suspend fun getRequest(
        codeId: String?,
        startDate: String?,
        endDate: String?,
        status: String?,
        searchValue: String?,
        pageNumber: Int?,
        pageSize: Int?
    ): Either<Failure.NetworkFailure, List<Request>> {
        return myRequestService.getRequest(
            codeId = codeId,
            startDate = startDate,
            endDate = endDate,
            status = status,
            searchValue = searchValue,
            pageNumber = pageNumber,
            pageSize = pageSize
        ).map {
            it.map { it.toRequest() }
        }
    }

    override suspend fun getRequestTypes(): Either<Failure.NetworkFailure, List<LeopardTabBarTypes>> {
        return myRequestService.getRequestTypes().map { modelDto ->
            val requestType = PortfolioRequestTypesDto("0", "All", modelDto.total)
            modelDto.portfolioRequestTypes.add(0, requestType)
            modelDto.portfolioRequestTypes.map { it.toPortfolioRequestTypes() }
        }
    }

}
