package com.attendace.leopard.presentation.screen.components.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.attendace.leopard.data.model.CalendarEventData

@Composable
fun EventsIndicator(
    events: List<CalendarEventData>,
    modifier: Modifier = Modifier
) {
    LazyRow(modifier = modifier) {
        events.forEach {
            item {
                EventsIndicatorItem(it)
            }
        }
    }
}

@Composable
fun EventsIndicatorItem(calendarEventData: CalendarEventData) {
    Box(
        modifier = Modifier
            .padding(2.dp)
            .size(4.dp)
            .clip(CircleShape)

            .background(
                color = Color(
                    calendarEventData.color.removePrefix("#").toLong(16) or 0x00000000FF000000
                )
            )
            .padding(2.dp)
    )
}