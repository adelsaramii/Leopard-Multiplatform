package com.attendace.leopard.presentation.screen.bulk.list.components

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.attendace.leopard.presentation.screen.components.CircleCheckIconToggleButton
import com.attendace.leopard.util.theme.gray2
import com.attendace.leopard.util.theme.gray3
import com.attendace.leopard.util.theme.*
import com.attendace.leopard.util.date.removeTimezone
import com.attendace.leopard.util.date.subtractHourAndMinute
import com.attendace.leopard.util.date.toDateHourly
import com.attendace.leopard.util.date.toHourAndMinute
import com.attendace.leopard.data.model.Bulk
import com.attendance.leopard.data.source.remote.model.dto.SummaryRequestStatus
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun RequestNeedBulkItem(
    bulkItem: Bulk,
    modifier: Modifier = Modifier,
    onRadioButtonClick: (Boolean) -> Unit = {},
    onBulkItemClicked: (Bulk) -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(shape = RoundedCornerShape(8.dp), color = gray2)
            .clickable {
//                bulkItem.isSelected = !bulkItem.isSelected
                onBulkItemClicked(bulkItem)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        CircleCheckIconToggleButton(isCheck = bulkItem.isSelected, onCheckedChange = {
            onRadioButtonClick(it)
            onBulkItemClicked(bulkItem)
        })
        BulkItem(bulkItem)
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PendingBulkItem(
    bulkItem: Bulk,
    modifier: Modifier = Modifier,
    onBulkItemClicked: (Bulk) -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(shape = RoundedCornerShape(8.dp), color = gray2)
            .clickable {
                bulkItem.isSelected = !bulkItem.isSelected
                onBulkItemClicked(bulkItem)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            modifier = Modifier.size(48.dp),
            contentScale = ContentScale.Inside,
            painter = painterResource("ic_status_pending.xml"),
            contentDescription = "pending",
        )
        BulkItem(bulkItem)
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun RejectedBulkItem(
    bulkItem: Bulk,
    modifier: Modifier = Modifier,
    onBulkItemClicked: (Bulk) -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(shape = RoundedCornerShape(8.dp), color = gray2)
            .clickable {
                bulkItem.isSelected = !bulkItem.isSelected
                onBulkItemClicked(bulkItem)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            modifier = Modifier.size(48.dp),
            contentScale = ContentScale.Inside,
            painter = painterResource("ic_status_reject.xml"),
            contentDescription = "pending"
        )
        BulkItem(bulkItem)
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AcceptedBulkItem(
    bulkItem: Bulk,
    modifier: Modifier = Modifier,
    onBulkItemClicked: (Bulk) -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(shape = RoundedCornerShape(8.dp), color = gray2)
            .clickable {
                bulkItem.isSelected = !bulkItem.isSelected
                onBulkItemClicked(bulkItem)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Image(
            modifier = Modifier.size(48.dp),
            contentScale = ContentScale.Inside,
            painter = painterResource("ic_status_accept.xml"),
            contentDescription = "pending"
        )
        BulkItem(bulkItem)
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BulkItem(bulkItem: Bulk) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(end = 8.dp)
    ) {

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center
            ) {
                val startDateTitle = bulkItem.startDate.removeTimezone().toDateHourly()
                Text(
                    color = textColor,
                    style = MaterialTheme.typography.subtitle2,
                    text = startDateTitle,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                val fromDate = bulkItem.startDate.removeTimezone().toHourAndMinute()
                val toDate = bulkItem.endDate.removeTimezone().toHourAndMinute()
                Text(
                    color = gray3,
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Medium,
                    text = "$fromDate - $toDate",
                    modifier = Modifier.padding(end = 8.dp)
                )
            }


            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center ,
                horizontalAlignment = Alignment.End
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val pairHourAndMinute = subtractHourAndMinute(
                        str1 = bulkItem.startDate.removeTimezone(),
                        str2 = bulkItem.endDate.removeTimezone()
                    )
                    val hoursBulk = pairHourAndMinute.first
                    val minutesBulk = pairHourAndMinute.second
                    val duration = when {
                        hoursBulk != "0" && minutesBulk != "0" -> {
                            "$hoursBulk ${localization(hour).value} $minutesBulk ${localization(minute).value}"
                        }

                        hoursBulk != "0" -> {
                            "$hoursBulk ${localization(hour).value}"
                        }

                        minutesBulk != "0" -> {
                            "$minutesBulk ${localization(minute).value}"
                        }

                        else -> {
                            ""
                        }
                    }
                    Text(
                        color = textColor,
                        style = MaterialTheme.typography.subtitle2,
                        text = duration,
                        modifier = Modifier.padding(4.dp)
                    )
                    Image(
                        painter = painterResource("ic_clock.xml"),
                        contentDescription = "clock",
                        modifier = Modifier.padding(4.dp)
                    )
                }
                if (bulkItem.status is SummaryRequestStatus.NotRequest) {
                    Text(
                        color = gray3,
                        style = MaterialTheme.typography.caption,
                        fontWeight = FontWeight.Medium,
                        text = localization(request_required).value
                    )
                }
            }


        }


    }
}