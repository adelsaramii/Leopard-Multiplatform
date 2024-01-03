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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.attendace.leopard.presentation.screen.components.CircleCheckIconToggleButton
import com.attendace.leopard.util.theme.gray2
import com.attendace.leopard.util.theme.gray3
import com.attendace.leopard.util.theme.textColor
import com.attendace.leopard.data.model.Portfolio

@Composable
fun PortfolioItemOther(
    portfolioItemData: Portfolio,
    modifier: Modifier = Modifier,
    onRadioButtonClick: (Portfolio) -> Unit = {},
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
        CircleCheckIconToggleButton(isCheck = portfolioItemData.isSelected)
        Column(modifier = Modifier) {
            Text(
                color = textColor,
                style = MaterialTheme.typography.subtitle2,
                text = portfolioItemData.requestCodeName,
            )
            Text(
                color = gray3,
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.Medium,
                text = portfolioItemData.description,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 4.dp, end = 16.dp)
            )
        }
    }
}