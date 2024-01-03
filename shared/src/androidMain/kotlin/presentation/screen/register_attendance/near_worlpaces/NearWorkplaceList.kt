package com.attendace.leopard.presentation.screen.register_attendance.near_worlpaces


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.primaryColor
import com.attendace.leopard.util.theme.white
import com.attendace.leopard.data.model.Workplace
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


@Composable
fun NearWorkplaceList(
    modifier: Modifier = Modifier,
    workplaces: List<Workplace>,
    selectedWorkplaceId: String?,
    onItemClick: ((Workplace) -> Unit),
) {
    val listState = rememberLazyListState()

    LazyRow(modifier = modifier, state = listState) {
        items(workplaces, key = { it.id }) { workplace ->
            NearWorkplaceItem(
                workplace = workplace,
                selectedWorkplaceId = selectedWorkplaceId,
                onClick = onItemClick
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun NearWorkplaceItem(
    workplace: Workplace,
    selectedWorkplaceId: String?,
    onClick: ((Workplace) -> Unit),
) {
    Card(
        elevation = 0.dp,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(4.dp)
            .shadow(elevation = 4.dp ,
            shape = RoundedCornerShape(16.dp))
            .selectable(selected = workplace.id == selectedWorkplaceId, onClick = {
                onClick(workplace)
            })
    ) {

        val itemBackgroundColorSelected = primaryColor
        val itemBackgroundColor = white

        val itemTextColorSelected = white
        val itemTextColor = primaryColor

        var backgroundColor by remember { mutableStateOf(itemBackgroundColor) }
        var textColor by remember { mutableStateOf(itemTextColor) }

        if (workplace.id == selectedWorkplaceId) {
            backgroundColor = itemBackgroundColorSelected
            textColor = itemTextColorSelected
        } else {
            backgroundColor = itemBackgroundColor
            textColor = itemTextColor
        }

        Surface(color = backgroundColor) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 8.dp),
            ) {
                Image(
                    modifier = Modifier.padding(4.dp),
                    painter = painterResource("ic_location.xml"),
                    contentDescription ="ic_location",
                    colorFilter = ColorFilter.tint(color = textColor)
                )
                Text(
                    text = workplace.name.trim(),
                    color = textColor,
                )
            }
        }
    }

}