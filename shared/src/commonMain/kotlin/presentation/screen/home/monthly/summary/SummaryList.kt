package com.attendace.leopard.presentation.screen.home.monthly.summary

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.onSurface
import com.attendance.leopard.data.model.Summary

@Composable
fun SummaryList(
    modifier: Modifier = Modifier,
    summaries: List<Summary>,
    headerTitle: State<String>,
    onItemClick: (Summary) -> Unit
) {
    if (summaries.isNotEmpty())
        Text(
            modifier = Modifier.padding(8.dp),
            text = headerTitle.value,
            style = MaterialTheme.typography.body1,
            color = onSurface,
            fontWeight = FontWeight.Medium
        )
    LazyColumn(modifier = modifier) {
        summaries.forEach {
            item {
                SummaryListItem(item = it, modifier = Modifier.padding(top = 8.dp)) {
                    onItemClick(it)
                }
            }
        }
    }
}