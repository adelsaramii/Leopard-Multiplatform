package com.attendace.leopard.presentation.screen.register_request

import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.attendace.leopard.presentation.viewmodel.util.state
import com.attendace.leopard.presentation.viewmodel.RegisterRequestViewModel
import com.attendace.leopard.data.model.Bulk
import com.attendace.leopard.data.model.Portfolio
import com.attendace.leopard.data.model.Request
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterRequestScreen(
    popBackStack: () -> Unit,
    modifier: Modifier = Modifier,
    request: Request? = null,
    portfolio: Portfolio? = null,
    selectedFormCodeId: String? = null,
    userId: String? = null,
    bulkRequests: List<Bulk>? = null,
    viewModel: RegisterRequestViewModel = koinInject(),
) {
    val state by viewModel.state()

    val scaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    request?.let { viewModel.setSelectedDoc(it) } ?: portfolio?.let { viewModel.setSelectedDoc(it) }
    ?: run {
        viewModel.getRequestFormType(selectedFormCodeId)
    }

    bulkRequests?.let {
        viewModel.setSelectedBulkRequests(it)
    }

    userId?.let { viewModel.setSelectedUserId(it) }

    BottomSheetScaffold(
        sheetContent = {
            RequestTypeBottomSheet(
                requestTypeForm = state.requestTypes,
                onBackPressed = {
                    coroutineScope.launch {
                        scaffoldState.bottomSheetState.partialExpand()
                    }
                },
                onItemSelected = { viewModel.selectRequestType(it) },
                selectedItem = state.selectedRequestTypes,
                retry = {},
            )
        },
        sheetPeekHeight = 0.dp,
        sheetContainerColor = Color.White,
        sheetContentColor = Color.White,
        scaffoldState = scaffoldState
    ) {
        RequestFormScreen(state = state,
            openRequestTypeBottomSheet = {
                coroutineScope.launch {
                    scaffoldState.bottomSheetState.expand()
                }
            },
            loadRequestForm = {
                viewModel.getRequestForm(it)
            },
            onRequestSubmit = {
                if (state.bulkRequests.isNotEmpty()) {
                    viewModel.addBulkRequest(it)
                } else {
                    viewModel.addRequest(it)
                }
            },
            popBackStack = {
                popBackStack()
            },
            clearValues = {
                viewModel.clearValues()
            },
            onRejectClick = { portfolio?.let { it1 -> viewModel.rejectPortfolioItem(it1) } },
            onConfirmClick = { portfolio?.let { it1 -> viewModel.confirmPortfolioItem(it1) } },
            onDeleteClick = { viewModel.deleteRequestItem((it)) },
            onBulkItemClick = {
                viewModel.changeBulkItemSelection(it)
            }) { key, name, values ->
            if (name.isNotEmpty()) {
                viewModel.setValue("${key}_$name", values)
            } else {
                viewModel.setValue(key, values)
            }
        }
    }

}