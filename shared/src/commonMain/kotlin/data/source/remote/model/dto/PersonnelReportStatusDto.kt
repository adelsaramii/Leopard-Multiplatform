package com.attendance.leopard.data.source.remote.model.dto

import com.attendace.leopard.data.model.PersonnelReportStatusModel
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class PersonnelReportStatusDto(
    @SerialName("PersonId")
    val personId: String?,
    @SerialName("PersonFullName")
    val personFullName: String?,
    @SerialName("PositionName")
    val positionName: String?,
    @SerialName("PersonnelImageUrl")
    val personnelImageUrl: String?,
    @SerialName("DepartmentName")
    val departmentName: String?,
    @SerialName("IsHeader")
    val isHeader: Boolean?,
    @SerialName("LastAtt")
    val lastAtt: String?,
    @SerialName("Present")
    val present: Boolean?,
    @SerialName("Status")
    val status: String?,
    @SerialName("StructureName")
    val structureName: String?,
    @SerialName("TimeOut")
    val timeOut: String?,
    @SerialName("Timein")
    val timeIn: String?,
    @SerialName("TotalCount")
    val totalCount: Int?
) {

    fun toModel(): PersonnelReportStatusModel {
        return PersonnelReportStatusModel(
            id = personId,
            fullName = personFullName,
            position = positionName,
            structureName = structureName,
            imageUrl = personnelImageUrl,
            isHeader = isHeader,
            lastAtt = lastAtt,
            status = status,
            timeOut = timeOut,
            timeIn = timeIn,
            totalCount = totalCount
        )
    }

}