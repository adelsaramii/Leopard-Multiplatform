package com.attendace.leopard.presentation.screen.request.list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.attendace.leopard.util.theme.*
import com.attendace.leopard.util.date.removeTimezone
import com.attendace.leopard.util.date.subtractDays
import com.attendace.leopard.util.date.toDateDailyRequest
import com.attendace.leopard.data.model.Request
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun RequestItemDaily(
    statusIcon: String,
    statusColor: Color,
    requestItemData: Request,
    modifier: Modifier = Modifier,
    onRequestItemClick: (Request) -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(shape = RoundedCornerShape(8.dp), color = gray2)
            .clickable { onRequestItemClick(requestItemData) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    color = textColor,
                    style = MaterialTheme.typography.subtitle2,
                    text = requestItemData.codeName,
                )
                val startDate = requestItemData.startDate.removeTimezone().toDateDailyRequest()
                val endDate = requestItemData.endDate.removeTimezone().toDateDailyRequest()
                Text(
                    color = gray3,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight(500),
                        letterSpacing = 0.1.sp
                    ),
                    fontWeight = FontWeight.Medium,
                    text = "$startDate - $endDate",
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Column(
                modifier = Modifier.padding(horizontal = 16.dp), horizontalAlignment = Alignment.End
            ) {
                var diffDays = subtractDays(
                    requestItemData.startDate.removeTimezone(),
                    requestItemData.endDate.removeTimezone()
                )
                if (diffDays == "0") diffDays = "1"
                Text(
                    color = textColor,
                    style = MaterialTheme.typography.subtitle2,
                    text = "$diffDays ${localization(days).value}"
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val statusText =
                        requestItemData.actorFullName
                    Text(
                        color = statusColor,
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight(500),
                            letterSpacing = 0.1.sp
                        ),
                        text = statusText,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .weight(9f, false),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Image(
                        painter = painterResource(statusIcon),
                        contentDescription = null,
                        modifier = Modifier.weight(1f, false)
                    )
                }
            }
        }
    }
}