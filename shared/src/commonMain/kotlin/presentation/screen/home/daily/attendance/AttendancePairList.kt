package com.attendace.leopard.presentation.screen.home.daily.attendance

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.attendace.leopard.data.base.Failed
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.data.base.Loading
import com.attendace.leopard.data.base.NotLoaded
import com.attendace.leopard.presentation.screen.components.NoDataPage
import com.attendace.leopard.util.theme.anUnknownErrorOccurred
import com.attendace.leopard.util.theme.localization
import com.attendace.leopard.presentation.screen.home.daily.shimmer.ShimmerAttendanceListItem
import com.attendace.leopard.presentation.screen.components.ErrorPage
import com.attendance.leopard.data.model.Attendance

@Composable
fun AttendancePairList(
    attendanceData: LoadableData<List<Pair<Attendance, Attendance>>>,
    retry: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (attendanceData) {
        is Failed -> {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ErrorPage(
                    description = attendanceData.failure.getErrorMessage()
                        ?: localization(anUnknownErrorOccurred).value,
                    onRetryClick = retry,
                )
            }
        }

        is Loaded -> {
            if (attendanceData.data.isEmpty()) Row(
                modifier = modifier
                    .fillMaxSize()
                    .padding(top = 80.dp),
                horizontalArrangement = Arrangement.Center,
            ) {
                NoDataPage()
            }
            else {
                LazyColumn(
                    modifier = modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxSize()
                ) {
                    attendanceData.data.forEach {
                        item {
                            AttendancePairItem(it, modifier = Modifier.padding(top = 16.dp))
                        }
                    }
                }
            }
        }

        Loading -> {
            LazyColumn(
                modifier = modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
            ) {
                items(4) {
                    ShimmerAttendanceListItem(modifier = Modifier.padding(top = 8.dp))
                }
            }
        }

        NotLoaded -> {}
    }

}