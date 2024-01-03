package com.attendace.leopard.presentation.screen.home.monthly.summary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.attendace.leopard.data.base.Failed
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.data.base.Loading
import com.attendace.leopard.data.base.NotLoaded
import com.attendace.leopard.presentation.screen.components.NoDataPage
import com.attendace.leopard.util.theme.localization
import com.attendace.leopard.util.theme.request_required
import com.attendace.leopard.util.theme.shapes
import com.attendace.leopard.util.theme.total_information
import com.attendace.leopard.presentation.screen.components.Shimmer
import com.attendace.leopard.presentation.screen.components.ErrorPage
import com.attendance.leopard.data.model.Summary
import com.attendance.leopard.data.model.SummaryCategory

@Composable
fun SummaryBottomSheet(
    summaryCategory: LoadableData<SummaryCategory>,
    modifier: Modifier = Modifier,
    retryDailySummary: () -> Unit,
    onItemClick: (Summary) -> Unit = {},
) {
    Column(modifier = modifier) {
        SummaryView(
            summaryCategory = summaryCategory, retry = retryDailySummary, onItemClick = onItemClick
        )
    }

}

@Composable
fun SummaryView(
    summaryCategory: LoadableData<SummaryCategory>,
    retry: () -> Unit,
    onItemClick: (Summary) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (summaryCategory) {
        is Failed -> {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                ErrorPage(description = summaryCategory.failure.getErrorMessage().toString()) {
                    retry()
                }
            }
        }

        is Loaded -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
            ) {
                if (summaryCategory.data.requestRequired.isEmpty() && summaryCategory.data.totalInformation.isEmpty()) {
                    Row(
                        modifier = modifier.fillMaxSize().padding(top = 80.dp),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        NoDataPage()
                    }
                } else {
                    SummaryList(
                        summaries = summaryCategory.data.requestRequired,
                        headerTitle = localization(request_required),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (it.isSelectable) onItemClick(it)
                    }
                    SummaryList(
                        summaries = summaryCategory.data.totalInformation,
                        headerTitle = localization(total_information),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (it.isSelectable) onItemClick(it)
                    }
                }
            }
        }

        Loading -> {
            Column(
                modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                Shimmer(
                    modifier = Modifier.padding(8.dp),
                    width = 120.dp,
                    height = 16.dp,
                    shape = shapes.small,
                )
                repeat(5) {
                    ShimmerSummaryListItem()
                }
            }
        }

        NotLoaded -> {}
    }
}