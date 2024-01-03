package com.attendace.leopard.presentation.screen.request.filter

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.black
import com.attendace.leopard.util.theme.white
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalFoundationApi::class, ExperimentalResourceApi::class)
@Composable
fun StatusSelectBottomSheet(
    onBackPressed: () -> Unit,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    selectedStatus: String? = null,
) {

    val statusList = listOf("Accepted", "Rejected", "Deleted", "Pending")

    Column(modifier = modifier) {
        IconButton(onClick = onBackPressed, modifier = Modifier.padding(16.dp, 16.dp, 16.dp)) {
            Icon(
                painter = painterResource("arrow_left.xml"),
                contentDescription = "back",
                tint = black
            )
        }
        Column(
            modifier = modifier
                .background(white)
                .defaultMinSize(minHeight = 300.dp)
                .padding(16.dp)
        ) {
            val lazyListState = rememberLazyListState()
            LazyColumn(
                modifier = Modifier.fillMaxSize(), state = lazyListState
            ) {
                items(items = statusList) {
                    StatusListItem(
                        isSelected = selectedStatus == it,
                        status = it,
                        modifier = Modifier.animateItemPlacement(),
                        onClick = {
                            onBackPressed()
                            onItemSelected(it)
                        }
                    )
                }
            }
        }
    }

}