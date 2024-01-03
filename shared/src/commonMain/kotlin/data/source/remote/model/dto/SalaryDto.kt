package com.attendance.leopard.data.source.remote.model.dto

import com.attendance.leopard.data.model.Salary
import com.attendance.leopard.data.model.SalaryItem
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class SalaryDto(
    @SerialName("WorkingInfo")
    val workingInfo: List<SalaryItemDto>,
    @SerialName("Incremental")
    val incremental: List<SalaryItemDto>,
    @SerialName("Decremental")
    val decremental: List<SalaryItemDto>,
    @SerialName("Number")
    val number: List<SalaryItemDto>,
    @SerialName("FinalResult")
    val finalResult: SalaryItemDto?,
    @SerialName("SumIncremental")
    val sumIncremental: SalaryItemDto?,
    @SerialName("SumDecremental")
    val sumDecremental: SalaryItemDto?,
)

@kotlinx.serialization.Serializable
data class SalaryItemDto(
    @SerialName("Type")
    val type: String,
    @SerialName("Value")
    val value: String,
    @SerialName("Title")
    val title: String,
    @SerialName("CurrencySymbol")
    val currencySymbol: String,
    @SerialName("UnitOfType")
    val unitOfType: String,
)

sealed class UnitType {
    object Money : UnitType()
    object Day : UnitType()
    object Time : UnitType()
    object Other : UnitType()
}


fun SalaryItemDto.toSalaryItem() = SalaryItem(
    type, value, title, currencySymbol,
    unitType = when (unitOfType) {
        "c29bdff2-8204-751c-ee95-252012693f50" -> {
            UnitType.Money
        }

        "40fc8cd4-31ed-bcc3-939b-7cc169a7a009" -> {
            UnitType.Time
        }

        "2d073bd5-72d6-8ee5-1726-e22993054181" -> {
            UnitType.Day
        }

        else -> {
            UnitType.Other
        }
    }
)

fun SalaryDto.toSalary(): Salary {
    return Salary(
        workingInfo.filter { it.value.isNotEmpty() }.map { it.toSalaryItem() },
        incremental.filter { it.value.isNotEmpty() }.map { it.toSalaryItem() },
        decremental.filter { it.value.isNotEmpty() }.map { it.toSalaryItem() },
        number.filter { it.value.isNotEmpty() }.map { it.toSalaryItem() },
        finalResult?.toSalaryItem(),
        sumIncremental?.toSalaryItem(),
        sumDecremental?.toSalaryItem(),
    )
}