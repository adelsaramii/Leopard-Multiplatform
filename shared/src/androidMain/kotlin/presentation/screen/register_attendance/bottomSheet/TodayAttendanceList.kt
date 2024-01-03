package com.attendace.leopard.presentation.screen.register_attendance.bottomSheet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.attendace.leopard.presentation.screen.home.daily.attendance.AttendanceItemStartCustomDottedLine
import com.attendace.leopard.presentation.screen.home.daily.attendance.AttendanceItemStartIndicator
import com.attendace.leopard.presentation.screen.home.daily.attendance.EmptyAttendanceItem
import com.attendace.leopard.util.date.removeTimezone
import com.attendace.leopard.util.theme.enter
import com.attendace.leopard.util.theme.exit
import com.attendace.leopard.util.theme.gray2
import com.attendace.leopard.util.theme.gray3
import com.attendace.leopard.util.theme.greenText
import com.attendace.leopard.util.theme.localization
import com.attendace.leopard.util.theme.primaryColor
import com.attendace.leopard.util.theme.splashGradiantEndColor
import com.attendace.leopard.util.theme.splashGradiantStartColor
import com.attendance.leopard.data.model.Log
import com.attendance.leopard.data.model.PairLog
import com.attendance.leopard.data.source.remote.model.dto.LogType
import kotlinx.datetime.LocalDateTime
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


@Composable
fun PairLogItem(pairLog: PairLog, modifier: Modifier = Modifier) {

    Row(modifier = modifier.padding(8.dp)) {
        AttendanceItemStartCustomDottedLine(
            modifier = Modifier
                .width(8.dp)
                .height(148.dp)
        )
        Column(modifier = Modifier.weight(1f, true)) {
            if (pairLog.enter.isMissed)
                EmptyAttendanceItem()
            else
                LogItem(pairLog.enter)

            Spacer(modifier = Modifier.height(8.dp))

            if (pairLog.exit.isMissed)
                EmptyAttendanceItem()
            else
                LogItem(pairLog.exit)

        }
    }
}


@OptIn(ExperimentalResourceApi::class)
@Composable
fun LogItem(
    log: Log,
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(gray2)
            .height(70.dp),
    ) {
        AttendanceItemStartIndicator(
            when (log.logType) {
                LogType.Enter -> splashGradiantEndColor
                LogType.Exit -> splashGradiantStartColor
            }
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = when (log.logType) {
                    LogType.Enter -> painterResource("drawable_enter.xml")
                    LogType.Exit -> painterResource("drawable_exit.xml")
                },
                contentDescription = "",
                modifier = Modifier
                    .size(36.dp)
            )

            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f, true),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                androidx.compose.material.Text(
                    text = if (log.logType == LogType.Enter) {
                        localization(enter).value
                    } else {
                        localization(exit).value
                    },
                    color = when (log.logType) {
                        LogType.Enter -> greenText
                        LogType.Exit -> primaryColor
                    },
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight(500)
                )
                log.recorder?.name?.let {
                    androidx.compose.material.Text(
                        text = it,
                        color = gray3,
                        style = MaterialTheme.typography.caption,
                        fontWeight = FontWeight(500)
                    )
                }

            }
            val time = LocalDateTime.parse(log.date.removeTimezone())
            androidx.compose.material.Text(
                text = "${time.hour}:${time.minute}",
                color = when (log.logType) {
                    LogType.Enter -> greenText
                    LogType.Exit -> primaryColor
                }
            )
        }
    }
}