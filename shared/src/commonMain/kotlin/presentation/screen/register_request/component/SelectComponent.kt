package com.attendace.leopard.presentation.screen.register_request.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.attendace.leopard.presentation.screen.components.ErrorRetryColumn
import com.attendace.leopard.presentation.screen.components.combo.ComboSearchRow
import com.attendace.leopard.presentation.screen.home.monthly.dateSelect.ShimmerDateListItem
import com.attendace.leopard.presentation.screen.home.monthly.subordinateSelect.sanitize
import com.attendace.leopard.presentation.viewmodel.util.state
import com.attendace.leopard.presentation.viewmodel.SelectComponentViewModel
import com.attendace.leopard.util.theme.*
import com.attendance.leopard.data.model.FormItem
import com.attendance.leopard.data.model.RequestSelectComponent
import org.koin.compose.koinInject

@Composable
fun SelectComponent(
    modifier: Modifier = Modifier,
    formItem: FormItem,
    loadData: (List<RequestSelectComponent>) -> Unit = {},
    getData: (String) -> Unit = {},
    viewModel: SelectComponentViewModel = koinInject(),
    onValueChangeListener: (RequestSelectComponent) -> Unit
) {
    val state by viewModel.state()
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        formItem.data?.let {
            viewModel.setSelectComponentData(formItem.key, it)
        }
        formItem.url?.let {
            viewModel.setUrl(formItem.key, it)
        }
    }

    if (visible) {
        SelectComponentDialog(formItem = formItem,
            onBackPressed = { visible = false },
            state = viewModel.currentState,
            retry = {}) {
            viewModel.setSelectedItem(formItem.key, it)
            onValueChangeListener(it)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (formItem.editable) 1f else 0.8f)
            .padding(8.dp)
    ) {
        Text(
            modifier = Modifier.padding(vertical = 4.dp),
            text = formItem.title,
            color = textColor,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.W500,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = gray, shape = RoundedCornerShape(8.dp))
                .padding(12.dp)
                .clickable {
                    if (formItem.editable) {
                        visible = true
                        if (formItem.provider == "JSON") {
                            formItem.data?.let(loadData)
                        } else {
                            formItem.url?.let(getData)
                        }
                    }
                }, verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.padding(start = 4.dp),
                imageVector = Icons.Outlined.ExpandMore,
                contentDescription = "ArrowDropDown",
                colorFilter = ColorFilter.tint(gray3)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                if (!formItem.editable) {
                    Text(
                        text = formItem.stringValue ?: "",
                        style = MaterialTheme.typography.body2,
                        color = textColor,
                    )
                } else {
                    Text(
                        text = state.selectedItem[formItem.key]?.title ?: "",
                        style = MaterialTheme.typography.body2,
                        color = textColor,
                    )
                }
            }

            if (!formItem.editable) {
                Image(
                    modifier = Modifier.padding(end = 4.dp),
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = "Lock",
                    colorFilter = ColorFilter.tint(gray3)
                )
            }

        }
    }
}

@Composable
fun PersonCombo(
    modifier: Modifier = Modifier,
    formItem: FormItem,
    isDetail: Boolean = false,
    selectedUserId: String?,
    viewModel: SelectComponentViewModel = koinInject(),
    onValueChangeListener: (RequestSelectComponent) -> Unit
) {
    val state by viewModel.state()
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = state.selectedItem1) {
        state.selectedItem1?.let { onValueChangeListener(it) }
    }

    LaunchedEffect(Unit) {
        formItem.data?.let {
            viewModel.setSelectComponentData(formItem.key, it)
        }
        formItem.url?.let {
            viewModel.setUrl(formItem.key, it, selectedUserId)
        }
    }

    if (visible) {
        SelectComponentDialog(
            formItem = formItem,
            onBackPressed = { visible = false },
            state = viewModel.currentState,
            retry = {},
        ) {
            viewModel.setSelectedItem(formItem.key, it)
            onValueChangeListener(it)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .alpha(if (formItem.editable) 1f else 0.8f)
            .padding(8.dp)
    ) {
        Text(
            modifier = Modifier.padding(vertical = 4.dp),
            text = formItem.title,
            color = textColor,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.W500,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = gray, shape = RoundedCornerShape(8.dp))
                .padding(12.dp)
                .clickable {
                    if (formItem.editable) {
                        visible = true
//                        if (formItem.provider == "JSON") {
//                            formItem.data?.let(loadData)
//                        } else {
//                            formItem.url?.let(getData)
//                        }
                    }
                }, verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.padding(start = 4.dp),
                imageVector = Icons.Outlined.ExpandMore,
                contentDescription = "ArrowDropDown",
                colorFilter = ColorFilter.tint(gray3)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                if (!formItem.editable && isDetail) {
                    Text(
                        text = formItem.stringValue ?: "",
                        style = MaterialTheme.typography.body2,
                        color = textColor,
                    )
                } else {
                    Text(
                        text = state.selectedItem[formItem.key]?.title ?: state.selectedItem1?.title
                        ?: "",
                        style = MaterialTheme.typography.body2,
                        color = textColor,
                    )
                }
            }

            if (!formItem.editable) {
                Image(
                    modifier = Modifier.padding(end = 4.dp),
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = "Lock",
                    colorFilter = ColorFilter.tint(gray3)
                )
            }

        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SelectComponentDialog(
    formItem: FormItem,
    state: SelectComponentViewModel.State,
    onBackPressed: () -> Unit,
    retry: () -> Unit,
    onValueChangeListener: (RequestSelectComponent) -> Unit
) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val lazyListState = rememberLazyListState()
    val items = state.data
    val properties = DialogProperties()
    Dialog(
        onDismissRequest = { onBackPressed() },
        properties.let {
            DialogProperties(
                dismissOnBackPress = it.dismissOnBackPress,
                dismissOnClickOutside = it.dismissOnClickOutside,
                usePlatformDefaultWidth = false
            )
        },
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .background(white, shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                ComboSearchRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp),
                    searchText = searchText,
                    onBackPressed = { onBackPressed() },
                    onSearchTextChange = {
                        searchText = it
                    },
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(
                    modifier = Modifier.fillMaxSize(), state = lazyListState
                ) {

                    when (val data = items[formItem.key]) {
                        is com.attendace.leopard.data.base.Failed -> {
                            item {
                                ErrorRetryColumn(
                                    errorTitle = data.failure.getErrorMessage(),
                                    onRetry = retry,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        is com.attendace.leopard.data.base.Loaded -> {
                            items(items = if (searchText.text.isNotEmpty()) {
                                data.data.filter {
                                    it.title.sanitize().lowercase().contains(
                                        searchText.text.sanitize().lowercase()
                                    )
                                }
                            } else {
                                data.data
                            }, key = { it.id }) { item ->
                                SelectComponentItem(
                                    item = item, onClick = {
                                        onBackPressed()
                                        onValueChangeListener(item)
                                    }, modifier = Modifier.animateItemPlacement()
                                )
                            }
                        }

                        com.attendace.leopard.data.base.Loading -> {
                            items(5) {
                                ShimmerDateListItem()
                            }
                        }

                        com.attendace.leopard.data.base.NotLoaded -> {}
                        null -> TODO()
                    }

                }
            }
        }

    }


}

@Composable
fun SelectComponentItem(
    item: RequestSelectComponent, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                onClick()
            }, verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = item.isSelected, onClick = onClick, colors = RadioButtonDefaults.colors(
                selectedColor = primaryColor, unselectedColor = gray2
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = item.title,
            style = MaterialTheme.typography.body1,
            color = gray3,
            modifier = Modifier.weight(1f, true)
        )
    }
}
