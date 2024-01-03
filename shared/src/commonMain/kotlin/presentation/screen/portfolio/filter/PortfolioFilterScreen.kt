package com.attendace.leopard.presentation.screen.portfolio.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
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
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.attendace.leopard.presentation.screen.components.LeopardLoadingButton
import com.attendace.leopard.presentation.screen.components.leopardButtonColors
import com.attendace.leopard.presentation.screen.home.monthly.subordinateSelect.SubordinateSelectBottomSheet
import com.attendace.leopard.presentation.viewmodel.PortfolioViewModel
import com.attendace.leopard.util.theme.*
import com.attendace.leopard.presentation.screen.components.portfolioGradientBackground
import com.attendace.leopard.presentation.screen.components.localization
import com.attendance.leopard.data.model.Subordinate
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import io.github.xxfast.decompose.router.Router
import io.github.xxfast.decompose.router.content.RoutedContent
import io.github.xxfast.decompose.router.rememberRouter
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PortfolioFilterScreen(
    state: PortfolioViewModel.State,
    onFilterApplyClick: (fromDate: LocalDate?, toDate: LocalDate?, applicantId: String?) -> Unit,
    modifier: Modifier = Modifier,
    onFilterResetClicked: () -> Unit = {},
    loadApplicant: () -> Unit = {},
    onBackClicked: () -> Unit = {},
    onClickedSubordinate: (Subordinate) -> Unit = {},
    onFromDateChanged: (LocalDate) -> Unit = {},
    onToDateChanged: (LocalDate) -> Unit = {},
) {
    val router: Router<PortfolioFilterNavigation> = rememberRouter(
        PortfolioFilterNavigation::class,
        stack =
        listOf(PortfolioFilterNavigation.PortfolioFilter)
    )

    RoutedContent(
        router = router,
    ) { screen ->
        when (screen) {
            PortfolioFilterNavigation.PortfolioFilter -> Box(
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
                                modifier = Modifier.localization(),
                                tint = black
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

                    val pickedApplicantName: String? by remember {
                        mutableStateOf(state.selectedSubordinate?.fullName)
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        val fromDateDialogState = rememberMaterialDialogState()
                        val toDateDialogState = rememberMaterialDialogState()

                        val filters = createFilters(
                            toDateValue = pickedToDate,
                            fromDateValue = pickedFromDate,
                            applicantName = pickedApplicantName
                        )



                        filters.forEach { data ->
                            FilterField(data = data, onClick = {
                                when (data) {
                                    is PortfolioFilterElement.Applicant -> {
                                        router.push(
                                            PortfolioFilterNavigation.PortfolioFilterApplicant
                                        )
                                        loadApplicant()
                                    }

                                    is PortfolioFilterElement.FromDate -> {
                                        fromDateDialogState.show()
                                    }

                                    is PortfolioFilterElement.ToDate -> {
                                        toDateDialogState.show()
                                    }
                                }
                            })
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
                                    state.selectedSubordinate?.personId
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

            PortfolioFilterNavigation.PortfolioFilterApplicant -> SubordinateSelectBottomSheet(
                subordinates = state.subordinates,
                onBackPressed = {
                    router.pop()
                },
                onItemSelected = {
                    router.pop()
                    onClickedSubordinate(it)
                },
                retry = {

                },
                loadNextItems = {
                    //TODO load next Items
                },
            )

        }
    }

}

@Composable
fun createFilters(toDateValue: LocalDate?, fromDateValue: LocalDate?, applicantName: String?) =
    listOf(
        PortfolioFilterElement.FromDate(
            localization(fromDate).value,
            fromDateValue?.toString() ?: ""
        ),
        PortfolioFilterElement.ToDate(localization(toDate).value, toDateValue?.toString() ?: ""),
        PortfolioFilterElement.Applicant(
            localization(applicant).value,
            applicantName ?: localization(not_selected).value
        ),
    )


sealed class PortfolioFilterElement(
    val title: String, val icon: ImageVector, val placeholderText: String
) {
    class FromDate(title: String, placeholderText: String) : PortfolioFilterElement(
        title = title, icon = Icons.Default.CalendarMonth, placeholderText = placeholderText
    )

    class ToDate(title: String, placeholderText: String) : PortfolioFilterElement(
        title = title, icon = Icons.Default.CalendarMonth, placeholderText = placeholderText
    )

    class Applicant(title: String, placeholderText: String) : PortfolioFilterElement(
        title = title,
        icon = Icons.Default.Person,
        placeholderText = placeholderText
    )
}


@Parcelize
sealed class PortfolioFilterNavigation : Parcelable {
    object PortfolioFilter : PortfolioFilterNavigation()
    object PortfolioFilterApplicant : PortfolioFilterNavigation()
}