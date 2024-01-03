package com.attendace.leopard.presentation.screen.index_card

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.attendace.leopard.presentation.screen.home.monthly.AppBar
import com.attendace.leopard.presentation.screen.home.monthly.subordinateSelect.SubordinateSelectBottomSheet
import com.attendace.leopard.presentation.viewmodel.util.state
import com.attendace.leopard.presentation.viewmodel.IndexCardViewModel
import com.attendace.leopard.util.theme.*
import com.attendace.leopard.presentation.screen.components.ErrorPage
import com.attendace.leopard.presentation.screen.components.leopardGradientBackground
import com.attendance.leopard.data.model.IndexCard
import com.attendance.leopard.data.model.toUser
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IndexCardScreen(
    viewModel: IndexCardViewModel = koinInject(),
    openMenuDrawer: () -> Unit,
) {
    val state by viewModel.state()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    BottomSheetScaffold(
        sheetContent = {
            SubordinateSelectBottomSheet(
                subordinates = state.subordinates,
                onBackPressed = {
                    coroutineScope.launch {
                        scaffoldState.bottomSheetState.partialExpand()
                    }
                },
                onItemSelected = { viewModel.selectSubordinate(it) },
                selectedSubordinate = state.selectedSubordinate.data,
                retry = {},
                loadNextItems = { },
            )
        },
        sheetContainerColor = Color.White,
        sheetContentColor = Color.White,
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp
    ) {
        IndexCard(state = state, openMenuDrawer = {
            openMenuDrawer()
        }, onAppBarClick = {
            coroutineScope.launch {
                scaffoldState.bottomSheetState.expand()
            }
        }, refresh = {
            viewModel.getIndexCards()
        })
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IndexCard(
    state: IndexCardViewModel.State,
    openMenuDrawer: () -> Unit,
    refresh: () -> Unit,
    onAppBarClick: () -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = 1,
        initialPageOffsetFraction = 0f
    ) {
        state.indexCards.data?.size ?: 0
    }
    var selectedIndex by remember {
        mutableStateOf(0)
    }
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            selectedIndex = page
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .leopardGradientBackground()
    ) {
        state.selectedSubordinate.data?.let {
            AppBar(
                title = localization(indexCard).value,
                user = com.attendace.leopard.data.base.Loaded(it.toUser()),
                onSubordinateClick = { onAppBarClick() },
                onNavigationIconClick = {
                    openMenuDrawer()
                },
                baseUrl = state.baseUrl ?: "",
                accessToken = state.accessToken ?: "",
            )
        }
            ?: run {
                AppBar(
                    title = localization(indexCard).value,
                    user = state.userInfo,
                    onSubordinateClick = { onAppBarClick() },
                    onNavigationIconClick = {
                        openMenuDrawer()
                    },
                    baseUrl = state.baseUrl ?: "",
                    accessToken = state.accessToken ?: "",
                )
            }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    shape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp), color = white
                )
                .padding(16.dp)
        ) {
            when (state.indexCards) {
                is com.attendace.leopard.data.base.Failed -> {
                    ErrorPage(
                        modifier = Modifier.fillMaxSize(),
                        description = state.indexCards.failure.getErrorMessage()
                            ?: localization(error_message).value,
                    ) {
                        refresh()
                    }
                }

                is com.attendace.leopard.data.base.Loaded -> {
                    HorizontalPager(
                        modifier = Modifier
                            .wrapContentHeight()
                            .padding(top = 16.dp, bottom = 32.dp),
                        state = pagerState,
                    ) { page ->
                        IndexCardItem(indexCard = state.indexCards.data[page])
                    }
                    if (state.indexCards.data.isNotEmpty() && state.indexCards.data.size > 1)
                        Row(
                            Modifier
                                .height(50.dp)
                                .fillMaxWidth()
                                .padding(bottom = 28.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            repeat(pagerState.pageCount) { iteration ->
                                val color =
                                    if (pagerState.currentPage == iteration) white else inactiveWhite
                                Box(
                                    modifier = Modifier
                                        .padding(3.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .size(30.dp)
                                )
                            }
                        }
                    state.indexCards.data.getOrNull(selectedIndex)
                        ?.let { IndexCardData(indexCard = it) }

                }

                com.attendace.leopard.data.base.Loading -> {
                    IndexCardShimmer()
                }

                com.attendace.leopard.data.base.NotLoaded -> {

                }
            }
        }

    }

}


@Composable
fun IndexCardItem(
    modifier: Modifier = Modifier, indexCard: IndexCard
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                shape = RoundedCornerShape(8.dp), color = if (!indexCard.remained.contains("-")) {
                    secondaryColor
                } else {
                    red
                }
            ),
        verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.Start,
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 8.dp),
            text = indexCard.name,
            color = white,
            style = MaterialTheme.typography.body1
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            color = white,
            thickness = 2.dp,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .height(16.dp)
                        .width(8.dp)
                        .background(shape = RoundedCornerShape(8.dp), color = lightGreen)
                        .border(width = 1.dp, color = white, shape = RoundedCornerShape(8.dp))
                )
                Text(
                    text = "${localization(added).value}: ${indexCard.added}",
                    color = white,
                    style = MaterialTheme.typography.body2,
                )
            }
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .height(16.dp)
                        .width(8.dp)
                        .background(shape = RoundedCornerShape(8.dp), color = red)
                        .border(width = 1.dp, color = white, shape = RoundedCornerShape(8.dp))
                )

                Text(
                    text = "${localization(reduced).value}: ${indexCard.reduced}",
                    color = white,
                    style = MaterialTheme.typography.body2,
                )
            }

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = localization(remain).value + ": ",
                    color = white,
                    style = MaterialTheme.typography.body1,
                )
                Text(
                    text = indexCard.remained,
                    color = white,
                    style = MaterialTheme.typography.body1,
                )
            }
        }
    }

}

@Composable
fun IndexCardData(
    modifier: Modifier = Modifier, indexCard: IndexCard
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {

        Text(
            text = indexCard.name,
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.W500,
            color = black,
            modifier = Modifier.padding(vertical = 8.dp),
        )
        IndexCardDataItem(localization(added).value, indexCard.added)
        IndexCardDataItem(localization(remain).value, indexCard.remained)
        IndexCardDataItem(localization(reduced).value, indexCard.reduced)
        IndexCardDataItem(localization(firstOfPeriod).value, indexCard.firstOfPeriod)
        IndexCardDataItem(localization(inProcess).value, indexCard.inProcess)
        IndexCardDataItem(localization(approvedNotApplied).value, indexCard.approvedNotApplied)

    }
}

@Composable
fun IndexCardDataItem(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(color = gray2, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.W500,
            modifier = Modifier.padding(16.dp),
            color = textColor,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.caption,
            fontWeight = FontWeight.W500,
            modifier = Modifier.padding(16.dp),
            color = gray3
        )
    }
}