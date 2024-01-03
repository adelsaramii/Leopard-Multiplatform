package com.attendace.leopard.presentation.screen.request.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.attendace.leopard.data.base.PageFailed
import com.attendace.leopard.data.base.PageInitialFailed
import com.attendace.leopard.data.base.PageInitialLoad
import com.attendace.leopard.data.base.PageInitialLoading
import com.attendace.leopard.data.base.PageInitialNotLoaded
import com.attendace.leopard.data.base.PageLoad
import com.attendace.leopard.data.base.PageLoading
import com.attendace.leopard.data.base.PaginationLoadableData
import com.attendace.leopard.presentation.screen.components.NoDataPage
import com.attendace.leopard.presentation.screen.request.list.components.RequestItemAttendance
import com.attendace.leopard.presentation.screen.request.list.components.RequestItemDaily
import com.attendace.leopard.presentation.screen.request.list.components.RequestItemHourly
import com.attendace.leopard.presentation.screen.request.list.components.RequestItemOther
import com.attendace.leopard.presentation.screen.request.list.components.ShimmerRequestItem
import com.attendace.leopard.util.theme.*
import com.attendace.leopard.data.model.Request
import com.attendace.leopard.data.model.RequestType
import com.attendace.leopard.data.model.StatusType
import com.attendace.leopard.presentation.screen.components.ErrorPage
import com.attendace.leopard.presentation.screen.components.refresh.PullRefreshIndicator
import com.attendace.leopard.presentation.screen.components.refresh.PullRefreshState
import com.attendace.leopard.presentation.screen.components.refresh.pullRefresh

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RequestList(
    modifier: Modifier = Modifier,
    requestListState: LazyListState,
    paginationData: PaginationLoadableData<List<Request>>,
    pullRefreshState: PullRefreshState,
    isRefreshing: Boolean,
    onRequestItemClick: (Request) -> Unit = {},
) {
    val data = paginationData.data ?: emptyList()
    when (paginationData) {
        is PageInitialFailed -> {
            ErrorPage(
                modifier = Modifier.fillMaxSize(),
                description = paginationData.throwable.message.toString()
            ) {

            }
        }

        is PageInitialLoad, is PageLoad, is PageLoading, is PageFailed -> {
            Box(
                modifier = modifier
                    .pullRefresh(pullRefreshState)
                    .clipToBounds(),
            ) {
                if (data.isEmpty()) {
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
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        state = requestListState,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                    ) {
                        items(data) {
                            val statusColor: Color = when (it.statusType) {
                                StatusType.Accepted -> green
                                StatusType.Deleted -> redText
                                StatusType.Pending -> primary
                                StatusType.Rejected -> redText
                            }
                            val statusIcon: String = when (it.statusType) {
                                StatusType.Accepted -> "ic_status_accept.xml"
                                StatusType.Deleted -> "ic_status_delete.xml"
                                StatusType.Pending -> "ic_status_pending.xml"
                                StatusType.Rejected -> "ic_status_reject.xml"
                            }
                            // Some of names have extra space at the string and they are not displayed properly
                            val requestItemData = it.copy(actorFullName = it.actorFullName.trim())
                            when (requestItemData.requestType) {
                                RequestType.Attendance -> {
                                    RequestItemAttendance(
                                        requestItemData = requestItemData,
                                        statusColor = statusColor,
                                        statusIcon = statusIcon,
                                        onRequestItemClick = onRequestItemClick
                                    )
                                }

                                RequestType.Hourly -> {
                                    RequestItemHourly(
                                        requestItemData = requestItemData,
                                        statusColor = statusColor,
                                        statusIcon = statusIcon,
                                        onRequestItemClick = onRequestItemClick
                                    )
                                }

                                RequestType.Daily -> {
                                    RequestItemDaily(
                                        requestItemData = requestItemData,
                                        statusColor = statusColor,
                                        statusIcon = statusIcon,
                                        onRequestItemClick = onRequestItemClick
                                    )
                                }

                                else -> {
                                    RequestItemOther(
                                        requestItemData = requestItemData,
                                        statusColor = statusColor,
                                        statusIcon = statusIcon,
                                        onRequestItemClick = onRequestItemClick
                                    )
                                }
                            }
                        }
                    }
                    PullRefreshIndicator(
                        isRefreshing, pullRefreshState, Modifier.align(Alignment.TopCenter)
                    )
                }
            }
        }

        is PageInitialLoading -> {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                repeat((1..10).count()) {
                    ShimmerRequestItem()
                }
            }
        }

        is PageInitialNotLoaded -> {}
    }

}