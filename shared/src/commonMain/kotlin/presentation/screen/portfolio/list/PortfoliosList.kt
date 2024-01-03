package com.attendace.leopard.presentation.screen.portfolio.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.dp
import com.attendace.leopard.data.base.PageFailed
import com.attendace.leopard.data.base.PageInitialFailed
import com.attendace.leopard.presentation.screen.components.NoDataPage
import com.attendace.leopard.presentation.screen.portfolio.list.components.PortfolioItemAttendance
import com.attendace.leopard.presentation.screen.portfolio.list.components.PortfolioItemDaily
import com.attendace.leopard.presentation.screen.portfolio.list.components.PortfolioItemHourly
import com.attendace.leopard.presentation.screen.portfolio.list.components.PortfolioItemOther
import com.attendace.leopard.presentation.screen.portfolio.list.components.ShimmerPortfolioHeader
import com.attendace.leopard.presentation.screen.portfolio.list.components.ShimmerPortfolioItem
import com.attendace.leopard.presentation.screen.portfolio.list.components.UserHeader
import com.attendace.leopard.presentation.screen.components.ErrorPage
import com.attendace.leopard.data.model.Portfolio
import com.attendace.leopard.data.model.RequestType
import com.attendace.leopard.presentation.viewmodel.PortfolioViewModel
import com.attendace.leopard.presentation.screen.components.refresh.PullRefreshIndicator
import com.attendace.leopard.presentation.screen.components.refresh.PullRefreshState
import com.attendace.leopard.presentation.screen.components.refresh.pullRefresh

@Composable
fun PortfoliosList(
    state: PortfolioViewModel.State,
    baseUrl: String,
    accessToken: String,
    modifier: Modifier = Modifier,
    pullRefreshState: PullRefreshState,
    isRefreshing: Boolean,
    onItemClick: (Portfolio) -> Unit,
    retry: () -> Unit,
    onSelectedCountChange: (Int) -> Unit,
) {

    val data = state.portfolios.data ?: emptyList()
    when (state.portfolios) {
        is PageFailed -> {}
        is PageInitialFailed -> {
            ErrorPage(
                modifier = Modifier.fillMaxSize(),
                description = state.portfolios.throwable.message.toString()
            ) {
                retry()
            }
        }

        is com.attendace.leopard.data.base.PageInitialLoad -> {
            Box(
                modifier = modifier
                    .pullRefresh(pullRefreshState)
                    .clipToBounds()
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
                        modifier = modifier
                            .padding(16.dp)
                            .fillMaxSize(),
                    ) {
                        items(data) {
                            if (it.isHeader) {
                                UserHeader(
                                    user = it, baseUrl = baseUrl, accessToken = accessToken
                                )
                            } else {
                                when (it.requestType) {
                                    RequestType.Attendance -> {
                                        PortfolioItemAttendance(portfolioItemData = it,
                                            onRadioButtonClick = {
                                                onSelectedCountChange(data.count { portfolio -> portfolio.isSelected })
                                            }, onPortfolioItemClick = onItemClick
                                        )
                                    }

                                    RequestType.ChangeShift -> {
                                        PortfolioItemOther(
                                            portfolioItemData = it, onRadioButtonClick = {
                                                onSelectedCountChange(data.count { portfolio -> portfolio.isSelected })
                                            }, onPortfolioItemClick = onItemClick
                                        )
                                    }

                                    RequestType.Daily -> {
                                        PortfolioItemDaily(
                                            portfolioItemData = it, onRadioButtonClick = {
                                                onSelectedCountChange(data.count { portfolio -> portfolio.isSelected })
                                            }, onPortfolioItemClick = onItemClick
                                        )
                                    }

                                    RequestType.ExchangeShift -> {
                                        PortfolioItemOther(
                                            portfolioItemData = it, onRadioButtonClick = {
                                                onSelectedCountChange(data.count { portfolio -> portfolio.isSelected })
                                            }, onPortfolioItemClick = onItemClick
                                        )
                                    }

                                    RequestType.Hourly -> {
                                        PortfolioItemHourly(
                                            portfolioItemData = it, onRadioButtonClick = {
                                                onSelectedCountChange(data.count { portfolio -> portfolio.isSelected })
                                            }, onPortfolioItemClick = onItemClick
                                        )
                                    }

                                    RequestType.MonthlyItem -> {
                                        PortfolioItemOther(
                                            portfolioItemData = it, onRadioButtonClick = {
                                                onSelectedCountChange(data.count { portfolio -> portfolio.isSelected })
                                            }, onPortfolioItemClick = onItemClick
                                        )
                                    }

                                    RequestType.Other -> {
                                        PortfolioItemOther(
                                            portfolioItemData = it, onRadioButtonClick = {
                                                onSelectedCountChange(data.count { portfolio -> portfolio.isSelected })
                                            }, onPortfolioItemClick = onItemClick
                                        )
                                    }
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

        is com.attendace.leopard.data.base.PageInitialLoading -> {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                repeat(3, action = {
                    ShimmerPortfolioHeader()
                    ShimmerPortfolioItem()
                    ShimmerPortfolioItem()
                })
            }
        }

        is com.attendace.leopard.data.base.PageInitialNotLoaded -> {}
        is com.attendace.leopard.data.base.PageLoad -> {}
        is com.attendace.leopard.data.base.PageLoading -> {}
    }

}