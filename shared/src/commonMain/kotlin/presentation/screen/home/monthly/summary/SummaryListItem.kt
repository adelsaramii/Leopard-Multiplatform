package com.attendace.leopard.presentation.screen.home.monthly.summary

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.hour
import com.attendace.leopard.util.theme.localization
import com.attendace.leopard.util.theme.minute
import com.attendace.leopard.util.theme.require_requests_count
import com.attendace.leopard.util.theme.textColor
import com.attendance.leopard.data.model.Summary

@Composable
fun SummaryListItem(
    modifier: Modifier = Modifier,
    item: Summary,
    onItemClick: (Summary) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = Color(
                    item.backgroundColor.removePrefix("#").toLong(16) or 0x00000000FF000000
                ),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onItemClick(item) }
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = if (!item.isRequestNeed) Arrangement.SpaceBetween else Arrangement.Start
        ) {
            if (item.isRequestNeed) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(
                            Color(
                                (item.color ?: item.backgroundColor).removePrefix("#").toLong(16) or 0x00000000FF000000
                            )
                        )
                )
            }
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = item.name,
                style = MaterialTheme.typography.body1,
                color = textColor,
                fontWeight = FontWeight.Medium
            )
            if (!item.isRequestNeed) {
                RequestTime(item)
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        if (item.isRequestNeed && item.requestNeedCount > 0) {
            Row(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    modifier = Modifier.padding(end = 4.dp),
                    text = "${item.requestNeedCount} ${localization(require_requests_count).value}",
                    style = MaterialTheme.typography.caption,
                    color = Color(
                        (item.color ?: item.backgroundColor).removePrefix("#").toLong(16) or 0x00000000FF000000
                    ),
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.width(16.dp))
                RequestTime(item)
            }
        }
    }
}

@Composable
fun RequestTime(item: Summary) {
    val hours = (item.value / 3600).toLong()
    val minutes = ((item.value % 3600) / 60).toLong()

    var result = ""
    if (hours != 0L) {
        result += "$hours ${localization(hour).value} "
    }
    if (minutes != 0L) {
        result += "$minutes ${localization(minute).value}"
    }
    Text(
        text = result,
        style = MaterialTheme.typography.caption,
        color = if (item.isRequestNeed) {
            Color(
                (item.color ?: item.backgroundColor).removePrefix("#").toLong(16) or 0x00000000FF000000
            )
        } else {
            textColor
        },
        fontWeight = FontWeight.Medium
    )
}
