package com.attendace.leopard.presentation.screen.home.daily.attendance.tabRow

import com.attendace.leopard.util.theme.attendance
import com.attendace.leopard.util.theme.daily_summary
import com.attendace.leopard.util.theme.localization


enum class AttendanceTabs(val title: () -> String, val tabIndex: Int) {

    Attendance({ localization(attendance).value }, 0),
    DailySummary({ localization(daily_summary).value }, 1)
}