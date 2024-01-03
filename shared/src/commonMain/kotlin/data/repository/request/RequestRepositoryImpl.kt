package com.attendace.leopard.data.repository.request

import arrow.core.Either
import com.attendance.leopard.data.model.*
import com.attendance.leopard.data.source.remote.model.dto.toDomainModel
import com.attendace.leopard.data.source.remote.service.request.RequestService
import com.attendace.leopard.util.error.Failure

class RequestRepositoryImpl(
    private val requestService: RequestService
) : RequestRepository {

    override suspend fun getRequestFormType(codeId: String?): Either<Failure.NetworkFailure, List<RequestFormType>> {
        return requestService.getRequestFormType(codeId).map { it.map { it.toDomainModel() } }
    }

    override suspend fun getRequestForm(
        formId: String,
        isBulk: Boolean
    ): Either<Failure.NetworkFailure, List<FormItem>> {
        return requestService.getRequestForm(formId = formId, isBulk)
            .map { it.map { it.toDomainModel() } }
    }

    override suspend fun getPersonComponentData(
        url: String,
        pageSize: Int,
        pageNumber: Int,
        searchValue: String
    ): Either<Failure.NetworkFailure, List<RequestSelectComponent>> {
        return requestService.getSelectComponentData(url, pageSize, pageNumber, searchValue)
            .map { it.map { it.toDomainModel() } }
    }

    override suspend fun addRequest(
        formKey: String,
        data: MutableMap<String, String>
    ): Either<Failure.NetworkFailure, AddRequestResponse> {
        return requestService.addRequest(formKey = formKey, data = data).map { it.toDomainModel() }
    }
    override suspend fun addBulkRequest(
        formKey: String,
        data: AddBulkRequestInput
    ): Either<Failure.NetworkFailure, AddRequestResponse> {
        return requestService.addBulkRequest(formKey = formKey, data = data).map { it.toDomainModel() }
    }

    override suspend fun deleteRequest(id: String): Either<Failure.NetworkFailure, Boolean> {
        return requestService.deleteRequest(id)
    }

    override suspend fun getRequestDetail(docId: String): Either<Failure.NetworkFailure, List<FormItem>> {
        return requestService.getRequestDetail(docId = docId).map { it.map { it.toDomainModel() } }
    }


}