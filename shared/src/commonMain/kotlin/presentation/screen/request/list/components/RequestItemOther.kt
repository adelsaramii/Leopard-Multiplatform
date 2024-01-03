package com.attendace.leopard.presentation.screen.request.list.components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.*
import com.attendace.leopard.data.model.Request
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun RequestItemOther(
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
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    color = textColor,
                    style = MaterialTheme.typography.subtitle2,
                    text = requestItemData.codeName,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        color = statusColor,
                        style = MaterialTheme.typography.caption,
                        fontWeight = FontWeight.Medium,
                        text = requestItemData.description,
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .weight(95f, false),
                        overflow = TextOverflow.Ellipsis
                    )
                    Image(
                        painter = painterResource(statusIcon),
                        contentDescription = null,
                        modifier = Modifier.weight(5f, false)
                    )
                }
            }
        }
    }
}