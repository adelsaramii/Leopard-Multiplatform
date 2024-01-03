package com.attendance.leopard.data.model

import com.attendance.leopard.data.source.remote.model.dto.PairLogDto

data class DayLog(
    val logsCount: Int,
    val missedLogsCount: Int,
    val hasMissedLog: Boolean,
    val logs: List<PairLog>,
)