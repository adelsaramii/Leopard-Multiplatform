package com.attendace.leopard.presentation.screen.salary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.attendace.leopard.data.base.Failed
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.data.base.Loading
import com.attendace.leopard.data.base.NotLoaded
import com.attendace.leopard.presentation.screen.components.calendar.header.CalendarHeaderArrowIconButton
import com.attendace.leopard.presentation.screen.home.monthly.AppBar
import com.attendace.leopard.presentation.screen.home.monthly.subordinateSelect.SubordinateSelectBottomSheet
import com.attendace.leopard.presentation.viewmodel.util.state
import com.attendace.leopard.presentation.viewmodel.SalaryViewModel
import com.attendace.leopard.util.theme.*
import com.attendace.leopard.presentation.screen.components.ErrorPage
import com.attendace.leopard.presentation.screen.components.leopardGradientBackground
import com.attendace.leopard.util.date.secondsToHoursAndMinutes
import com.attendance.leopard.data.model.SalaryItem
import com.attendance.leopard.data.model.WorkPeriod
import com.attendance.leopard.data.source.remote.model.dto.UnitType
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalaryScreen(
    openMenuDrawer: () -> Unit,
    viewModel: SalaryViewModel = koinInject()
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
        SalaryPage(
            state = state,
            openMenuDrawer = {
                openMenuDrawer()
            }, onAppBarClick = {
                coroutineScope.launch {
                    scaffoldState.bottomSheetState.expand()
                }
            },
            onNextClick = {
                viewModel.selectWorkPeriod(it)
            },
            onPreviousClick = { viewModel.selectWorkPeriod(it) },
            retry = {
                viewModel.getSalary()
            }
        )
    }

}

@Composable
fun SalaryPage(
    state: SalaryViewModel.State,
    openMenuDrawer: () -> Unit = {},
    onAppBarClick: () -> Unit = {},
    onPreviousClick: (WorkPeriod) -> Unit,
    onNextClick: (WorkPeriod) -> Unit,
    retry: () -> Unit = {},
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .leopardGradientBackground()
    ) {

        state.selectedSubordinate.data?.let {
            AppBar(
                title = localization(salary).value,
                onSubordinateClick = { },
                onNavigationIconClick = {
                    openMenuDrawer()
                },
                baseUrl = state.baseUrl ?: "",
                accessToken = state.accessToken ?: "",
            )
        }
            ?: run {
                AppBar(
                    title = localization(salary).value,
                    onSubordinateClick = { },
                    onNavigationIconClick = {
                        openMenuDrawer()
                    },
                    baseUrl = state.baseUrl ?: "",
                    accessToken = state.accessToken ?: "",
                )
            }

        if (state.workPeriods is Loading || state.salary is Loading) {
            SalaryShimmer()
        }


        when (state.salary) {
            is Failed -> {
                ErrorPage(
                    modifier = Modifier.fillMaxSize(),
                    description =
                    (state.salary as Failed).failure.getErrorMessage()
                        ?: localization(error_message).value,
                ) {
                    retry()
                }
            }

            is Loaded -> {
                Column() {
                    state.selectedWorkPeriod?.let {
                        state.salary.data?.finalResult?.let { it1 ->
                            SalaryCard(
                                finalSalary = it1,
                                workPeriod = it,
                                nextEnable = (state.workPeriods.data?.indexOf(state.selectedWorkPeriod)
                                    ?.plus(1)
                                    ?: 0) < (state.workPeriods.data?.size ?: 0),
                                previousEnable = state.workPeriods.data?.indexOf(state.selectedWorkPeriod) != 0,
                                onNextClick = {
                                    val item = state.workPeriods.data?.get(
                                        state.workPeriods.data?.indexOf(state.selectedWorkPeriod)
                                            ?.plus(1) ?: 0
                                    )
                                    item?.let(onNextClick)
                                },
                                onPreviousClick = {
                                    val item = state.workPeriods.data?.get(
                                        state.workPeriods.data?.indexOf(state.selectedWorkPeriod)
                                            ?.minus(1) ?: 0
                                    )
                                    item?.let(onPreviousClick)
                                }
                            )
                        }
                        Box(
                            modifier = Modifier
                                .background(
                                    shape = RoundedCornerShape(
                                        topStart = 16.dp,
                                        topEnd = 16.dp
                                    ),
                                    color = white
                                )
                                .padding(24.dp)
                        ) {

                            Column(
                                modifier = Modifier
                                    .verticalScroll(rememberScrollState()),
                            ) {
                                state.salary.data?.let { salary ->
                                    if (salary.workingInfo.isNotEmpty()) {
                                        SalaryDetailSection(
                                            title = localization(workingInfo).value,
                                            data = salary.workingInfo,
                                            valueColor = textColor
                                        )
                                    }
                                    if (salary.incremental.isNotEmpty()) {
                                        SalaryDetailSection(
                                            title = localization(incremental).value,
                                            data = salary.incremental,
                                            valueColor = green,
                                            prefixSign = "+",
                                            sum = salary.sumIncremental
                                        )
                                    }
                                    if (salary.decremental.isNotEmpty()) {
                                        SalaryDetailSection(
                                            title = localization(decremental).value,
                                            data = salary.decremental,
                                            valueColor = red,
                                            prefixSign = "-",
                                            sum = salary.sumDecremental
                                        )
                                    }
                                    if (salary.number.isNotEmpty()) {
                                        SalaryDetailSection(
                                            title = localization(number).value,
                                            data = salary.number,
                                            valueColor = textColor
                                        )
                                    }


                                }
                            }
                        }

                    }
                }


            }

            Loading -> {

            }

            NotLoaded -> {

            }
        }


    }

}

