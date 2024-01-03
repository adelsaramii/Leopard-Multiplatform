package com.attendace.leopard.presentation.screen.home.daily.attendance.tabRow

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.attendace.leopard.presentation.viewmodel.HomeViewModel
import com.attendace.leopard.util.theme.gray2
import com.attendace.leopard.util.theme.primaryColor
import com.attendace.leopard.util.theme.white
import com.attendace.leopard.presentation.screen.home.daily.attendance.AttendancePairList
import com.attendace.leopard.presentation.screen.home.monthly.summary.SummaryView
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AttendanceTabRow(
    state: HomeViewModel.State,
    retryDailySummary: () -> Unit,
    retryAttendance: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val pages = AttendanceTabs.values()

    val pagerState = rememberPagerState(
        initialPage = 1,
        initialPageOffsetFraction = 0f
    ) {
        pages.size
    }
    val scope = rememberCoroutineScope()

    val indicator = @Composable { tabPositions: List<TabPosition> ->
        CustomIndicator(tabPositions, pagerState)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(), contentAlignment = Alignment.Center
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            TabRow(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(percent = 50)),
                selectedTabIndex = pagerState.currentPage,
                indicator = indicator,
                backgroundColor = gray2,
                divider = {}
            ) {
                pages.forEachIndexed { index, tab ->
                    val indicatorTextColor by animateColorAsState(
                        targetValue = if (pagerState.currentPage == index)
                            white
                        else
                            primaryColor,
                        animationSpec = tween(500)
                    )

                    Tab(
                        modifier = Modifier
                            .zIndex(6f)
                            .height(32.dp),
                        text = {
                            Text(text = tab.title.invoke(), color = indicatorTextColor)
                        },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )
                }
            }

            HorizontalPager(
                modifier = modifier.fillMaxSize(),
                state = pagerState,
            ) { page ->
                when (page) {
                    AttendanceTabs.Attendance.tabIndex -> {
                        AttendancePairList(
                            attendanceData = state.dailyAttendance,
                            retry = retryAttendance
                        )
                    }
                    AttendanceTabs.DailySummary.tabIndex -> {
                        SummaryView(
                            summaryCategory = state.dailySummary,
                            retry = retryDailySummary,
                            onItemClick = {} //TODO onItemClick DailySummary
                        )
                    }
                }

            }
        }

    }
}