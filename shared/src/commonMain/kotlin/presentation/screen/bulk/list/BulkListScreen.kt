@file:OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class)

package com.attendace.leopard.presentation.screen.bulk.list

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.attendace.leopard.presentation.screen.bulk.BulkAppBar
import com.attendace.leopard.presentation.screen.components.LeopardLoadingButton
import com.attendace.leopard.presentation.screen.components.portfolioGradientBackground
import com.attendace.leopard.presentation.viewmodel.BulkViewModel
import com.attendace.leopard.util.theme.*
import com.attendace.leopard.data.model.Bulk
import com.attendace.leopard.presentation.screen.components.localization
import com.attendace.leopard.presentation.screen.components.refresh.PullRefreshState
import com.attendace.leopard.presentation.screen.components.refresh.rememberPullRefreshState
import com.attendance.leopard.data.model.Summary
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BulkListScreen(
    state: BulkViewModel.State,
    summary: Summary,
    selectedDate: String,
    modifier: Modifier = Modifier,
    viewModel: BulkViewModel,
    onBackClicked: () -> Unit,
    onWorkPeriodClick: () -> Unit,
    onNextClick: (List<Bulk>) -> Unit,
) {
    val pullRefreshState = rememberPullRefreshState(state.isRefreshing, { viewModel.refresh() })

    BulkListScreenContent(
        summary = summary,
        state = state,
        pullRefreshState = pullRefreshState,
        onBackClicked = { onBackClicked() },
        onWorkPeriodClick = { onWorkPeriodClick() },
        onNextClick = onNextClick,
        onSelectedCountChange = {
//                viewModel.setSelectedCount(it)
        },
        selectedDate = selectedDate,
        onSelectAllClick = {
            viewModel.changeAllSelection(it)
        },
        onItemClick = {
            viewModel.changeSelection(it)
        },
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BulkListScreenContent(
    summary: Summary,
    selectedDate: String,
    state: BulkViewModel.State,
    pullRefreshState: PullRefreshState,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
    onWorkPeriodClick: () -> Unit,
    onNextClick: (List<Bulk>) -> Unit,
    onSelectedCountChange: (Int) -> Unit,
    onItemClick: (Bulk) -> Unit,
    onSelectAllClick: (Boolean) -> Unit
) {
    val date by remember { mutableStateOf(state.selectedWorkPeriod?.name ?: selectedDate) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .portfolioGradientBackground()
    ) {

        BulkAppBar(
            onBackClicked = { onBackClicked() },
            onTitleClicked = { onWorkPeriodClick() },
            title = "${summary.name}| $date",
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 8.dp),
        )
        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .background(
                        white, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    )
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
            ) {
                BulkList(
                    items = state.bulks,
                    isRefreshing = state.isRefreshing,
                    pullRefreshState = pullRefreshState,
                    onSelectedCountChange = { onSelectedCountChange(it) },
                    modifier = Modifier.weight(1f),
                    onBulkItemClicked = { onItemClick(it) },
                    state = state,
                    onSelectAllClick = { onSelectAllClick(it) },
                )

                val buttonBackgroundColor =
                    if ((state.bulks.data?.filter { it.isSelected }?.size
                            ?: 0) > 0
                    ) primary else gray
                val buttonEnableState =
                    (state.bulks.data?.filter { it.isSelected }?.size ?: 0) > 0
                LeopardLoadingButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    onClick = {
                        state.bulks.data?.filter { it.isSelected }
                            ?.let { it1 -> onNextClick(it1) }
                    },
                    isEnabled = buttonEnableState,
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = buttonBackgroundColor,
                        contentColor = white,
                    ),
                ) {
                    Row {
                        Text(
                            text = localization(next).value,
                            color = white,
                            style = MaterialTheme.typography.button,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Image(
                            painter = painterResource("arrow_right.xml"),
                            contentDescription = "arrow right",
                            modifier = Modifier.localization(),
                            colorFilter = ColorFilter.tint(white),
                        )
                    }
                }
            }
        }

    }
}