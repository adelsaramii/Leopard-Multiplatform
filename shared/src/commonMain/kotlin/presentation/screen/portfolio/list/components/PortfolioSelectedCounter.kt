package com.attendace.leopard.presentation.screen.portfolio.list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.attendace.leopard.presentation.screen.components.CircleCheckIconToggleButton
import com.attendace.leopard.util.theme.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PortfolioSelectedCounter(
    selectedCounter: Int,
    isCheck: Boolean,
    modifier: Modifier = Modifier,
    onSelectAllButton: (Boolean) -> Unit = {},
    onCancelClicked: () -> Unit = {}
) {
    var selectedValue by remember { mutableStateOf(isCheck) }
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .background(Color.Transparent, RectangleShape)
                .fillMaxWidth()
                .padding(start = 8.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
            ) {
                CircleCheckIconToggleButton(
                    isCheck = isCheck, onCheckedChange = {
                        selectedValue = it
                        onSelectAllButton(it)
                    })
                Text(
                    text = localization(select_all).value, style = MaterialTheme.typography.body2
                )
            }
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(end = 8.dp),
                    text = "$selectedCounter ${localization(selected).value}",
                    style = MaterialTheme.typography.body2
                )
                Image(painter = painterResource("ic_cancel.xml"),
                    contentDescription = null,
                    modifier = Modifier.clickable { onCancelClicked() })
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .shadow(1.dp)
        )
    }
}