package com.attendace.leopard.presentation.screen.home.monthly.dateSelect

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.attendace.leopard.data.base.Failed
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.data.base.Loading
import com.attendace.leopard.data.base.NotLoaded
import com.attendace.leopard.presentation.screen.components.combo.ComboSearchRow
import com.attendace.leopard.util.theme.black
import com.attendace.leopard.util.theme.localization
import com.attendace.leopard.util.theme.red
import com.attendace.leopard.util.theme.retry
import com.attendace.leopard.util.theme.white
import com.attendance.leopard.data.model.WorkPeriod

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DateSelectBottomSheet(
    dates: LoadableData<List<WorkPeriod>>,
    onBackPressed: () -> Unit,
    onItemSelected: (WorkPeriod) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    loadNextItems: () -> Unit = {},
    selectedDate: WorkPeriod? = null,
) {
    Column(
        modifier = modifier
            .background(white)
            .defaultMinSize(minHeight = 300.dp)
            .padding(16.dp)
    ) {
        var searchText by remember { mutableStateOf(TextFieldValue("")) }
        val lazyListState = rememberLazyListState()

        ComboSearchRow(
            searchText = searchText,
            onSearchTextChange = {
                searchText = it
            },
            onBackPressed = onBackPressed,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyListState
        ) {
            when (dates) {
                is Failed -> {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = dates.title ?: "An error occurred", color = black)
                            Button(
                                onClick = { onRetry() },
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = red,
                                    contentColor = white
                                ),
                            ) {
                                Text(text = localization(retry).value)
                            }
                        }
                    }
                }
                is Loaded -> {
                    items(
                        items = if (searchText.text.isNotEmpty()) {
                            dates.data.filter {
                                it.name.lowercase().contains(searchText.text.lowercase())
                            }
                        } else {
                            dates.data
                        },
                        key = { it.id }
                    ) { date ->
                        DateListItem(
                            date = date,
                            isSelected = date.id == selectedDate?.id,
                            onClick = {
                                onBackPressed()
                                onItemSelected(date)
                            },
                            modifier = Modifier.animateItemPlacement()
                        )
                    }
                }
                Loading -> {
                    items(5) {
                        ShimmerDateListItem()
                    }
                }
                NotLoaded -> {}
            }
        }
    }
}