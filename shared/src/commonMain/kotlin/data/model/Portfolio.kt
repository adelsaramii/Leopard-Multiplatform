package com.attendace.leopard.data.model

import com.attendance.leopard.data.source.remote.model.dto.ModifyPortfolioInput
import kotlinx.serialization.Serializable

@Serializable
data class Portfolio(
    val Id: String,
    val personId: String,
    val personnelName: String,
    val personnelPosition: String,
    val personnelImageUrl: String,
    val docId: String,
    val requestCodeId: String,
    val requestCodeName: String,
    val startDate: String,
    val endDate: String,
    val registrationDate: String,
    val description: String,
    val autoDescription: String,
    val isHeader: Boolean,
    var isSelected: Boolean = false,
    val actorId: String,
    val wfInstanceId: String,
    val wfVersionId: String,
    val wfTaskDetailId: String,
    val ownerId: String,
    val statusId: String,
    val hasAcceptAction: Boolean = false,
    val hasRejectAction: Boolean = false,
    val hasDeleteAction: Boolean = false,
    val requestType: RequestType,
    val possibleAction: List<RequestPossibleAction>
)

fun Portfolio.toModifyPortfolioInput(actionId: String): ModifyPortfolioInput {
    return ModifyPortfolioInput(
        Id = Id,
        wfVersionId = wfVersionId,
        wfInstanceId = wfInstanceId,
        actionId = actionId,
        statusId = statusId,
        wfTaskDetailId = wfTaskDetailId,
        docId = docId,
        ownerId = ownerId
    )
}


@Serializable
sealed class RequestType {
    @Serializable
    object Attendance : RequestType()

    @Serializable
    object ChangeShift : RequestType()

    @Serializable
    object Hourly : RequestType()

    @Serializable
    object Daily : RequestType()

    @Serializable
    object ExchangeShift : RequestType()

    @Serializable
    object MonthlyItem : RequestType()

    @Serializable
    object Other : RequestType()
}

@Serializable
sealed class RequestPossibleAction {
    @Serializable
    object Delete : RequestPossibleAction()

    @Serializable
    object Reject : RequestPossibleAction()

    @Serializable
    object Accept : RequestPossibleAction()

    @Serializable
    object Nothing : RequestPossibleAction()
}