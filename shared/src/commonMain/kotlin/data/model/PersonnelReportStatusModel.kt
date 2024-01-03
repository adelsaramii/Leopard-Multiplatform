package com.attendace.leopard.data.model

data class PersonnelReportStatusModel(
    val id: String?,
    val fullName: String?,
    val position: String?,
    val structureName: String?,
    val imageUrl: String?,
    val isHeader: Boolean?,
    val lastAtt: String?,
    val status: String?,
    val timeOut: String?,
    val timeIn: String?,
    val totalCount: Int?
)

enum class PersonnelReportStatusTypeEnum {
    Absents,
    Presents,
    Overtime,
    Rest
}