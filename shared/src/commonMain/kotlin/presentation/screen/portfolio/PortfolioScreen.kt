package com.attendace.leopard.presentation.screen.portfolio

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.attendace.leopard.presentation.screen.portfolio.filter.PortfolioFilterScreen
import com.attendace.leopard.presentation.screen.portfolio.list.PortfolioListScreen
import com.attendace.leopard.presentation.viewmodel.util.state
import com.attendace.leopard.presentation.viewmodel.PortfolioViewModel
import com.attendace.leopard.data.model.Portfolio
import io.github.xxfast.decompose.router.Router
import io.github.xxfast.decompose.router.content.RoutedContent
import kotlinx.datetime.LocalDate
import org.koin.compose.koinInject

@Composable
fun PortfolioScreen(
    openMenuDrawer: () -> Unit,
    navigateToRequestDetail: (Portfolio) -> Unit,
    modifier: Modifier = Modifier,
    portfolioRouter :  Router<PortfolioNavigation>,
    viewModel: PortfolioViewModel = koinInject(),
) {

    val state by viewModel.state()
    var isFilter = false

    RoutedContent(
        router = portfolioRouter,
    ) { screen ->
        when (screen) {
            PortfolioNavigation.Portfolio -> PortfolioListScreen(
                viewModel = viewModel,
                state = state,
                isFilter = isFilter,
                onFilterClicked = { portfolioRouter.push(PortfolioNavigation.PortfolioFilter) },
                openMenuDrawer = openMenuDrawer,
            ) { navigateToRequestDetail(it) }

            PortfolioNavigation.PortfolioFilter -> PortfolioFilterScreen(
                state = state,
                onFilterApplyClick = { fromDate: LocalDate?, toDate: LocalDate?, applicantId: String? ->
                    isFilter = true
                    viewModel.applyFilters(fromDate, toDate, applicantId)
                    portfolioRouter.pop()
                },
                onFilterResetClicked = {
                    isFilter = false
                    viewModel.resetAllFilters()
                    portfolioRouter.pop()
                },
                loadApplicant = {
                    viewModel.getPortfolioApplicantApi()
                },
                onBackClicked = {
                    portfolioRouter.pop()
                },
                onClickedSubordinate = {
                    viewModel.selectSubordinate(it)
                },
                onFromDateChanged = {
                    viewModel.changeFromDate(it)
                },
                onToDateChanged = {
                    viewModel.changeToDate(it)
                },
            )

        }
    }

}
