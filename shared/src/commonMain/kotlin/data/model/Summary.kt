package com.attendance.leopard.data.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class Summary(
    val id: String,
    val value: Float,
    val backgroundColor: String = "#FFF",
    val color: String? ="#FFF",
    val isRequestNeed: Boolean,
    val name: String,
    val requestNeedCount: Int = 0,
    val isSelectable:Boolean
)

fun Long.toTime(): String {
    val hours = this / 3600
    val minutes = (this % 3600) / 60

    var result = ""
    if (hours != 0L) {
        result += "$hours h "
    }
    if (minutes != 0L) {
        result += "$minutes min"
    }

    return result
}