package com.attendace.leopard.data.source.remote.service.request

import arrow.core.Either
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.model.AddBulkRequestInput
import com.attendance.leopard.data.source.remote.model.dto.AddRequestResponseDto
import com.attendance.leopard.data.source.remote.model.dto.FormItemDto
import com.attendance.leopard.data.source.remote.model.dto.RequestFormTypeDto
import com.attendance.leopard.data.source.remote.model.dto.RequestSelectComponentDto

interface RequestService {
    suspend fun getRequestFormType(codeId: String? = null):
            Either<Failure.NetworkFailure, List<RequestFormTypeDto>>

    suspend fun getSelectComponentData(
        url: String,
        pageSize: Int,
        pageNumber: Int,
        searchValue: String
    ): Either<Failure.NetworkFailure, List<RequestSelectComponentDto>>

    suspend fun addRequest(
        formKey: String,
        data: MutableMap<String,String>
    ): Either<Failure.NetworkFailure, AddRequestResponseDto>

    suspend fun addBulkRequest(
        formKey: String,
        data: AddBulkRequestInput
    ): Either<Failure.NetworkFailure, AddRequestResponseDto>

    suspend fun deleteRequest(id:String): Either<Failure.NetworkFailure, Boolean>


    suspend fun getRequestDetail(docId: String): Either<Failure.NetworkFailure, List<FormItemDto>>

    suspend fun getRequestForm(
        formId: String,
        isBulk: Boolean
    ): Either<Failure.NetworkFailure, List<FormItemDto>>
}