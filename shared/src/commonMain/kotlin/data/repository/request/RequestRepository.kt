package com.attendace.leopard.data.repository.request

import arrow.core.Either
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.model.*

interface RequestRepository {
    suspend fun getRequestFormType(codeId: String?): Either<Failure.NetworkFailure, List<RequestFormType>>
    suspend fun getRequestForm(formId: String,isBulk: Boolean): Either<Failure.NetworkFailure, List<FormItem>>
    suspend fun getPersonComponentData(
        url: String,
        pageSize: Int,
        pageNumber: Int,
        searchValue: String
    ): Either<Failure.NetworkFailure, List<RequestSelectComponent>>
    suspend fun addRequest(
        formKey: String,
        data: MutableMap<String,String>
    ): Either<Failure.NetworkFailure, AddRequestResponse>

    suspend fun getRequestDetail(docId: String): Either<Failure.NetworkFailure, List<FormItem>>
    suspend fun addBulkRequest(
        formKey: String,
        data: AddBulkRequestInput
    ): Either<Failure.NetworkFailure, AddRequestResponse>
    suspend fun deleteRequest(id:String): Either<Failure.NetworkFailure, Boolean>

}