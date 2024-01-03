package com.attendace.leopard.data.repository.my_request

import arrow.core.Either
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.model.LeopardTabBarTypes
import com.attendace.leopard.data.model.Request

interface MyRequestRepository {

    suspend fun getRequest(
        codeId: String?,
        startDate: String?,
        endDate: String?,
        status: String?,
        searchValue: String?,
        pageNumber: Int?,
        pageSize: Int?,
    ): Either<Failure.NetworkFailure, List<Request>>

    suspend fun getRequestTypes(): Either<Failure.NetworkFailure, List<LeopardTabBarTypes>>

}
