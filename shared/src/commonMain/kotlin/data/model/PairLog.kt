package com.attendance.leopard.data.model

import com.attendace.leopard.data.model.Workplace

data class PairLog(
    val Id: String = "",
    val date: String,
    val hasMissedLog: Boolean,
    val enter: Log,
    val exit: Log,
    val workplace: Workplace?,
)