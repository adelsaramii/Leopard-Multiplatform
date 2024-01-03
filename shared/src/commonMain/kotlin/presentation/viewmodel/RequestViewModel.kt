package com.attendace.leopard.presentation.viewmodel

import com.attendace.leopard.data.repository.auth.AuthRepository
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
import com.attendace.leopard.util.helper.debounce
import com.attendance.leopard.data.model.LeopardTabBarTypes
import com.attendace.leopard.data.model.Request
import com.attendance.leopard.data.model.User
import com.attendace.leopard.data.repository.my_request.MyRequestRepository
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

class RequestViewModel(
    val repository: MyRequestRepository,
    val authRepository: AuthRepository
) : com.attendace.leopard.data.base.BaseViewModel<RequestViewModel.State>(
    State()
) {

    data class State(
        val request: com.attendace.leopard.data.base.PaginationLoadableData<List<Request>> = com.attendace.leopard.data.base.PageInitialNotLoaded(
            1, REQUESTS_PAGE_SIZE
        ),
        val filterPickedFromDate: LocalDate? = null,
        val filterPickedToDate: LocalDate? = null,
        val filterPickedApplicant: String? = null,
        val accessToken: String = "",
        var requestCode: String? = null,
        val requestTypes: com.attendace.leopard.data.base.LoadableData<List<LeopardTabBarTypes>> = com.attendace.leopard.data.base.NotLoaded,
        val codeId: String? = null,
        val statusName: String? = null,
        val statusId: String? = null,
        var searchText: String? = null,
        var isRefreshing: Boolean = false,
        var fromDate: String? = null,
        var endDate: String? = null,
        val userInfo: com.attendace.leopard.data.base.LoadableData<User> = com.attendace.leopard.data.base.NotLoaded,

        )

    init {
        getUserInfo()
        loadRequest()
        loadRequestTypes()
    }

    private fun loadRequest() {
        if (!currentState.request.hasMorePages()) return
        if (currentState.request.page == 1) updateState {
            copy(request = request.toLoading())
        }
        viewModelScope.launch {
            currentState.fromDate = currentState.filterPickedFromDate?.toString()
            currentState.endDate = currentState.filterPickedToDate?.toString()

            repository.getRequest(
                codeId = currentState.codeId,
                startDate = currentState.fromDate,
                endDate = currentState.endDate,
                status = currentState.statusId,
                searchValue = currentState.searchText,
                pageNumber = currentState.request.page,
                pageSize = currentState.request.limit,
            ).fold(ifRight = {
                updateState {
                    copy(
                        request = request.toLoaded(
                            data = request.data.orEmpty() + it,
                            page = request.page + 1,
                            addedCount = it.size,
                            hasMorePages = (it.size == REQUESTS_PAGE_SIZE)
                        ), isRefreshing = false
                    )
                }
            }, ifLeft = {
                updateState {
                    copy(
                        request = request.toFailed(
                            throwable = Throwable(it.getErrorMessage()),
                            title = it.getErrorMessage()
                        ), isRefreshing = false
                    )
                }
            })
        }
    }

    private fun loadRequestTypes() {
        updateState {
            copy(requestTypes = com.attendace.leopard.data.base.Loading)
        }
        viewModelScope.launch {
            repository.getRequestTypes().fold(ifRight = {
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

    fun onLoadMoreCalled() {
        loadRequest()
    }

    fun changeRequestCode(requestCode: String?) {
        updateState {
            copy(
                codeId = requestCode, request = com.attendace.leopard.data.base.PageInitialNotLoaded(
                    1,
                    REQUESTS_PAGE_SIZE
                )
            )
        }
        loadRequest()
    }

    fun refresh() {
        viewModelScope.launch {
            updateState {
                copy(
                    requestCode = requestCode,
                    request = com.attendace.leopard.data.base.PageInitialNotLoaded(
                        1,
                        REQUESTS_PAGE_SIZE
                    ),
                    isRefreshing = true
                )
            }
            loadRequest()
            loadRequestTypes()
        }
    }

    fun searchTextView(search: String?) {
        viewModelScope.launch {
                updateState {
                    copy(
                        searchText = when (search.isNullOrBlank()) {
                            true -> null
                            false -> search
                        },
                        request = com.attendace.leopard.data.base.PageInitialNotLoaded(
                            1,
                            REQUESTS_PAGE_SIZE
                        ),
                    )
                }
                loadRequest()
        }
    }

    val searchWithDebounce = debounce<String?>(700L, viewModelScope) {
        searchTextView(it)
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

    fun applyFilters(fromDate: LocalDate?, toDate: LocalDate?, statusId: String?) {
        updateState {
            copy(
                filterPickedFromDate = fromDate,
                filterPickedToDate = toDate,
                statusId = statusId,
                request = com.attendace.leopard.data.base.PageInitialNotLoaded(
                    1,
                    REQUESTS_PAGE_SIZE
                ),
            )
        }
        loadRequest()
    }

    fun resetAllFilters() {
        updateState {
            copy(
                filterPickedApplicant = null,
                filterPickedFromDate = null,
                filterPickedToDate = null,
                statusId = null,
                statusName = null,
                request = com.attendace.leopard.data.base.PageInitialNotLoaded(
                    1,
                    REQUESTS_PAGE_SIZE
                ),
            )
        }
        loadRequest()
    }

    fun setStatusName(statusName: String) {
        updateState {
            copy(
                statusName = statusName
            )
        }
    }

    fun setStatusId(statusId: String) {
        updateState {
            copy(
                statusId = statusId
            )
        }
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            authRepository.getOfflineUserInfo().collect {
                updateState { copy(userInfo = com.attendace.leopard.data.base.Loaded(it)) }
            }
        }
    }

    companion object {
        const val REQUESTS_PAGE_SIZE = 20
    }
}
