package com.attendace.leopard.presentation.screen.components.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.attendace.leopard.presentation.screen.components.calendar.utils.ColumnSize
import com.attendace.leopard.presentation.screen.components.calendar.utils.EnglishWeekDays
import com.attendace.leopard.presentation.screen.components.ErrorPage
import com.attendace.leopard.data.model.Day
import com.attendace.leopard.data.model.DayStatus
import com.attendace.leopard.util.theme.holidayColor
import com.attendace.leopard.util.theme.weekDaysColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomCalendar(
    calendarItems: com.attendace.leopard.data.base.LoadableData<List<Day>>,
    selectedDay: Day?,
    onDayClick: (Day) -> Unit,
    columnSize: ColumnSize,
    onWeekPositionChanged: (Int) -> Unit,
    retry: () -> Unit,
    modifier: Modifier = Modifier,
    scrollToForward: Int = 0,
) {
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = 1,
        initialPageOffsetFraction = 0f
    ) {
        calendarItems.data?.size?.floorDiv(7) ?: 1
    }
    LaunchedEffect(key1 = selectedDay) {
        scope.launch {
            if (calendarItems.data != null && calendarItems.data?.indexOf(selectedDay) != -1) {
                pagerState.scrollToPage(
                    calendarItems.data?.indexOf(
                        selectedDay
                    )?.floorDiv(7) ?: 0
                )
            }
        }
    }
    LaunchedEffect(key1 = scrollToForward) {
        scope.launch {
            pagerState.animateScrollToPage(scrollToForward)
        }
    }
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            onWeekPositionChanged(page)
        }
    }
    Column(modifier = modifier.fillMaxWidth()) {
        if (calendarItems is com.attendace.leopard.data.base.Failed) {
//            ErrorRetryColumn(
//                errorTitle = calendarItems.failure.getErrorMessage(),
//                retry = retry,
//                modifier = Modifier.fillMaxWidth()
//            )
            ErrorPage(modifier = Modifier.fillMaxSize(),description = calendarItems.failure.getErrorMessage()?:"") {
                retry()
            }
            return@Column
        }
        when (columnSize) {
            is ColumnSize.Custom -> {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.wrapContentHeight()
                ) { page ->
                    LazyVerticalGrid(
                        modifier = Modifier.fillMaxWidth(),
                        content = {
                            addDaysHeader()
                            when (calendarItems) {
                                is com.attendace.leopard.data.base.Loaded -> {
                                    calendarItems.data.subList(page * 7, page * 7 + 7).forEach {
                                        item {
                                            if (selectedDay?.date == it.date) {
                                                CalendarDay(
                                                    it.copy(dayStatus = DayStatus.Selected),
                                                    onCurrentDayClick = onDayClick
                                                )
                                            } else {
                                                CalendarDay(
                                                    it.copy(dayStatus = it.dayStatus),
                                                    onCurrentDayClick = onDayClick
                                                )
                                            }
                                        }
                                    }

                                }

                                com.attendace.leopard.data.base.Loading -> {
                                    items(7) {
                                        ShimmerCalendarDay()
                                    }
                                }

                                com.attendace.leopard.data.base.NotLoaded -> {}
                                else -> {}
                            }
                        },
                        contentPadding = PaddingValues(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        columns = GridCells.Fixed(7)
                    )
                }

            }

            else -> {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxWidth(),
                    columns = GridCells.Fixed(7),
                    content = {
                        when (calendarItems) {
                            is com.attendace.leopard.data.base.Failed -> {}
                            is com.attendace.leopard.data.base.Loaded -> {
                                addDaysHeader()
                                calendarItems.data.forEach {
                                    item {
                                        CalendarDay(day = it, onCurrentDayClick = onDayClick)
                                    }
                                }

                            }

                            com.attendace.leopard.data.base.Loading -> {
                                items(35) {
                                    ShimmerCalendarDay()
                                }
                            }

                            com.attendace.leopard.data.base.NotLoaded -> {}
                        }

                    },
                    contentPadding = PaddingValues(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                )
            }
        }

    }

}

private fun LazyGridScope.addDaysHeader() {
    itemsIndexed(EnglishWeekDays) { index: Int, item ->
        if (index == 0)
            CalendarNormalText(
                text = item,
                style = MaterialTheme.typography.caption,
                textColor = holidayColor,
            )
        else
            CalendarNormalText(
                text = item,
                style = MaterialTheme.typography.caption,
                textColor = weekDaysColor,
            )
    }
}

private fun LazyListScope.addDaysHeader() {
    itemsIndexed(EnglishWeekDays) { index: Int, item ->
        if (index == 0)
            CalendarNormalText(
                text = item,
                style = MaterialTheme.typography.caption,
                textColor = holidayColor,
            )
        else
            CalendarNormalText(
                text = item,
                style = MaterialTheme.typography.caption,
                textColor = weekDaysColor,
            )
    }
}