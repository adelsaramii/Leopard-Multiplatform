package com.attendace.leopard.presentation.screen.components.calendar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.attendace.leopard.di.loge
import com.attendace.leopard.util.theme.gray2
import com.attendace.leopard.util.theme.holidayColor
import com.attendace.leopard.util.theme.normalDayTextColor
import com.attendace.leopard.util.theme.selectedDayBackgroundColor
import com.attendace.leopard.util.theme.selectedDayColor
import com.attendace.leopard.util.theme.shimmerColor
import com.attendace.leopard.util.theme.unavailableDayColor
import com.attendace.leopard.util.theme.unavailableStrokeColor
import com.attendace.leopard.presentation.screen.components.Shimmer
import com.attendace.leopard.data.model.Day
import com.attendace.leopard.data.model.DayStatus
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CalendarDay(
    day: Day,
    onCurrentDayClick: (Day) -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
) {
    val textColor = getTextColor(day.dayStatus)
    val border = getBorder(day.dayStatus)
    val backgroundColor = getBackgroundColor(day.dayStatus)

    Box(modifier = modifier) {
        AnimatedVisibility(
            visible = day.haveMissedAttendance,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = (-3).dp, y = (-2).dp)
                .zIndex(1f)
        ) {
            Image(
                painter = painterResource("ic_warning.xml"),
                contentDescription = "warning",
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(size = size)
                .border(border = border, shape = RoundedCornerShape(4.dp))
                .background(color = backgroundColor, shape = RoundedCornerShape(4.dp))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = true)
                ) {
                    onCurrentDayClick(day)
                },
        ) {
            CalendarNormalText(
                text = day.getDayOfMonth().toString(),
                style = MaterialTheme.typography.body1,
                textColor = textColor,
                modifier = Modifier.align(Alignment.Center)
            )
            AnimatedVisibility(
                visible = day.events.isNotEmpty(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(2.dp)
            ) {
                EventsIndicator(day.events)
            }
        }
    }
}

fun getTextColor(dayStatus: DayStatus): Color {
    return when (dayStatus) {
        DayStatus.Holiday -> holidayColor
        DayStatus.Normal -> normalDayTextColor
        DayStatus.Unavailable -> unavailableDayColor
        DayStatus.Selected -> selectedDayColor
        DayStatus.Today -> normalDayTextColor
    }
}

private fun getBorder(dayStatus: DayStatus) = BorderStroke(
    width = 1.dp,
    color = when (dayStatus) {
        DayStatus.Holiday -> holidayColor
        DayStatus.Normal -> Color.Transparent
        DayStatus.Unavailable -> unavailableStrokeColor
        DayStatus.Selected -> Color.Transparent
        DayStatus.Today -> normalDayTextColor
    },
)


fun getBackgroundColor(dayStatus: DayStatus): Color {
    return when (dayStatus) {
        DayStatus.Selected -> selectedDayBackgroundColor
        else -> Color.Transparent
    }
}

fun Day.getDayOfMonth(): Int {
    val date = LocalDateTime.parse(this.date)
    return date.dayOfMonth
}

@Composable
fun ShimmerCalendarDay(modifier: Modifier = Modifier) {
    Shimmer(
        width = 36.dp,
        height = 36.dp,
        gradientWidth = 36.dp,
        shape = RoundedCornerShape(4.dp),
        shimmerColor = shimmerColor,
        backColor = gray2.copy(alpha = 0.5f),
        modifier = modifier
    )
}