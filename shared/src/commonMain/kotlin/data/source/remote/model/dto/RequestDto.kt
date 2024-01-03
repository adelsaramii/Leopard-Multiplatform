package com.attendance.leopard.data.source.remote.model.dto

import com.attendace.leopard.data.model.Request
import com.attendace.leopard.data.model.RequestType
import com.attendace.leopard.data.model.StatusType
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class RequestDto(
    @SerialName("ActorFullName")
    val actorFullName: String,
    @SerialName("Code")
    val code: String,
    @SerialName("CodeName")
    val codeName: String,
    @SerialName("Description")
    val description: String? = "",
    @SerialName("Id")
    val docId: String,
    @SerialName("EndDate")
    val endDate: String,
    @SerialName("PersonnelId")
    val personnelId: String,
    @SerialName("RequestClassId")
    val requestClassId: String,
    @SerialName("RequestClassName")
    val requestClassName: String,
    @SerialName("StartDate")
    val startDate: String,
    @SerialName("StatusId")
    val statusId: String,
    @SerialName("StatusName")
    val statusName: String,
    @SerialName("PossibleActions")
    val possibleActions: List<ActionDto>,
)

fun RequestDto.toRequest() = Request(
    personnelId = personnelId,
    docId = docId,
    code = code,
    codeName = codeName,
    startDate = startDate,
    endDate = endDate,
    actorFullName = actorFullName,
    statusType = when (statusId) {
        "0bc9f96a-69b7-611f-891b-82022341d98e" -> StatusType.Accepted
        "ff92b072-37d5-8b32-d65c-9c635c965809" -> StatusType.Rejected
        "e5834733-d020-4165-4869-a5a10b83beda" -> StatusType.Deleted
        else -> StatusType.Pending //805af805-eea7-1261-1d9a-8bdc77b139eb
    },
    statusName = statusName,
    description = description?:"",
    requestType = when (requestClassId) {
        "659589ab-362d-4086-bb93-0b8f7080d644" -> RequestType.Attendance
        "d064742b-e156-4ba2-84a2-1920163735e7" -> RequestType.ChangeShift
        "82a42b5d-5e9c-4d3f-af8b-77103c294766" -> RequestType.Hourly
        "ac5c9a67-1cd8-46a1-836b-bc1e86b06565" -> RequestType.Daily
        "c4a4fb85-6619-4df9-98a5-c81d1fcee202" -> RequestType.ExchangeShift
        "8e10fba0-6e9a-45b5-88c5-ff2d3acf27f7" -> RequestType.MonthlyItem
        else -> RequestType.Other
    },
    statusId = statusId,
    possibleAction = possibleActions.toPossibleActions()
)
