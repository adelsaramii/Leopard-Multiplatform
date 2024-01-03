package com.attendace.leopard.presentation.viewmodel

import com.attendace.leopard.data.base.BaseViewModel
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.data.base.Loading
import com.attendance.leopard.data.model.RequestSelectComponent
import com.attendace.leopard.data.repository.request.RequestRepository
import kotlinx.coroutines.launch

class SelectComponentViewModel(
    private val requestRepository: RequestRepository
) : com.attendace.leopard.data.base.BaseViewModel<SelectComponentViewModel.State>(
    State()
) {

    data class State(
        val data: MutableMap<String, com.attendace.leopard.data.base.LoadableData<List<RequestSelectComponent>>> = mutableMapOf(),
        val selectedItem: MutableMap<String, RequestSelectComponent> = mutableMapOf(),
        val selectedItem1:RequestSelectComponent?=null,
        val url: String? = null,
        val selectedUserId: String? = null,
    )

    fun setSelectComponentData(key: String, items: List<RequestSelectComponent>) {
        currentState.data[key] = com.attendace.leopard.data.base.Loaded(items)
        updateState {
            copy(data = currentState.data)
        }
    }

    fun setSelectedItem(key: String, item: RequestSelectComponent) {
        currentState.data[key]?.data?.forEach {
            it.isSelected = false
        }
        currentState.data[key]?.data?.firstOrNull{it.id==item.id}?.isSelected = true
        currentState.selectedItem[key] = item
        updateState { copy(selectedItem = currentState.selectedItem , data = currentState.data) }
    }

    fun setSelectedUserId(userId: String) {
        updateState { copy(selectedUserId = userId) }
    }

    fun setUrl(formKey: String, url: String, selectedUserId: String? = null) {
        updateState {
            copy(url = url)
        }

        viewModelScope.launch {
            currentState.data[formKey] = com.attendace.leopard.data.base.Loading
            updateState { copy(data = currentState.data) }
            requestRepository.getPersonComponentData(
                url,
                pageNumber = 1,
                pageSize = 10000,
                searchValue = ""
            ).fold(ifRight = { data ->
                currentState.data[formKey] = com.attendace.leopard.data.base.Loaded(data)
                val selectedUser = data.firstOrNull { it.id == selectedUserId }
                selectedUser?.let {
                    it.isSelected = true
                }

                updateState {
                    copy(
                        selectedItem = currentState.selectedItem,
                        data = currentState.data,
                        selectedItem1 = selectedUser)
                }

            },
                ifLeft = {

                })
        }
    }
}