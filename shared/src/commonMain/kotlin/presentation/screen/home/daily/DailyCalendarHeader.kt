package com.attendace.leopard.presentation.screen.home.daily

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.presentation.screen.components.calendar.header.CalendarHeader
import com.attendace.leopard.data.model.Day
import com.attendance.leopard.data.model.WorkPeriod

@Composable
fun DailyCalendarHeader(
    workPeriod: WorkPeriod?,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    selectWeekPosition: Int,
    calendarDayItems: LoadableData<List<Day>>,
    modifier: Modifier = Modifier
) {
    CalendarHeader(
        workPeriod = workPeriod,
        onPreviousClick = onPreviousClick,
        onNextClick = onNextClick,
        enableChangeMonth = false,
        nextEnable = (calendarDayItems.data?.size ?: 0).floorDiv(7) > selectWeekPosition + 1,
        previousEnable = selectWeekPosition > 0,
        modifier = modifier
    )
}