package com.attendace.leopard.presentation.screen.home.monthly

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.presentation.screen.components.calendar.header.CalendarHeader
import com.attendance.leopard.data.model.WorkPeriod

@Composable
fun MonthlyCalendarHeader(
    workperiodItems: LoadableData<List<WorkPeriod>>,
    workPeriod: WorkPeriod?,
    onPreviousClick: (WorkPeriod) -> Unit,
    onNextClick: (WorkPeriod) -> Unit,
    onMonthChangeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CalendarHeader(
        workPeriod = workPeriod,
        onPreviousClick = {
            val item = workperiodItems.data?.get(
                workperiodItems.data?.indexOf(workPeriod)
                    ?.minus(1) ?: 0
            )
            item?.let(onPreviousClick)
        },
        onNextClick = {
            val item = workperiodItems.data?.get(
                workperiodItems.data?.indexOf(workPeriod)
                    ?.plus(1) ?: 0
            )
            item?.let(onNextClick)
        },
        onMonthChangeClick = onMonthChangeClick,
        nextEnable = (workperiodItems.data?.indexOf(workPeriod)?.plus(1)
            ?: 0) < (workperiodItems.data?.size ?: 0),
        previousEnable = workperiodItems.data?.indexOf(workPeriod) != 0
    )
}