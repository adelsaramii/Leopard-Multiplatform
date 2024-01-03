package com.attendance.leopard.data.source.remote.model.dto

import com.attendance.leopard.data.model.LeopardTabBarTypes
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
class PersonnelStatusReportTypeDto(
    @SerialName("TotalCounts") val items: ArrayList<PersonnelStatusReportTypeItemDto>
)

@kotlinx.serialization.Serializable
data class PersonnelStatusReportTypeItemDto(
    @SerialName("Count") val count: Int,
    @SerialName("Name") val name: String,
    @SerialName("Status") val status: String,
)

fun PersonnelStatusReportTypeItemDto.toLeopardTabBarTypes(id: String) = LeopardTabBarTypes(
    id = id, name, count, status
)

fun leopardTabBarTypesIdGenerator(list: ArrayList<PersonnelStatusReportTypeItemDto>): List<LeopardTabBarTypes> {
    var id = -1
    return list.map {
        id++
        it.toLeopardTabBarTypes(id.toString())
    }
}