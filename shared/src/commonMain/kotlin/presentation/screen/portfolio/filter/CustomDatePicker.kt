package com.attendace.leopard.presentation.screen.portfolio.filter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.attendace.leopard.util.date.nowLocalDateTime
import com.attendace.leopard.util.theme.white
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import kotlinx.datetime.LocalDate

@Composable
fun CustomDatePicker(
    state: MaterialDialogState,
    initialDate: LocalDate?,
    title: String,
    onDateChange: (LocalDate) -> Unit,
) {
    CompositionLocalProvider(
        LocalLayoutDirection provides LayoutDirection.Ltr
    ) {
        MaterialDialog(
            dialogState = state,
            buttons = {
                positiveButton("Select") {
                    state.hide()
                }
                negativeButton("Cancel") {
                    state.hide()
                }
            }
        ) {
            val now = nowLocalDateTime()

            datepicker(
                initialDate = initialDate ?: LocalDate(now.year , now.monthNumber  ,now.dayOfMonth),
                title = title,
                onDateChange = {
                    onDateChange(it)
                },
                colors = DatePickerDefaults.colors(
                    dateActiveTextColor = white,
                    headerTextColor = white,
                )
            )


        }
    }
}
