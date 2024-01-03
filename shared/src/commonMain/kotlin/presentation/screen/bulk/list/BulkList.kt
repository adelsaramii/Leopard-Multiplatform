package com.attendace.leopard.presentation.screen.bulk.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.dp
import com.attendace.leopard.di.loge
import com.attendace.leopard.data.base.Failed
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.data.base.Loading
import com.attendace.leopard.data.base.NotLoaded
import com.attendace.leopard.presentation.screen.bulk.list.components.AcceptedBulkItem
import com.attendace.leopard.presentation.screen.bulk.list.components.PendingBulkItem
import com.attendace.leopard.presentation.screen.bulk.list.components.RejectedBulkItem
import com.attendace.leopard.presentation.screen.bulk.list.components.RequestNeedBulkItem
import com.attendace.leopard.presentation.viewmodel.BulkViewModel
import com.attendace.leopard.util.theme.primary
import com.attendace.leopard.presentation.screen.components.NoDataPage
import com.attendace.leopard.presentation.screen.portfolio.list.components.PortfolioSelectedCounter
import com.attendace.leopard.presentation.screen.request.list.components.ShimmerRequestItem
import com.attendace.leopard.data.model.Bulk
import com.attendace.leopard.presentation.screen.components.refresh.PullRefreshIndicator
import com.attendace.leopard.presentation.screen.components.refresh.PullRefreshState
import com.attendace.leopard.presentation.screen.components.refresh.pullRefresh
import com.attendance.leopard.data.source.remote.model.dto.SummaryRequestStatus

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BulkList(
    items: LoadableData<List<Bulk>>,
    isRefreshing: Boolean,
    pullRefreshState: PullRefreshState,
    state: BulkViewModel.State,
    modifier: Modifier = Modifier,
    onSelectedCountChange: (Int) -> Unit,
    onBulkItemClicked: (Bulk) -> Unit,
    onSelectAllClick: (Boolean) -> Unit
) {

    when (items) {
        is Failed -> {

        }

        is Loaded -> {
            if (items.data.isEmpty()) {
                Box(
                    modifier = modifier
                        .pullRefresh(pullRefreshState)
                        .clipToBounds()
                ) {
                    LazyColumn(
                        modifier = modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            NoDataPage()
                        }
                    }
                    PullRefreshIndicator(
                        isRefreshing, pullRefreshState, Modifier.align(Alignment.TopCenter)
                    )
                }
            } else {

                PortfolioSelectedCounter(
                    selectedCounter = state.bulks.data?.filter { it.isSelected }?.size ?: 0,
                    isCheck = state.bulks.data?.size == state.bulks.data?.filter { it.isSelected }?.size,
                    onCancelClicked = {
                        state.bulks.data?.forEach { bulk -> bulk.isSelected = false }
                    },
                    onSelectAllButton = {
                        onSelectAllClick(it)
//                        state.bulks.data?.forEach { bulk -> bulk.isSelected = it }
//                        state.selectedCount = if (it) state.bulks.data?.size ?: 0 else 0
                    },
                )
                Box(
                    modifier = modifier
                        .pullRefresh(pullRefreshState)
                        .clipToBounds()
                ) {
                    BulkList(requests = items.data, onBulkItemClicked = onBulkItemClicked) {
                        onSelectedCountChange(items.data.count { it.isSelected })
                    }
                    PullRefreshIndicator(
                        refreshing = isRefreshing,
                        state = pullRefreshState,
                        contentColor = primary,
                        modifier = Modifier.align(Alignment.TopCenter),
                    )
                }
            }
        }

        Loading -> {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                repeat((1..15).count()) {
                    ShimmerRequestItem()
                }
            }
        }

        NotLoaded -> {

        }
    }


}

@Composable
fun BulkList(
    modifier: Modifier = Modifier,
    requests: List<Bulk>,
    onBulkItemClicked: (Bulk) -> Unit,
    onRadioButtonClick: (Boolean) -> Unit = {}
) {
    val bulksListState = rememberLazyListState()
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = bulksListState,
        modifier = modifier.padding(16.dp),
    ) {
        items(requests) { bulk ->
            when (bulk.status) {
                SummaryRequestStatus.Accepted -> {
                    AcceptedBulkItem(
                        bulkItem = bulk,
                        onBulkItemClicked = {
//                        onBulkItemClicked(it)
                        },
                    )
                }

                SummaryRequestStatus.Pending -> {
                    PendingBulkItem(
                        bulkItem = bulk,
                        onBulkItemClicked = {
//                        onBulkItemClicked(it)
                        },
                    )
                }

                SummaryRequestStatus.NotRequest -> {
                    RequestNeedBulkItem(
                        bulkItem = bulk,
                        onRadioButtonClick = {
                            onRadioButtonClick(it)
                        },
                        onBulkItemClicked = {
                            onBulkItemClicked(it)
                        },
                    )
                }

                SummaryRequestStatus.Rejected -> {
                    RejectedBulkItem(
                        bulkItem = bulk,
                        onBulkItemClicked = {
//                        onBulkItemClicked(it)
                        },
                    )
                }
            }

        }
    }
}
