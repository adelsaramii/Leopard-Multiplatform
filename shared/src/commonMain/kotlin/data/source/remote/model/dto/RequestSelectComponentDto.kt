package com.attendance.leopard.data.source.remote.model.dto

import com.attendance.leopard.data.model.RequestSelectComponent
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class RequestSelectComponentDto(
    @SerialName("Id") val id: String = "",
    @SerialName("Title") val title: String = "",
    @SerialName("Name") val name: String = "",
)

fun RequestSelectComponentDto.toDomainModel(): RequestSelectComponent {
    return if (title.isNotEmpty())
        RequestSelectComponent(id, title)
    else RequestSelectComponent(id, name)
}

