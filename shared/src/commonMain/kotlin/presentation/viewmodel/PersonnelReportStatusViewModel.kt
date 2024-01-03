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
import com.attendance.leopard.data.model.LeopardTabBarTypes
import com.attendace.leopard.data.model.PersonnelReportStatusModel
import com.attendace.leopard.data.model.PersonnelReportStatusTypeEnum
import com.attendace.leopard.data.repository.personnel_report_status.PersonnelStatusReportRepository
import kotlinx.coroutines.launch

class PersonnelReportStatusViewModel constructor(
    private val repository: PersonnelStatusReportRepository
) : com.attendace.leopard.data.base.BaseViewModel<PersonnelReportStatusViewModel.State>(State()) {

    data class State(
        val personnelReportStatus: com.attendace.leopard.data.base.PaginationLoadableData<List<PersonnelReportStatusModel>> = com.attendace.leopard.data.base.PageInitialNotLoaded(
            1,
            PersonnelReportStatus_PAGE_SIZE
        ),
        val accessToken: String = "",
        val baseUrl: String = "",
        val leopardTabBarTypes: com.attendace.leopard.data.base.LoadableData<List<LeopardTabBarTypes>> = com.attendace.leopard.data.base.NotLoaded,
        var status: String? = PersonnelReportStatusTypeEnum.Absents.name,
        var isRefreshing: Boolean = false,
        var searchText: String? = null,
        var refreshScroll: Boolean = false, // cuz we use one lazy column
    )

    init {
        getBaseUrl()
        getToken()
        loadPersonnelReportStatusTypes()
        loadPersonnelReportStatus()
    }

    private fun loadPersonnelReportStatus() {
        if (currentState.personnelReportStatus is com.attendace.leopard.data.base.PageInitialNotLoaded) {
            updateState {
                copy(refreshScroll = !refreshScroll)
            }
        }

        if (!currentState.personnelReportStatus.hasMorePages()) return
        updateState {
            copy(personnelReportStatus = personnelReportStatus.toLoading())
        }

        viewModelScope.launch {
            repository.getPersonnelStatusReportList(
                currentState.personnelReportStatus.page,
                currentState.personnelReportStatus.limit,
                currentState.status,
                currentState.searchText
            ).fold(ifRight = {
                updateState { copy(isRefreshing = false) }
                updateState {
                    copy(
                        personnelReportStatus = personnelReportStatus.toLoaded(
                            data = personnelReportStatus.data.orEmpty() + it,
                            page = personnelReportStatus.page + 1,
                            addedCount = it.size,
                            hasMorePages = (it.size == PersonnelReportStatus_PAGE_SIZE)
                        )
                    )
                }
            }, ifLeft = {
                updateState { copy(isRefreshing = false) }
                updateState {
                    copy(
                        personnelReportStatus = personnelReportStatus.toFailed(
                            throwable = Throwable(it.getErrorMessage()),
                            title = it.getErrorMessage()
                        )
                    )
                }
            })
        }
    }

    private fun loadPersonnelReportStatusTypes() {
        updateState {
            copy(leopardTabBarTypes = com.attendace.leopard.data.base.Loading)
        }
        viewModelScope.launch {
            repository.getPersonnelStatusReportTypes().fold(ifRight = {
                updateState {
                    copy(
                        leopardTabBarTypes = com.attendace.leopard.data.base.Loaded(it)
                    )
                }
            }, ifLeft = {
                updateState {
                    copy(
                        leopardTabBarTypes = com.attendace.leopard.data.base.Failed(it)
                    )
                }
            })
        }
    }

    fun onLoadMoreCalled() {
        loadPersonnelReportStatus()
    }

    fun changeStatus(status: String?) {
        updateState {
            copy(
                status = status,
                personnelReportStatus = com.attendace.leopard.data.base.PageInitialNotLoaded(
                    1,
                    PortfolioViewModel.PORTFOLIO_PAGE_SIZE
                )
            )
        }
        loadPersonnelReportStatus()
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

    fun retry() {
        loadPersonnelReportStatusTypes()
        loadPersonnelReportStatus()
    }

    fun refresh() {
        viewModelScope.launch {
            updateState {
                copy(
                    status = status,
                    personnelReportStatus = com.attendace.leopard.data.base.PageInitialNotLoaded(
                        1,
                        PersonnelReportStatus_PAGE_SIZE
                    ),
                    isRefreshing = true
                )
            }
            loadPersonnelReportStatus()
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
                    personnelReportStatus = com.attendace.leopard.data.base.PageInitialNotLoaded(
                        1,
                        PersonnelReportStatus_PAGE_SIZE
                    ),
                )
            }
            loadPersonnelReportStatus()
        }
    }

    val searchWithDebounce = debounce<String?>(700L, viewModelScope) {
        searchTextView(it)
    }

    companion object {
        const val PersonnelReportStatus_PAGE_SIZE = 20
    }

}