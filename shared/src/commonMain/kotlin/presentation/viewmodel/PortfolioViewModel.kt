package com.attendace.leopard.presentation.viewmodel

import com.attendace.leopard.util.helper.debounce
import com.attendace.leopard.data.base.BaseViewModel
import com.attendace.leopard.data.base.Failed
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.data.base.Loading
import com.attendace.leopard.data.base.NotLoaded
import com.attendace.leopard.data.base.PageInitialNotLoaded
import com.attendace.leopard.data.base.PaginationLoadableData
import com.attendace.leopard.data.base.hasMorePages
import com.attendace.leopard.data.base.toFailed
import com.attendace.leopard.data.base.toLoaded
import com.attendace.leopard.data.base.toLoading
import com.attendance.leopard.data.model.ModifyPortfolioResponse
import com.attendace.leopard.data.model.Portfolio
import com.attendance.leopard.data.model.LeopardTabBarTypes
import com.attendance.leopard.data.model.Subordinate
import com.attendace.leopard.data.model.toModifyPortfolioInput
import com.attendace.leopard.data.repository.portfolio.PortfolioRepository
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate


class PortfolioViewModel(
    val repository: PortfolioRepository
) : com.attendace.leopard.data.base.BaseViewModel<PortfolioViewModel.State>(State()) {

    data class State(
        val portfolios: com.attendace.leopard.data.base.PaginationLoadableData<List<Portfolio>> = com.attendace.leopard.data.base.PageInitialNotLoaded(
            1, PORTFOLIO_PAGE_SIZE
        ),
        var filterPickedFromDate: LocalDate? = null,
        var filterPickedToDate: LocalDate? = null,
        val filterPickedApplicant: String? = null,
        val accessToken: String = "",
        val baseUrl: String = "",
        var requestCode: String? = null,
        val requestTypes: com.attendace.leopard.data.base.LoadableData<List<LeopardTabBarTypes>> = com.attendace.leopard.data.base.NotLoaded,
        val confirmPortfolioResponse: com.attendace.leopard.data.base.LoadableData<List<ModifyPortfolioResponse>> = com.attendace.leopard.data.base.NotLoaded,
        val rejectPortfolioResponse: com.attendace.leopard.data.base.LoadableData<List<ModifyPortfolioResponse>> = com.attendace.leopard.data.base.NotLoaded,
        var isRefreshing: Boolean = false,
        var searchText: String? = null,
        var fromDate: String? = null,
        var endDate: String? = null,
        val subordinates: com.attendace.leopard.data.base.LoadableData<List<Subordinate>> = com.attendace.leopard.data.base.NotLoaded,
        var selectedSubordinate: Subordinate? = null,
    )

    init {
        getBaseUrl()
        getToken()
        loadPortfolios()
        loadPortfolioRequestTypes()
    }

    private fun loadPortfolios() {
        if (!currentState.portfolios.hasMorePages()) return
        updateState {
            copy(portfolios = portfolios.toLoading())
        }

        currentState.fromDate = currentState.filterPickedFromDate?.toString()
        currentState.endDate = currentState.filterPickedToDate?.toString()

        viewModelScope.launch {
            repository.getPortfolio(
                currentState.portfolios.page,
                currentState.portfolios.limit,
                currentState.requestCode,
                currentState.fromDate,
                currentState.endDate,
                currentState.filterPickedApplicant,
                currentState.searchText
            ).fold(ifRight = {
                updateState { copy(isRefreshing = false) }
                updateState {
                    copy(
                        portfolios = portfolios.toLoaded(
                            data = portfolios.data.orEmpty() + it,
                            page = portfolios.page + 1,
                            addedCount = it.size,
                            hasMorePages = (it.size == PORTFOLIO_PAGE_SIZE)
                        )
                    )
                }
            }, ifLeft = {
                updateState { copy(isRefreshing = false) }
                updateState {
                    copy(
                        portfolios = portfolios.toFailed(
                            throwable = Throwable(it.getErrorMessage()),
                            title = it.getErrorMessage()
                        )
                    )
                }
            })
        }
    }

    private fun loadPortfolioRequestTypes() {
        updateState {
            copy(requestTypes = com.attendace.leopard.data.base.Loading)
        }
        viewModelScope.launch {
            repository.getPortfolioRequestTypes().fold(ifRight = {
                updateState {
                    copy(
                        requestTypes = com.attendace.leopard.data.base.Loaded(it)
                    )
                }
            }, ifLeft = {
                updateState {
                    copy(
                        requestTypes = com.attendace.leopard.data.base.Failed(it)
                    )
                }
            })
        }
    }

    fun confirmPortfolioItem(portfolioItems: List<Portfolio>) {
        updateState {
            copy(confirmPortfolioResponse = com.attendace.leopard.data.base.Loading)
        }
        viewModelScope.launch {
            repository.modifyPortfolio(portfolioItems.map {
                it.toModifyPortfolioInput("f5034853-0965-4cf7-b7a6-c6ccff94541c")
            }).fold(ifRight = {
                updateState {
                    copy(confirmPortfolioResponse = com.attendace.leopard.data.base.Loaded(it))
                }
                updateState {
                    copy(
                        portfolios = com.attendace.leopard.data.base.PageInitialNotLoaded(
                            1,
                            PORTFOLIO_PAGE_SIZE
                        )
                    )
                }

                loadPortfolioRequestTypes()
                loadPortfolios()
            }, ifLeft = {})
        }
    }

    fun rejectPortfolioItem(portfolioItems: List<Portfolio>) {
        updateState {
            copy(rejectPortfolioResponse = com.attendace.leopard.data.base.Loading)
        }
        viewModelScope.launch {
            repository.modifyPortfolio(portfolioItems.map { it.toModifyPortfolioInput("be3e41eb-815e-4fd3-bea2-feb29a6f2a45") })
                .fold(ifRight = {
                    updateState {
                        copy(rejectPortfolioResponse = com.attendace.leopard.data.base.Loaded(it))
                    }
                    updateState {
                        copy(
                            portfolios = com.attendace.leopard.data.base.PageInitialNotLoaded(
                                1,
                                PORTFOLIO_PAGE_SIZE
                            )
                        )
                    }

                    loadPortfolioRequestTypes()
                    loadPortfolios()
                }, ifLeft = {})
        }
    }

    fun onLoadMoreCalled() {
        loadPortfolios()
    }

    fun applyFilters(fromDate: LocalDate?, toDate: LocalDate?, applicantId: String?) {
        updateState {
            copy(
                filterPickedFromDate = fromDate,
                filterPickedToDate = toDate,
                filterPickedApplicant = applicantId,
                portfolios = com.attendace.leopard.data.base.PageInitialNotLoaded(
                    1,
                    PORTFOLIO_PAGE_SIZE
                ),
            )
        }
        loadPortfolios()
    }

    fun resetAllFilters() {
        updateState {
            copy(
                filterPickedApplicant = null,
                filterPickedFromDate = null,
                filterPickedToDate = null,
                selectedSubordinate = null,
                portfolios = com.attendace.leopard.data.base.PageInitialNotLoaded(
                    1,
                    PORTFOLIO_PAGE_SIZE
                ),
            )
        }
        loadPortfolios()
    }

    fun changeRequestCode(requestCode: String?) {
        updateState {
            copy(
                requestCode = requestCode, portfolios = com.attendace.leopard.data.base.PageInitialNotLoaded(
                    1,
                    PORTFOLIO_PAGE_SIZE
                )
            )
        }
        loadPortfolios()
    }

    private fun getToken() {
        viewModelScope.launch {
            repository.getAccessToken().collect {
                updateState {
                    copy(
                        accessToken = it
                    )
                }
            }
        }
    }

    private fun getBaseUrl() {
        viewModelScope.launch {
            repository.getBaseUrl().collect {
                updateState {
                    copy(
                        baseUrl = it
                    )
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            updateState {
                copy(
                    requestCode = requestCode,
                    portfolios = com.attendace.leopard.data.base.PageInitialNotLoaded(
                        1,
                        PORTFOLIO_PAGE_SIZE
                    ),
                    isRefreshing = true
                )
            }
            loadPortfolios()
            loadPortfolioRequestTypes()
        }
    }

    private fun searchTextView(search: String?) {
        viewModelScope.launch {
            updateState {
                copy(
                    searchText = when (search.isNullOrBlank()) {
                        true -> null
                        false -> search
                    },
                    portfolios = com.attendace.leopard.data.base.PageInitialNotLoaded(
                        1,
                        PORTFOLIO_PAGE_SIZE
                    ),
                )
            }
            loadPortfolios()
        }
    }

    fun getPortfolioApplicantApi() {
        viewModelScope.launch {
            repository.getPortfolioApplicantApi().fold(ifRight = {
                updateState {
                    copy(
                        subordinates = com.attendace.leopard.data.base.Loaded(it)
                    )
                }
            }, ifLeft = {
                updateState {
                    copy(
                        subordinates = com.attendace.leopard.data.base.Failed(it)
                    )
                }
            })
        }
    }

    fun selectSubordinate(subordinate: Subordinate) {
        updateState {
            copy(
                selectedSubordinate = subordinate
            )
        }
    }

    fun changeFromDate(date: LocalDate) {
        updateState {
            copy(
                filterPickedFromDate = date
            )
        }
    }

    fun changeToDate(date: LocalDate) {
        updateState {
            copy(
                filterPickedToDate = date
            )
        }
    }


    /*fun modifyPortfolio(portfolioItems: List<Portfolio>, actionId: String) {
        updateState {
            copy(modifyPortfolioResponse = Loading)
        }
        viewModelScope.launch {
            repository.modifyPortfolio(portfolioItems.map { it.toModifyPortfolioInput(actionId) })
                .fold(ifRight = {
                    updateState {
                        copy(modifyPortfolioResponse = Loaded(it))
                    }
                    updateState {
                        copy(
                            portfolios = PageInitialNotLoaded(1, PORTFOLIO_PAGE_SIZE)
                        )
                    }
                    loadPortfolios()
                },
                    ifLeft = {
                    })
        }
    }*/

    companion object {
        const val PORTFOLIO_PAGE_SIZE = 20
    }

    val searchWithDebounce = debounce<String?>(700L, viewModelScope) {
        searchTextView(it)
    }

}



