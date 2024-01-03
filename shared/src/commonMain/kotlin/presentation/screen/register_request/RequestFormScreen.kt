package com.attendace.leopard.presentation.screen.register_request

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.attendace.leopard.presentation.screen.bulk.list.BulkList
import com.attendace.leopard.presentation.screen.components.LeopardLoadingButton
import com.attendace.leopard.presentation.screen.home.monthly.AppBar
import com.attendace.leopard.presentation.screen.portfolio.list.components.PortfolioConfirmation
import com.attendace.leopard.presentation.screen.register_request.component.DatePicker
import com.attendace.leopard.presentation.screen.register_request.component.PersonCombo
import com.attendace.leopard.presentation.screen.register_request.component.SelectComponent
import com.attendace.leopard.presentation.screen.register_request.component.TextFieldComponent
import com.attendace.leopard.presentation.screen.register_request.component.TimePicker
import com.attendace.leopard.presentation.viewmodel.RegisterRequestViewModel
import com.attendace.leopard.util.theme.delete
import com.attendace.leopard.util.theme.error_message
import com.attendace.leopard.util.theme.gray
import com.attendace.leopard.util.theme.item_has_error
import com.attendace.leopard.util.theme.localization
import com.attendace.leopard.util.theme.not_selected
import com.attendace.leopard.util.theme.primaryColor
import com.attendace.leopard.util.theme.register_request
import com.attendace.leopard.util.theme.request_type
import com.attendace.leopard.util.theme.submit
import com.attendace.leopard.util.theme.textColor
import com.attendace.leopard.util.theme.white
import com.attendace.leopard.presentation.screen.components.portfolioGradientBackground
import com.attendace.leopard.data.model.Bulk
import com.attendance.leopard.data.model.FormItem
import com.attendace.leopard.data.model.Portfolio
import com.attendace.leopard.data.model.Request
import com.attendance.leopard.data.model.RequestFormType
import com.attendace.leopard.data.model.RequestPossibleAction.Accept
import com.attendace.leopard.data.model.RequestPossibleAction.Delete
import com.attendace.leopard.data.model.RequestPossibleAction.Reject