@Composable
fun SalaryCard(
    modifier: Modifier = Modifier,
    finalSalary: SalaryItem,
    workPeriod: WorkPeriod,
    nextEnable: Boolean = true,
    previousEnable: Boolean = true,
    onPreviousClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        CalendarHeaderArrowIconButton(
            "angle_left.xml",
            onClick = {
                onPreviousClick()
            },
            enabled = previousEnable
        )

        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                modifier = Modifier.padding(4.dp),
                text = "${localization(finalPayroll).value} | ${workPeriod.name}",
                style = MaterialTheme.typography.body2,
                color = white
            )
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = "${finalSalary.value} ${finalSalary.currencySymbol} ",
                    style = MaterialTheme.typography.h4,
                    color = white
                )
            }
        }

        CalendarHeaderArrowIconButton(
            "angle_right.xml",
            onClick = {
                onNextClick()
            },
            enabled = nextEnable
        )
    }

}

@Composable
fun SalaryDetailSection(
    modifier: Modifier = Modifier,
    title: String,
    data: List<SalaryItem>,
    valueColor: Color,
    prefixSign: String = "",
    sum: SalaryItem? = null,
) {


    Column(modifier = modifier.padding(8.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            sum?.let { sumItem ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(4.dp),
                        text = title,
                        style = MaterialTheme.typography.body1,
                        color = textColor,
                        textAlign = TextAlign.Start
                    )
                    Row(modifier = Modifier.padding(4.dp)) {
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                            Text(
                                modifier = Modifier.padding(4.dp),
                                text = "$prefixSign ${sumItem.value} ${sumItem.currencySymbol}",
                                style = MaterialTheme.typography.body1,
                                color = valueColor
                            )
                        }
                    }

                }
            } ?: run {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = title,
                    style = MaterialTheme.typography.body1,
                    color = textColor,
                    textAlign = TextAlign.Start
                )
            }


        }
        data.forEach {
            if (it.value.isNotEmpty()) {
                when (it.unitType) {
                    UnitType.Day -> {
                        SalaryDetailItem(
                            title = it.title,
                            value = "$prefixSign ${it.value} ${localization(days).value}",
                            valueColor = valueColor
                        )
                    }

                    UnitType.Money -> {
                        SalaryDetailItem(
                            title = it.title,
                            value = "$prefixSign ${it.value}",
                            valueColor = valueColor,
                            symbol = it.currencySymbol
                        )
                    }

                    UnitType.Time -> {
                        SalaryDetailItem(
                            title = it.title,
                            value = "$prefixSign ${it.value.secondsToHoursAndMinutes()}",
                            valueColor = valueColor
                        )
                    }

                    UnitType.Other -> {
                        SalaryDetailItem(
                            title = it.title,
                            value = "$prefixSign ${it.value}",
                            valueColor = valueColor
                        )
                    }

                }
            }

        }
    }
}

@Composable
fun SalaryDetailItem(title: String, value: String, symbol: String = "", valueColor: Color) {
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
            modifier = Modifier
                .weight(0.70f)
                .padding(16.dp),
            color = textColor,
            maxLines = 2
        )
        Row(modifier = Modifier.padding(12.dp)) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = "$value $symbol",
                    style = MaterialTheme.typography.body1,
                    color = valueColor
                )
            }
        }


    }
}