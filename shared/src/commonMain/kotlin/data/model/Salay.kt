package com.attendance.leopard.data.model

import com.attendance.leopard.data.source.remote.model.dto.UnitType


data class Salary(
    val workingInfo: List<SalaryItem>,
    val incremental: List<SalaryItem>,
    val decremental: List<SalaryItem>,
    val number: List<SalaryItem>,
    val finalResult: SalaryItem?,
    val sumIncremental: SalaryItem?,
    val sumDecremental: SalaryItem?,
)

data class SalaryItem(
    val type: String,
    val value: String,
    val title: String,
    val currencySymbol: String,
    val unitType: UnitType
)