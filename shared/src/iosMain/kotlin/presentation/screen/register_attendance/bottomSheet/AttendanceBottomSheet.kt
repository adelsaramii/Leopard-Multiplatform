package com.attendace.leopard.presentation.screen.register_attendance.bottomSheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.attendace.leopard.data.base.Failed
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.data.base.Loading
import com.attendace.leopard.data.base.NotLoaded
import com.attendace.leopard.util.theme.gray
import com.attendace.leopard.util.theme.localization
import com.attendace.leopard.util.theme.primary
import com.attendace.leopard.util.theme.shapes
import com.attendace.leopard.util.theme.today_attendance
import com.attendace.leopard.util.theme.white
import com.attendance.leopard.data.model.DayLog

@Composable
fun AttendanceBottomSheet(
    todayLogs: LoadableData<DayLog>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                localization(today_attendance).value,
                color = primary,
                style = MaterialTheme.typography.body1
            )
            Spacer(modifier = Modifier.width(4.dp))

            if (todayLogs is Loaded) {
                val logCount = todayLogs.data.logsCount
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(color = primary, shape = CircleShape)
                        .padding(vertical = 2.dp, horizontal = 4.dp),
                ) {
                    Text(
                        text = logCount.toString(),
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.Center).background(color = primary),
                        style = MaterialTheme.typography.caption,
                        textAlign = TextAlign.Center,
                        color = white
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        when (todayLogs) {
            is Failed -> {}
            is Loaded -> {
                todayLogs.data.logs.forEach {
                    PairLogItem(it)
                }
            }
            Loading -> {}
            NotLoaded -> {}
        }


    }
}

