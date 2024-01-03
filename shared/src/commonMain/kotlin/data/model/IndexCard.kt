package com.attendance.leopard.data.model

import kotlinx.serialization.SerialName

data class IndexCard(
    val name: String,
    val added: String,
    val reduced: String,
    val remained: String,
    val firstOfPeriod: String,
    val inProcess: String,
    val approvedNotApplied: String,
)