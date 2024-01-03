package com.attendance.leopard.data.source.remote.model.dto

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ModifyPortfolioInput(
    @SerialName("Id") val Id: String,
    @SerialName("WfVersionId") val wfVersionId: String,
    @SerialName("WfInstanceId") val wfInstanceId: String,
    @SerialName("ActionId") val actionId: String,
    @SerialName("StatusId") val statusId: String,
    @SerialName("WfTaskDetailId") val wfTaskDetailId: String,
    @SerialName("DocId") val docId: String,
    @SerialName("OwnerId") val ownerId: String,
)