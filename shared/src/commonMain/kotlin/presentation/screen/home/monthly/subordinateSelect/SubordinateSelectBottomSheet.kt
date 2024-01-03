package com.attendace.leopard.presentation.screen.home.monthly.subordinateSelect

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.attendace.leopard.data.base.Failed
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.data.base.Loading
import com.attendace.leopard.data.base.NotLoaded
import com.attendace.leopard.presentation.screen.components.ErrorRetryColumn
import com.attendace.leopard.presentation.screen.components.combo.ComboSearchRow
import com.attendace.leopard.presentation.screen.home.monthly.dateSelect.ShimmerDateListItem
import com.attendace.leopard.util.theme.white
import com.attendance.leopard.data.model.Subordinate
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SubordinateSelectBottomSheet(
    subordinates: LoadableData<List<Subordinate>>,
    onBackPressed: () -> Unit,
    onItemSelected: (Subordinate) -> Unit,
    retry: () -> Unit,
    modifier: Modifier = Modifier,
    loadNextItems: () -> Unit = {},
    selectedSubordinate: Subordinate? = null,
) {
//    if (lazyListState.isScrolledToTheEnd()) {
//        loadNextItems()
//    }

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
            when (subordinates) {
                is Failed -> {
                    item {
                        ErrorRetryColumn(
                            errorTitle = subordinates.failure.getErrorMessage(),
                            onRetry = retry,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                is Loaded -> {
                    items(
                        items = if (searchText.text.isNotEmpty()) {
                            subordinates.data.filter {
                                it.fullName.sanitize().lowercase().contains(
                                    searchText.text.sanitize()
                                        .lowercase()
                                ) ||
                                        it.personCode.contains(searchText.text.sanitize())
                            }
                        } else {
                            subordinates.data
                        },
                        key = { it.personId }
                    ) { subordinate ->
                        SubordinateListItem(
                            subordinate = subordinate,
                            isSelected = selectedSubordinate?.personId == subordinate.personId,
                            onClick = {
                                onBackPressed()
                                onItemSelected(subordinate)
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

fun String.sanitize(): String {
    val sanitizeChars: HashMap<Char, Char> = HashMap()
    sanitizeChars['\u0649'] = '\u06CC'
    sanitizeChars['\u064A'] = '\u06CC'
    sanitizeChars['\u0643'] = '\u06A9'
    sanitizeChars['\u0660'] = '\u0030'
    sanitizeChars['\u0661'] = '\u0031'
    sanitizeChars['\u0662'] = '\u0032'
    sanitizeChars['\u0663'] = '\u0033'
    sanitizeChars['\u0664'] = '\u0034'
    sanitizeChars['\u0665'] = '\u0035'
    sanitizeChars['\u0666'] = '\u0036'
    sanitizeChars['\u0667'] = '\u0037'
    sanitizeChars['\u0668'] = '\u0038'
    sanitizeChars['\u0669'] = '\u0039'
    sanitizeChars['\u06F0'] = '\u0030'
    sanitizeChars['\u06F1'] = '\u0031'
    sanitizeChars['\u06F2'] = '\u0032'
    sanitizeChars['\u06F3'] = '\u0033'
    sanitizeChars['\u06F4'] = '\u0034'
    sanitizeChars['\u06F5'] = '\u0035'
    sanitizeChars['\u06F6'] = '\u0036'
    sanitizeChars['\u06F7'] = '\u0037'
    sanitizeChars['\u06F8'] = '\u0038'
    sanitizeChars['\u06F9'] = '\u0039'
    sanitizeChars['آ'] = 'ا'
    sanitizeChars['إ'] = 'ا'
    sanitizeChars['أ'] = 'ا'

    val chars = CharArray(this.length)
    for (i in 0 until this.length) {
        val ch: Char = this[i]
        if (sanitizeChars.containsKey(ch)) chars[i] = sanitizeChars[ch]!! else chars[i] = ch
    }
    return chars.concatToString()

}