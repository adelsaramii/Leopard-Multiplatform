package com.attendace.leopard.presentation.screen.home.daily.attendance

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.attendance.leopard.data.model.Attendance

@Composable
fun AttendancePairItem(attendance: Pair<Attendance, Attendance>, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        AttendanceItemStartCustomDottedLine(
            modifier = Modifier
                .width(8.dp)
                .height(148.dp)
        )
        Column(modifier = Modifier.weight(1f, true)) {
            if (attendance.first.isMissed)
                EmptyAttendanceItem()
            else
                EnterAttendanceItem(attendance.first)

            Spacer(modifier = Modifier.height(8.dp))

            if (attendance.second.isMissed)
                EmptyAttendanceItem()
            else
                ExitAttendanceItem(attendance.second)
        }
    }
}