package com.attendance.leopard.data.source.remote.model.dto

import com.attendance.leopard.data.model.FormItem
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class FormItemDto(
    @SerialName("Key")
    val key: String,
    @SerialName("Title")
    val title: String,
    @SerialName("Editable")
    val editable: Boolean,
    @SerialName("Required")
    val required: Boolean,
    @SerialName("RequiredMessage")
    val requiredMessage: String,
    @SerialName("OrderIndex")
    val orderIndex: Int,
    @SerialName("Component")
    val component: String,
    @SerialName("StringValue")
    val stringValue: String? = null,
    @SerialName("DataSource")
    val dataSource: DataSourceDto? = null,
)

@kotlinx.serialization.Serializable
data class DataSourceDto(
    @SerialName("Provider")
    val provider: String,
    @SerialName("Url")
    val url: String? = null,
    @SerialName("Data")
    val data: List<RequestSelectComponentDto>? = null,
)

fun FormItemDto.toDomainModel(): FormItem {
    return FormItem(
        key,
        title,
        editable,
        required,
        requiredMessage,
        orderIndex,
        component,
        stringValue = stringValue,
        provider = dataSource?.provider,
        url = dataSource?.url,
        data = dataSource?.data?.map { it.toDomainModel() }
    )
}