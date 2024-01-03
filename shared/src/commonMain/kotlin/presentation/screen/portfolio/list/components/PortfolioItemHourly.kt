package com.attendace.leopard.presentation.screen.portfolio.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.attendace.leopard.presentation.screen.components.CircleCheckIconToggleButton
import com.attendace.leopard.util.theme.*
import com.attendace.leopard.util.date.removeTimezone
import com.attendace.leopard.util.date.subtractHourAndMinute
import com.attendace.leopard.util.date.toDateHourly
import com.attendace.leopard.util.date.toHourAndMinute
import com.attendace.leopard.data.model.Portfolio

@Composable
fun PortfolioItemHourly(
    portfolioItemData: Portfolio,
    modifier: Modifier = Modifier,
    onRadioButtonClick: (Boolean) -> Unit = {},
    onPortfolioItemClick: (Portfolio) -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(shape = RoundedCornerShape(8.dp), color = gray2)
            .clickable { onPortfolioItemClick(portfolioItemData) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        CircleCheckIconToggleButton(isCheck = portfolioItemData.isSelected, onCheckedChange = {
            portfolioItemData.isSelected = it
            onRadioButtonClick(it)
        })
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        color = textColor,
                        style = MaterialTheme.typography.subtitle2,
                        text = portfolioItemData.requestCodeName,
                    )
                }
                val pairHourAndMinute = subtractHourAndMinute(
                    str1 = portfolioItemData.startDate.removeTimezone(),
                    str2 = portfolioItemData.endDate.removeTimezone()
                )
                val hours = pairHourAndMinute.first
                val minutes = pairHourAndMinute.second
                val duration = when {
                    hours != "0" && minutes != "0" -> {
                        "$hours ${localization(hour).value} $minutes ${localization(minute).value}"
                    }
                    hours != "0" -> {
                        "$hours ${localization(hour).value}"
                    }
                    minutes != "0" -> {
                        "$minutes ${localization(minute).value}"
                    }
                    else -> {
                        ""
                    }
                }
                Text(
                    color = textColor,
                    style = MaterialTheme.typography.subtitle2,
                    text = duration,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    color = gray3,
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Medium,
                    text = portfolioItemData.registrationDate.removeTimezone().toDateHourly()
                )
                val fromDate = portfolioItemData.startDate.removeTimezone().toHourAndMinute()
                val toDate = portfolioItemData.endDate.removeTimezone().toHourAndMinute()
                Text(
                    color = gray3,
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Medium,
                    text = "$fromDate - $toDate",
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}