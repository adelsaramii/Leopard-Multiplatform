package com.attendace.leopard.data.model

import com.attendance.leopard.data.source.remote.model.dto.SummaryRequestStatus

@kotlinx.serialization.Serializable
data class Bulk(
    val codeId: String,
    val dailyAttendance: String? = null,
    val dateAttendance: String,
    val earlyOut: Boolean,
    val endDate: String,
    val firstLateIn: Boolean,
    val id: String,
    val lastEarlyOut: Boolean,
    val lateIn: Boolean,
    val personId: String,
    val requestStatus: Int,
    val sourceId: String,
    val sourceTypeId: String,
    val startDate: String,
    val workPeriodId: String,
    var isSelected: Boolean = false,
    var status: SummaryRequestStatus,
)
