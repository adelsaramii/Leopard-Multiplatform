package com.attendace.leopard.presentation.screen.home.daily.attendance.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.attendace.leopard.data.model.Day
import com.attendace.leopard.presentation.screen.components.calendar.CalendarDay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DailyCalendar(
    days: List<Day>,
    modifier: Modifier = Modifier,
) {
    val state = rememberLazyListState()
    LazyRow(
        state = state,
        flingBehavior = rememberSnapFlingBehavior(lazyListState = state),
        modifier = modifier
    ) {
        days.forEach {
            item {
                CalendarDay(day = it, onCurrentDayClick = { })
            }
        }
    }
}