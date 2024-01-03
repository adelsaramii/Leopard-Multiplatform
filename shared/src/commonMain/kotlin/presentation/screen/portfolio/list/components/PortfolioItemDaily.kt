package com.attendace.leopard.presentation.screen.portfolio.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.attendace.leopard.util.date.subtractDays
import com.attendace.leopard.util.date.toDateDaily
import com.attendace.leopard.data.model.Portfolio

@Composable
fun PortfolioItemDaily(
    portfolioItemData: Portfolio,
    modifier: Modifier = Modifier,
    onRadioButtonClick: (Boolean) -> Unit = {},
    onPortfolioItemClick: (Portfolio) -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(shape = RoundedCornerShape(8.dp), color = gray2)
            .clickable { onPortfolioItemClick(portfolioItemData) }
            .padding(end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircleCheckIconToggleButton(isCheck = portfolioItemData.isSelected,
                onCheckedChange = {
                    portfolioItemData.isSelected = it
                    onRadioButtonClick(it)
                })
            Column {
                Text(
                    color = textColor,
                    style = MaterialTheme.typography.subtitle2,
                    text = portfolioItemData.requestCodeName,
                )
                val fromDate = portfolioItemData.startDate.removeTimezone().toDateDaily()
                val toDate = portfolioItemData.endDate.removeTimezone().toDateDaily()
                Text(
                    color = gray3,
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Medium,
                    text = "$fromDate - $toDate",
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

        }
        var diffDays = subtractDays(
            portfolioItemData.startDate.removeTimezone(), portfolioItemData.endDate.removeTimezone()
        )
        if (diffDays == "0") diffDays = "1"
        Text(
            color = textColor,
            style = MaterialTheme.typography.subtitle2,
            text = "$diffDays ${localization(days).value}",
            modifier = Modifier
        )
    }
}