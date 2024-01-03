package com.attendance.leopard.data.source.remote.model.dto

import com.attendance.leopard.data.model.IndexCard
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class IndexCardDto(
    @SerialName("Name")
    val name: String,
    @SerialName("Added")
    val added: String,
    @SerialName("Reduced")
    val reduced: String,
    @SerialName("Remained")
    val remained: String,
    @SerialName("FirstOfPeriod")
    val firstOfPeriod: String,
    @SerialName("InProcess")
    val inProcess: String,
    @SerialName("ApprovedNotApplied")
    val approvedNotApplied: String,
//    @SerialName("Workperiod")
//    val workplace: WorkPeriodDto,
)

fun IndexCardDto.toIndexCard():IndexCard{
    return IndexCard(name, added, reduced, remained, firstOfPeriod, inProcess, approvedNotApplied)
}