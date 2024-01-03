package com.attendace.leopard.data.source.remote.service.my_request

import arrow.core.Either
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.source.remote.model.dto.MyRequestTypesModelDto
import com.attendance.leopard.data.source.remote.model.dto.RequestDto

interface MyRequestService {

    suspend fun getRequest(
        codeId: String?,
        startDate: String?,
        endDate: String?,
        status: String?,
        searchValue: String?,
        pageNumber: Int?,
        pageSize: Int?,
    ): Either<Failure.NetworkFailure, List<RequestDto>>

    suspend fun getRequestTypes(): Either<Failure.NetworkFailure, MyRequestTypesModelDto>

}
