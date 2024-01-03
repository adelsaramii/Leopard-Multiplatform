package com.attendance.leopard.data.source.remote.model.dto

import com.attendace.leopard.data.model.Bulk
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BulkDto(
    @SerialName("CodeId") val codeId: String,
    @SerialName("DailyAttendance") val dailyAttendance: String? = null,
    @SerialName("DateAttendance") val dateAttendance: String,
    @SerialName("EarlyOut") val earlyOut: Boolean,
    @SerialName("EndDate") val endDate: String,
    @SerialName("FirstLateIn") val firstLateIn: Boolean,
    @SerialName("Id") val id: String,
    @SerialName("LastEarlyOut") val lastEarlyOut: Boolean,
    @SerialName("LateIn") val lateIn: Boolean,
    @SerialName("PersonId") val personId: String,
    @SerialName("RequestStatus") val requestStatus: Int,
    @SerialName("SourceId") val sourceId: String,
    @SerialName("SourceTypeId") val sourceTypeId: String,
    @SerialName("StartDate") val startDate: String,
    @SerialName("WorkPeriodId") val workPeriodId: String
)

fun BulkDto.toBulk(): Bulk {
    return Bulk(
        codeId,
        dailyAttendance,
        dateAttendance,
        earlyOut,
        endDate,
        firstLateIn,
        id,
        lastEarlyOut,
        lateIn,
        personId,
        requestStatus,
        sourceId,
        sourceTypeId,
        startDate,
        workPeriodId,
        status = when (requestStatus) {
            0 -> {
                SummaryRequestStatus.NotRequest
            }
            1 -> {
                SummaryRequestStatus.Pending
            }
            2 -> {
                SummaryRequestStatus.Accepted
            }
            else -> {
                SummaryRequestStatus.Rejected
            }
        }
    )
}

@Serializable
sealed class SummaryRequestStatus {
    @Serializable
    object Accepted : SummaryRequestStatus()

    @Serializable
    object NotRequest : SummaryRequestStatus()

    @Serializable
    object Pending : SummaryRequestStatus()

    @Serializable
    object Rejected : SummaryRequestStatus()

}
