package com.attendace.leopard.presentation.viewmodel

import com.attendace.leopard.data.base.BaseViewModel
import com.attendace.leopard.data.base.Failed
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.data.base.Loading
import com.attendace.leopard.data.base.NotLoaded
import com.attendace.leopard.data.model.Bulk
import com.attendace.leopard.data.model.Portfolio
import com.attendace.leopard.data.model.Request
import com.attendace.leopard.data.model.toModifyPortfolioInput
import com.attendance.leopard.data.model.*
import com.attendace.leopard.data.repository.portfolio.PortfolioRepository
import com.attendace.leopard.data.repository.request.RequestRepository
import kotlinx.coroutines.launch

class RegisterRequestViewModel(
    private val requestRepository: RequestRepository,
    private val portfolioRepository: PortfolioRepository,
) : com.attendace.leopard.data.base.BaseViewModel<RegisterRequestViewModel.State>(
    State()
) {

    data class State(
        val requestTypes: com.attendace.leopard.data.base.LoadableData<List<RequestFormType>> = com.attendace.leopard.data.base.NotLoaded,
        val formItems: com.attendace.leopard.data.base.LoadableData<List<FormItem>> = com.attendace.leopard.data.base.NotLoaded,
        val requestDetailFormItems: com.attendace.leopard.data.base.LoadableData<List<FormItem>> = com.attendace.leopard.data.base.NotLoaded,
        val selectedRequestTypes: RequestFormType? = null,
        val formItemsValue: MutableMap<String, String> = mutableMapOf(),
        val addRequestResponse: com.attendace.leopard.data.base.LoadableData<AddRequestResponse> = com.attendace.leopard.data.base.NotLoaded,
        val addBulkRequestResponse: com.attendace.leopard.data.base.LoadableData<AddRequestResponse> = com.attendace.leopard.data.base.NotLoaded,
        val docId: String? = null,
        val userId: String? = null,
        val confirmPortfolioResponse: com.attendace.leopard.data.base.LoadableData<List<ModifyPortfolioResponse>> = com.attendace.leopard.data.base.NotLoaded,
        val rejectPortfolioResponse: com.attendace.leopard.data.base.LoadableData<List<ModifyPortfolioResponse>> = com.attendace.leopard.data.base.NotLoaded,
        val deleteRequestResponse: com.attendace.leopard.data.base.LoadableData<Boolean> = com.attendace.leopard.data.base.NotLoaded,
        val isDetail: Boolean = false,
        val bulkRequests: List<Bulk> = emptyList(),
        val selectedRequest: com.attendace.leopard.data.base.LoadableData<Request> = com.attendace.leopard.data.base.NotLoaded,
        val selectedPortfolio: com.attendace.leopard.data.base.LoadableData<Portfolio> = com.attendace.leopard.data.base.NotLoaded,
    )

    fun getRequestFormType(codeId: String? = null) {
        viewModelScope.launch {
            requestRepository.getRequestFormType(codeId).fold(
                ifRight = {
                    updateState { copy(requestTypes = com.attendace.leopard.data.base.Loaded(it)) }
                },
                ifLeft = {
                    updateState { copy(requestTypes = com.attendace.leopard.data.base.Failed(it)) }
                }
            )
        }
    }

    fun getRequestForm(formId: String) {
        updateState { copy(formItems = com.attendace.leopard.data.base.Loading) }
        viewModelScope.launch {
            requestRepository.getRequestForm(formId, currentState.bulkRequests.isNotEmpty()).fold(
                ifRight = {
                    updateState { copy(formItems = com.attendace.leopard.data.base.Loaded(it)) }
                },
                ifLeft = {
                    updateState { copy(formItems = com.attendace.leopard.data.base.Failed(it)) }
                }
            )
        }
    }

    fun addRequest(formId: String) {
        val values = currentState.formItemsValue
        val formItems = currentState.formItems.data
        var validateForm = true
        formItems?.forEach { formItem ->
            if (formItem.required && !values.containsKey(formItem.key) &&
                !values.containsKey("${formItem.key}_Id")
            ) {
                validateForm = false
            }
        }
        if (!validateForm) {
            updateState {
                copy(
                    addRequestResponse = com.attendace.leopard.data.base.Loaded(
                        AddRequestResponse(
                            message = "Please Fill All Required Fields",
                            validate = false
                        )
                    )
                )
            }
        } else {
            updateState { copy(addRequestResponse = com.attendace.leopard.data.base.Loading) }
            viewModelScope.launch {
                requestRepository.addRequest(formId, currentState.formItemsValue).fold(
                    ifLeft = {
                        updateState { copy(addRequestResponse = com.attendace.leopard.data.base.Failed(
                            it
                        )
                        ) }
                    },
                    ifRight = {
                        updateState { copy(addRequestResponse = com.attendace.leopard.data.base.Loaded(
                            it
                        )
                        ) }
                    }
                )
            }
        }
    }

    fun addBulkRequest(formId: String) {
        val values = currentState.formItemsValue
        val formItems = currentState.formItems.data
        var validateForm = true
        formItems?.forEach { formItem ->
            if (formItem.required && formItem.editable && !values.containsKey(formItem.key) &&
                !values.containsKey("${formItem.key}_Id")
            ) {
                validateForm = false
            }
        }
        if (!validateForm) {
            updateState {
                copy(
                    addRequestResponse = com.attendace.leopard.data.base.Loaded(
                        AddRequestResponse(
                            message = "Please Fill All Required Fields",
                            validate = false
                        )
                    )
                )
            }
        } else {
            updateState { copy(addBulkRequestResponse = com.attendace.leopard.data.base.Loading) }
            viewModelScope.launch {
                requestRepository.addBulkRequest(
                    data = AddBulkRequestInput(
                        formData = currentState.formItemsValue,
                        periods = currentState.bulkRequests.filter { it.isSelected }.map {
                            Period(
                                it.startDate,
                                it.endDate
                            )
                        }
                    ), formKey = formId
                ).fold(
                    ifLeft = {
                        updateState { copy(addBulkRequestResponse = com.attendace.leopard.data.base.Failed(
                            it
                        )
                        ) }
                    },
                    ifRight = {
                        updateState { copy(addBulkRequestResponse = com.attendace.leopard.data.base.Loaded(
                            it
                        )
                        ) }
                    }
                )
            }
        }
    }

    private fun getRequestDetailRequest(docId: String) {
        updateState { copy(requestDetailFormItems = com.attendace.leopard.data.base.Loading) }
        viewModelScope.launch {
            requestRepository.getRequestDetail(docId).fold(
                ifLeft = {
                    updateState { copy(requestDetailFormItems = com.attendace.leopard.data.base.Failed(
                        it
                    )
                    ) }
                },
                ifRight = {
                    updateState { copy(requestDetailFormItems = com.attendace.leopard.data.base.Loaded(
                        it
                    )
                    ) }
                }
            )
        }
    }

    fun selectRequestType(requestFormType: RequestFormType) {
        updateState {
            copy(selectedRequestTypes = requestFormType)
        }
    }


    fun setValue(key: String, value: String) {
        currentState.formItemsValue[key] = value
        updateState {
            copy(formItemsValue = currentState.formItemsValue)
        }
    }

    fun clearValues() {
        updateState { copy(formItemsValue = mutableMapOf()) }
    }

    fun setSelectedBulkRequests(bulkRequests: List<Bulk>) {
        updateState { copy(bulkRequests = bulkRequests) }
    }

    fun setSelectedUserId(id: String) {
        updateState { copy(userId = id) }
    }

    fun setSelectedDoc(doc: Request) {
        updateState { copy(docId = docId, isDetail = true, selectedRequest = com.attendace.leopard.data.base.Loaded(
            doc
        )
        ) }
        getRequestDetailRequest(doc.docId)
    }

    fun setSelectedDoc(doc: Portfolio) {
        updateState { copy(docId = docId, isDetail = true, selectedPortfolio = com.attendace.leopard.data.base.Loaded(
            doc
        )
        ) }
        getRequestDetailRequest(doc.docId)
    }

    fun confirmPortfolioItem(portfolioItem: Portfolio) {
        updateState {
            copy(confirmPortfolioResponse = com.attendace.leopard.data.base.Loading)
        }
        viewModelScope.launch {
            portfolioRepository.modifyPortfolio(
                listOf(portfolioItem.toModifyPortfolioInput("f5034853-0965-4cf7-b7a6-c6ccff94541c"))
            ).fold(ifRight = {
                updateState {
                    copy(confirmPortfolioResponse = com.attendace.leopard.data.base.Loaded(it))
                }
            }, ifLeft = {})
        }
    }

    fun deleteRequestItem(request: Request) {
        updateState {
            copy(deleteRequestResponse = com.attendace.leopard.data.base.Loading)
        }
        viewModelScope.launch {
            requestRepository.deleteRequest(
                request.docId
            ).fold(ifRight = {
                updateState {
                    copy(deleteRequestResponse = com.attendace.leopard.data.base.Loaded(it))
                }
            }, ifLeft = {
                updateState {
                    copy(deleteRequestResponse = com.attendace.leopard.data.base.Failed(it))
                }
            })
        }
    }

    fun rejectPortfolioItem(portfolioItem: Portfolio) {
        updateState {
            copy(rejectPortfolioResponse = com.attendace.leopard.data.base.Loading)
        }
        viewModelScope.launch {
            portfolioRepository.modifyPortfolio(
                listOf(portfolioItem.toModifyPortfolioInput("be3e41eb-815e-4fd3-bea2-feb29a6f2a45"))
            ).fold(ifRight = {
                updateState {
                    copy(confirmPortfolioResponse = com.attendace.leopard.data.base.Loaded(it))
                }
            }, ifLeft = {})
        }
    }

    fun changeBulkItemSelection(selectedItem: Bulk) {
        val newItems = currentState.bulkRequests
        updateState {
            copy(bulkRequests = newItems.map {
                if (it.id == selectedItem.id)
                    it.copy(isSelected = !selectedItem.isSelected)
                else it
            })
        }
    }

}