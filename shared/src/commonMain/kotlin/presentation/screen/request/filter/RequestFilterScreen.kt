package com.attendace.leopard.presentation.screen.request.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.attendace.leopard.presentation.screen.components.LeopardLoadingButton
import com.attendace.leopard.presentation.screen.components.leopardButtonColors
import com.attendace.leopard.presentation.screen.portfolio.filter.CustomDatePicker
import com.attendace.leopard.presentation.screen.request.RequestNavigation
import com.attendace.leopard.presentation.viewmodel.RequestViewModel
import com.attendace.leopard.util.theme.*
import com.attendace.leopard.presentation.screen.components.portfolioGradientBackground
import com.attendace.leopard.presentation.screen.components.localization
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import io.github.xxfast.decompose.router.Router
import io.github.xxfast.decompose.router.content.RoutedContent
import io.github.xxfast.decompose.router.rememberRouter
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun RequestFilterScreen(
    state: RequestViewModel.State,
    onFilterApplyClick: (fromDate: LocalDate?, toDate: LocalDate?, statusId: String?) -> Unit,
    modifier: Modifier = Modifier,
    onFilterResetClicked: () -> Unit = {},
    onBackClicked: () -> Unit = {},
    onClickedStatus: (String) -> Unit = {},
    onFromDateChanged: (LocalDate) -> Unit = {},
    onToDateChanged: (LocalDate) -> Unit = {},
) {
    val router: Router<RequestNavigation> = rememberRouter(
        RequestNavigation::class,
        stack =
        listOf(RequestNavigation.RequestFilter)
    )

    RoutedContent(
        router = router,
    ) { screen ->
        when (screen) {
            RequestNavigation.RequestFilter -> Box(
                modifier = modifier
                    .portfolioGradientBackground()
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .background(
                            white,
                            RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                        )
                        .padding(32.dp),
                ) {

                    Box {
                        Text(
                            text = localization(filterPage).value,
                            style = MaterialTheme.typography.h5,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )

                        IconButton(onClick = { onBackClicked() }) {
                            Icon(
                                painter = painterResource("arrow_left.xml"),
                                contentDescription = "back",
                                tint = black,
                                modifier = Modifier.localization()
                            )
                        }
                    }

                    var pickedFromDate by remember {
                        mutableStateOf(
                            state.filterPickedFromDate
                        )
                    }

                    var pickedToDate by remember {
                        mutableStateOf(
                            state.filterPickedToDate
                        )
                    }

                    val statusName: String? by remember {
                        mutableStateOf(state.statusName)
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        val fromDateDialogState = rememberMaterialDialogState()
                        val toDateDialogState = rememberMaterialDialogState()

                        val filters by remember {
                            derivedStateOf {
                                createFilters(
                                    toDate = pickedToDate,
                                    fromDate = pickedFromDate,
                                    status = statusName,
                                )
                            }
                        }

                        filters.forEach { data ->
                            RequestFilterField(
                                data = data,
                                onClick = {
                                    when (data) {
                                        is RequestFilterElement.Status -> {
                                            router.push(RequestNavigation.RegisterFilterStatus)
                                        }

                                        is RequestFilterElement.FromDate -> {
                                            fromDateDialogState.show()
                                        }

                                        is RequestFilterElement.ToDate -> {
                                            toDateDialogState.show()
                                        }
                                    }
                                },
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        CustomDatePicker(
                            state = fromDateDialogState,
                            initialDate = pickedFromDate,
                            title = localization(fromDate).value
                        ) {
                            pickedFromDate = it
                            onFromDateChanged(it)
                        }

                        CustomDatePicker(
                            state = toDateDialogState,
                            initialDate = pickedToDate,
                            title = localization(toDate).value
                        ) {
                            pickedToDate = it
                            onToDateChanged(it)
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        LeopardLoadingButton(
                            onClick = {
                                onFilterApplyClick(
                                    pickedFromDate,
                                    pickedToDate,
                                    state.statusId,
                                )
                            },
                            colors = leopardButtonColors(backgroundColor = primary),
                            modifier = Modifier
                                .height(46.dp)
                                .weight(0.4f)
                                .fillMaxWidth()
                                .clip(shape = RoundedCornerShape(8.dp))
                        ) {
                            Text(
                                text = localization(applyFilter).value,
                                color = white,
                                style = MaterialTheme.typography.caption,
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        OutlinedButton(
                            onClick = onFilterResetClicked,
                            modifier = Modifier
                                .height(46.dp)
                                .weight(0.4f)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = localization(resetAllFilters).value,
                                color = primary,
                                style = MaterialTheme.typography.caption,
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }

                }

            }

            RequestNavigation.RegisterFilterStatus -> StatusSelectBottomSheet(
                onBackPressed = { router.pop() },
                onItemSelected = { onClickedStatus(it) },
                selectedStatus = state.statusName,
            )

            else -> {}
        }
    }

}

fun createFilters(toDate: LocalDate?, fromDate: LocalDate?, status: String?) = listOf(
    RequestFilterElement.FromDate(fromDate?.toString() ?: ""),
    RequestFilterElement.ToDate(toDate?.toString() ?: ""),
    RequestFilterElement.Status(status),
)

sealed class RequestFilterElement(
    val title: String, val icon: ImageVector, val placeholderText: String
) {
    class FromDate(placeholderText: String) : RequestFilterElement(
        title = "From Date", icon = Icons.Default.CalendarMonth, placeholderText = placeholderText
    )

    class ToDate(placeholderText: String) : RequestFilterElement(
        title = "To Date", icon = Icons.Default.CalendarMonth, placeholderText = placeholderText
    )

    class Status(placeholderText: String?) : RequestFilterElement(
        title = "Status",
        icon = Icons.Default.ArrowDropDown,
        placeholderText = placeholderText ?: "select a status"
    )
}