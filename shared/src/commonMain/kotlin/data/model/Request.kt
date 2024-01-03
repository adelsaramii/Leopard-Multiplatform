package com.attendace.leopard.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Request(
    val personnelId: String,
    val docId: String,
    val code: String,
    val codeName: String,
    val startDate: String,
    val endDate: String,
    val actorFullName: String ="",
    val statusId: String,
    val statusName: String ="",
    val description: String,
    val possibleAction: List<RequestPossibleAction>,
    val requestType: RequestType,
    val statusType: StatusType,
)


@Serializable
sealed class StatusType {
    @Serializable
    object Accepted : StatusType()

    @Serializable
    object Deleted : StatusType()

    @Serializable
    object Pending : StatusType()

    @Serializable
    object Rejected : StatusType()

}
