package com.attendance.leopard.data.model


data class FormItem(
    val key: String,
    var title: String,
    val editable: Boolean,
    val required: Boolean,
    val requiredMessage: String,
    val orderIndex: Int,
    val component: String,
    val url: String? = null,
    val provider: String? = null,
    val stringValue: String? = null,
    val hasError: Boolean = false,
    val data: List<RequestSelectComponent>? = null,
)