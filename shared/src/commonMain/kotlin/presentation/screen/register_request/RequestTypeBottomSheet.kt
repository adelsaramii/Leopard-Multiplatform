package com.attendace.leopard.presentation.screen.register_request

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
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
import com.attendace.leopard.presentation.screen.home.monthly.subordinateSelect.sanitize
import com.attendace.leopard.util.theme.*
import com.attendance.leopard.data.model.RequestFormType


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RequestTypeBottomSheet(
    requestTypeForm: com.attendace.leopard.data.base.LoadableData<List<RequestFormType>>,
    onBackPressed: () -> Unit,
    onItemSelected: (RequestFormType) -> Unit,
    modifier: Modifier = Modifier,
    selectedItem: RequestFormType? = null,
    retry: () -> Unit,
    loadNextItems: () -> Unit = {},
) {

    Column(
        modifier = modifier
            .background(white)
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
            when (requestTypeForm) {
                is com.attendace.leopard.data.base.Failed -> {}

                is com.attendace.leopard.data.base.Loaded -> {
                    items(
                        items = if (searchText.text.isNotEmpty()) {
                            requestTypeForm.data.filter {
                                it.name.sanitize().lowercase().contains(
                                    searchText.text.sanitize()
                                        .lowercase()
                                )
                            }
                        } else {
                            requestTypeForm.data
                        },
                        key = { it.id }
                    ) { requestFormType ->
                        RequestTypeItem(
                            requestFormType = requestFormType,
                            isSelected = requestFormType.id == selectedItem?.id,
                            onClick = {
                                onBackPressed()
                                onItemSelected(requestFormType)
                            },
                            modifier = Modifier.animateItemPlacement()
                        )
                    }
                }
                com.attendace.leopard.data.base.Loading -> {
                }
                com.attendace.leopard.data.base.NotLoaded -> {}
            }

        }
    }
}

@Composable
fun RequestTypeItem(
    requestFormType: RequestFormType,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = primaryColor,
                unselectedColor = gray2
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = requestFormType.name,
            style = MaterialTheme.typography.body1,
            color = gray3,
            modifier = Modifier.weight(1f, true)
        )
    }
}