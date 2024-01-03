package com.attendance.leopard.data.source.remote.model.dto

import com.attendace.leopard.data.model.Portfolio
import com.attendace.leopard.data.model.RequestPossibleAction
import com.attendace.leopard.data.model.RequestType
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class PortfolioDto(
    @SerialName("Id")
    val Id: String,
    @SerialName("PersonnelId")
    val personId: String,
    @SerialName("PersonnelName")
    val personnelName: String,
    @SerialName("PersonnelPosition")
    val personnelPosition: String,
    @SerialName("PersonnelImageUrl")
    val personnelImageUrl: String,
    @SerialName("DocId")
    val docId: String,
    @SerialName("RequestCodeId")
    val requestCodeId: String,
    @SerialName("RequestCodeName")
    val requestCodeName: String,
    @SerialName("RequestClassId")
    val requestClassId: String,
    @SerialName("RequestClassName")
    val requestClassName: String,
    @SerialName("StartDate")
    val startDate: String,
    @SerialName("EndDate")
    val endDate: String,
    @SerialName("RegistrationDate")
    val registrationDate: String,
    @SerialName("Description")
    val description: String,
    @SerialName("AutoDescription")
    val autoDescription: String,
    @SerialName("IsHeader")
    val isHeader: Boolean,
    @SerialName("ActorId")
    val actorId: String,
    @SerialName("WfInstanceId")
    val wfInstanceId: String,
    @SerialName("WfVersionId")
    val wfVersionId: String,
    @SerialName("WfTaskDetailId")
    val wfTaskDetailId: String,
    @SerialName("OwnerId")
    val ownerId: String,
    @SerialName("StatusId")
    val statusId: String,
    @SerialName("PossibleActions")
    val possibleActions: List<ActionDto>,
)

@kotlinx.serialization.Serializable
data class ActionDto(
    @SerialName("Id")
    val Id: String,
    @SerialName("Name")
    val name: String,
    @SerialName("BookmarkName")
    val bookmarkName: String,
)

fun PortfolioDto.toPortfolio() = Portfolio(
    Id,
    personId,
    personnelName,
    personnelPosition,
    personnelImageUrl,
    docId,
    requestCodeId,
    requestCodeName,
    startDate,
    endDate,
    registrationDate,
    description,
    autoDescription,
    isHeader = isHeader,
    actorId = actorId,
    wfInstanceId = wfInstanceId,
    wfVersionId = wfVersionId,
    wfTaskDetailId = wfTaskDetailId,
    ownerId = ownerId,
    statusId = statusId,
    requestType = when (requestClassId) {
        "659589ab-362d-4086-bb93-0b8f7080d644" -> RequestType.Attendance
        "d064742b-e156-4ba2-84a2-1920163735e7" -> RequestType.ChangeShift
        "82a42b5d-5e9c-4d3f-af8b-77103c294766" -> RequestType.Hourly
        "ac5c9a67-1cd8-46a1-836b-bc1e86b06565" -> RequestType.Daily
        "c4a4fb85-6619-4df9-98a5-c81d1fcee202" -> RequestType.ExchangeShift
        "8e10fba0-6e9a-45b5-88c5-ff2d3acf27f7" -> RequestType.MonthlyItem
        else -> RequestType.Other
    }, possibleAction = possibleActions.toPossibleActions()
)


fun List<ActionDto>.toPossibleActions(): List<RequestPossibleAction> {
    val possibleActions = mutableListOf<RequestPossibleAction>()
    if (this.any { it.Id == "f5034853-0965-4cf7-b7a6-c6ccff94541c" }) {
        possibleActions.add(RequestPossibleAction.Accept)
    }
    if (this.any { it.Id == "f5034853-0965-4cf7-b7a6-c6ccff94541c" }) {
        possibleActions.add(RequestPossibleAction.Delete)
    }
    if (this.any { it.Id == "be3e41eb-815e-4fd3-bea2-feb29a6f2a45" }) {
        possibleActions.add(RequestPossibleAction.Reject)
    }
    return possibleActions
}