@Composable
fun RequestFormScreen(
    modifier: Modifier = Modifier,
    state: RegisterRequestViewModel.State,
    openRequestTypeBottomSheet: () -> Unit,
    loadRequestForm: (String) -> Unit,
    onRequestSubmit: (String) -> Unit,
    onConfirmClick: (Portfolio) -> Unit,
    onRejectClick: (Portfolio) -> Unit,
    onDeleteClick: (Request) -> Unit,
    onBulkItemClick: (Bulk) -> Unit,
    popBackStack: () -> Unit,
    clearValues: () -> Unit,
    setValue: (String, String, String) -> Unit
) {
    val isLoading = state.formItems is com.attendace.leopard.data.base.Loading || state.requestDetailFormItems is com.attendace.leopard.data.base.Loading
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMessage = localization(error_message).value
    val itemsHasError = localization(item_has_error).value
    LaunchedEffect(key1 = state.selectedRequestTypes) {
        state.selectedRequestTypes?.id?.let { loadRequestForm(it) }
        clearValues()
    }
    LaunchedEffect(key1 = state.addBulkRequestResponse) {
        if (state.addBulkRequestResponse is com.attendace.leopard.data.base.Loaded) {
            if (state.addBulkRequestResponse.data?.validate == true) {
                popBackStack()
                state.addBulkRequestResponse.data?.message?.let {
                    snackbarHostState.showSnackbar(
                        it
                    )
                }
            } else {
                state.addBulkRequestResponse.data?.message?.let {
                    snackbarHostState.showSnackbar(
                        "$it  $itemsHasError"
                    )
                }
            }
        } else if (state.addBulkRequestResponse is com.attendace.leopard.data.base.Failed) {
            snackbarHostState.showSnackbar(
                message = errorMessage
            )
        }

    }
    LaunchedEffect(key1 = state.addRequestResponse) {
        if (state.addRequestResponse is com.attendace.leopard.data.base.Loaded) {
            if (state.addRequestResponse.data?.validate == true) {
                popBackStack()
            }
            state.addRequestResponse.data?.message?.let {
                snackbarHostState.showSnackbar(
                    it
                )
            }
        } else if (state.addRequestResponse is com.attendace.leopard.data.base.Failed) {
            snackbarHostState.showSnackbar(
                message = errorMessage
            )
        }

    }
    LaunchedEffect(key1 = state.confirmPortfolioResponse) {

        if (state.confirmPortfolioResponse is com.attendace.leopard.data.base.Loaded) {
            if (state.confirmPortfolioResponse.data.isNullOrEmpty()) {
                popBackStack()
            }
            state.addRequestResponse.data?.message?.let {
                snackbarHostState.showSnackbar(
                    it
                )
            }
        } else if (state.addRequestResponse is com.attendace.leopard.data.base.Failed) {

            snackbarHostState.showSnackbar(
                message = errorMessage
            )
        }
    }
    LaunchedEffect(key1 = state.rejectPortfolioResponse) {
        if (state.rejectPortfolioResponse is com.attendace.leopard.data.base.Loaded) {
            if (state.rejectPortfolioResponse.data.isNullOrEmpty()) {
                popBackStack()
            }
            state.addRequestResponse.data?.message?.let {
                snackbarHostState.showSnackbar(
                    it
                )
            }
        } else if (state.addRequestResponse is com.attendace.leopard.data.base.Failed) {
            snackbarHostState.showSnackbar(
                errorMessage
            )
        }

    }

    LaunchedEffect(key1 = state.deleteRequestResponse) {
        if (state.deleteRequestResponse is com.attendace.leopard.data.base.Loaded) {
            if (state.deleteRequestResponse.data == true) {
                popBackStack()
            } else {
                snackbarHostState.showSnackbar(
                    message = errorMessage
                )
            }
        } else if (state.addRequestResponse is com.attendace.leopard.data.base.Failed) {
            snackbarHostState.showSnackbar(
                message = errorMessage
            )
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(modifier = Modifier.weight(1f)) {

                AppBar(
                    title = localization(register_request).value,
                    onSubordinateClick = {},
                    modifier = Modifier
                        .portfolioGradientBackground()
                        .padding(horizontal = 8.dp)
                        .padding(top = 8.dp),
                    navigationIconId = "arrow_left.xml",
                    onNavigationIconClick = {
                        popBackStack()
                    },
                )

                if (isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }

                Column(verticalArrangement = Arrangement.SpaceBetween) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                            .verticalScroll(rememberScrollState())
                            .weight(1f)
                    ) {
                        if (!state.isDetail) {
                            RequestTypeSelect(
                                state.selectedRequestTypes,
                                openRequestTypeBottomSheet = openRequestTypeBottomSheet
                            )
                        }

                        Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                            if (!state.isDetail) {
                                RequestForm(
                                    formItems = state.formItems,
                                    setValue = setValue,
                                    userId = state.userId,
                                )
                            } else {
                                RequestDetailForm(
                                    formItems = state.requestDetailFormItems,
                                    setValue = setValue,
                                    userId = state.userId,
                                )
                            }
                        }


                    }

                    BulkList(
                        modifier = Modifier.heightIn(max = 240.dp),
                        requests = state.bulkRequests,
                        onBulkItemClicked = { onBulkItemClick(it) }) {
                    }
                    FormActionButton(
                        state,
                        onRequestSubmit,
                        onConfirmClick,
                        onRejectClick,
                        onDeleteClick,
                    )
                }

            }

        }
    }


}

