package com.attendace.leopard.presentation.screen.home.daily.attendance

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.attendace.leopard.presentation.screen.home.daily.attendance.models.AttendanceType
import com.attendace.leopard.util.theme.enter
import com.attendace.leopard.util.theme.exit
import com.attendace.leopard.util.theme.gray2
import com.attendace.leopard.util.theme.gray3
import com.attendace.leopard.util.theme.greenText
import com.attendace.leopard.util.theme.lightYellow
import com.attendace.leopard.util.theme.localization
import com.attendace.leopard.util.theme.missedAttendance
import com.attendace.leopard.util.theme.primaryColor
import com.attendace.leopard.util.theme.splashGradiantEndColor
import com.attendace.leopard.util.theme.splashGradiantStartColor
import com.attendace.leopard.util.theme.textColor
import com.attendace.leopard.util.theme.white
import com.attendace.leopard.util.theme.yellow
import com.attendance.leopard.data.model.Attendance
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun EnterAttendanceItem(attendanceInfo: Attendance, modifier: Modifier = Modifier) {
    AttendanceItem(AttendanceType.Enter, attendanceInfo, modifier)
}

@Composable
fun ExitAttendanceItem(attendanceInfo: Attendance, modifier: Modifier = Modifier) {
    AttendanceItem(AttendanceType.Exit, attendanceInfo, modifier)
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AttendanceItem(
    attendanceType: AttendanceType, attendanceInfo: Attendance, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(gray2)
            .height(80.dp),
    ) {
        AttendanceItemStartIndicator(
            when (attendanceType) {
                AttendanceType.Enter -> splashGradiantEndColor
                AttendanceType.Exit -> splashGradiantStartColor
            }
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = when (attendanceType) {
                    AttendanceType.Enter -> painterResource("drawable_enter.xml")
                    AttendanceType.Exit -> painterResource("drawable_exit.xml")
                },
                contentDescription = "",
                modifier = Modifier.size(36.dp),
            )

            Column(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f, true),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = if (attendanceInfo.isEnter) {
                        localization(enter).value
                    } else {
                        localization(exit).value
                    },
                    color = when (attendanceType) {
                        AttendanceType.Enter -> greenText
                        AttendanceType.Exit -> primaryColor
                    },
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight(500),
                )
                Text(
                    text = attendanceInfo.cardReaderName,
                    color = gray3,
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight(500),
                )
            }
            Text(
                text = attendanceInfo.time,
                color = when (attendanceType) {
                    AttendanceType.Enter -> greenText
                    AttendanceType.Exit -> primaryColor
                },
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.W500,
            )
        }
    }
}


@OptIn(ExperimentalResourceApi::class)
@Composable
fun EmptyAttendanceItem(modifier: Modifier = Modifier) {
    val stroke = Stroke(
        width = 3f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(lightYellow)
            .height(70.dp),
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRoundRect(
                color = yellow, style = stroke, cornerRadius = CornerRadius(8.dp.toPx()),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource("ic_warning.xml"),
                contentDescription = "",
                tint = yellow
            )
            Text(
                text = localization(missedAttendance).value,
                color = textColor,
                fontSize = 16.sp,
                modifier = Modifier
                    .weight(1f, true)
                    .padding(start = 8.dp)
            )
            Text(
                text = "--:--",
                color = textColor,
                style = MaterialTheme.typography.subtitle2,
            )
        }
    }
}


@Composable
fun AttendanceItemStartIndicator(
    backgroundColor: Color, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .fillMaxHeight()
            .width(24.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(white)
                .align(Alignment.Center)
        )
    }
}