@Composable
fun FormComponent(
    formItem: FormItem,
    isDetail: Boolean = false,
    userId: String?,
    setValue: (String, String, String) -> Unit
) {

    when (formItem.component) {
        "PERSON_COMBO" -> {
            PersonCombo(
                isDetail = isDetail,
                formItem = formItem,
                selectedUserId = userId
            ) {
                setValue(formItem.key, "Id", it.id)
                setValue(formItem.key, "Title", it.title)
            }
        }

        "DATE_COMPONENT" -> {
            DatePicker(formItem = formItem) {
                setValue(formItem.key, "", it)
            }
        }

        "TIME_COMPONENT" -> {
            TimePicker(formItem = formItem) {
                setValue(formItem.key, "", it)
            }
        }

        "SELECT_LIST" -> {
            SelectComponent(formItem = formItem) {
                setValue(formItem.key, "", it.id)
            }
        }

        "TEXT_AREA" -> {
            TextFieldComponent(formItem = formItem) {
                setValue(formItem.key, "", it)
            }
        }

        "TEXT_BOX" -> {
            TextFieldComponent(formItem = formItem) {
                setValue(formItem.key, "", it)
            }
        }
    }
}


@Composable
fun RequestTypeSelect(
    selectedRequestType: RequestFormType?,
    openRequestTypeBottomSheet: () -> Unit
) {
    Text(
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
        text = localization(request_type).value,
        color = textColor,
        style = MaterialTheme.typography.body2,
        fontWeight = FontWeight.W500,
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .border(width = 1.dp, color = gray, shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
            .clickable {
                openRequestTypeBottomSheet()
            },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = selectedRequestType?.name ?: localization(not_selected).value,
                style = MaterialTheme.typography.body2,
                color = textColor,
            )
        }
    }
}

@Composable
fun RequestForm(
    formItems: com.attendace.leopard.data.base.LoadableData<List<FormItem>>,
    userId: String?,
    setValue: (String, String, String) -> Unit,
) {
    formItems.data?.forEach {
        FormComponent(
            formItem = it, userId = userId,
            setValue = { key, name, value ->
                setValue(key, name, value)
            },
        )
    }
}

@Composable
fun RequestDetailForm(
    formItems: com.attendace.leopard.data.base.LoadableData<List<FormItem>>,
    userId: String?,
    setValue: (String, String, String) -> Unit,
) {
    formItems.data?.forEach {
        FormComponent(
            formItem = it,
            userId = userId,
            isDetail = true,
            setValue = { key, name, value ->
                setValue(key, name, value)
            },
        )
    }
}


@Composable
fun FormActionButton(
    state: RegisterRequestViewModel.State,
    onRequestSubmit: (String) -> Unit,
    onConfirmClick: (Portfolio) -> Unit,
    onRejectClick: (Portfolio) -> Unit,
    onDeleteClick: (Request) -> Unit
) {
    if (!state.isDetail) {
        LeopardLoadingButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor),
            isLoading = state.addRequestResponse is com.attendace.leopard.data.base.Loading || state.addBulkRequestResponse is com.attendace.leopard.data.base.Loading,
            onClick = {
                state.selectedRequestTypes?.let {
                    onRequestSubmit(it.id)
                }
            },
        ) {
            Text(
                text = localization(submit).value,
                color = white,
                style = MaterialTheme.typography.button,
            )
        }
    }
    if (state.requestDetailFormItems is com.attendace.leopard.data.base.Loaded) {
        state.selectedRequest.data?.let { request ->
            if (request.possibleAction.any { it == Delete }) {
                LeopardLoadingButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor),
                    isLoading = state.deleteRequestResponse is com.attendace.leopard.data.base.Loading,
                    onClick = {
                        onDeleteClick(request)
                    },
                ) {
                    Text(
                        text = localization(delete).value,
                        color = white,
                        style = MaterialTheme.typography.button,
                    )
                }
            }
        }
        state.selectedPortfolio.data?.let {
            Row {
                if (it.possibleAction.any { it == Reject } && it.possibleAction.any { it == Accept }) {
                    PortfolioConfirmation(
                        confirmLoading = state.confirmPortfolioResponse is com.attendace.leopard.data.base.Loading,
                        rejectLoading = state.rejectPortfolioResponse is com.attendace.leopard.data.base.Loading,
                        onConfirmClick = { onConfirmClick(it) },
                        onRejectClick = { onRejectClick(it) },
                    )
                }
            }
        }
    }
}