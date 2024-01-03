package com.attendace.leopard.presentation.screen.auth

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.data.base.Loading
import com.attendace.leopard.presentation.screen.auth.util.LoginFlowPager
import com.attendace.leopard.presentation.screen.auth.util.TopPagerContent
import com.attendace.leopard.presentation.screen.auth.util.locale.LocaleBottomSheet
import com.attendace.leopard.presentation.viewmodel.util.state
import com.attendace.leopard.presentation.viewmodel.AuthViewModel
import com.attendace.leopard.presentation.screen.qrScanner.QrScannerScreen
import com.attendace.leopard.util.theme.error_message
import com.attendace.leopard.util.theme.language
import com.attendace.leopard.util.theme.localization
import com.attendace.leopard.util.theme.login_error
import com.attendace.leopard.util.theme.neutralLight0Dark10
import com.attendace.leopard.util.theme.red
import com.attendace.leopard.util.theme.secondary
import com.attendace.leopard.util.theme.white
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun AuthScreen(
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = koinInject()
) {
    val state by viewModel.state()

    val snackBarHostState = remember {
        SnackbarHostState()
    }
    val coroutineScope = rememberCoroutineScope()
    val loginFlowPagerState =
        rememberPagerState(initialPage = 1, initialPageOffsetFraction = 0f) { 3 }

    LaunchedEffect(state.loginFlowPage) {
        loginFlowPagerState.scrollToPage(state.loginFlowPage)
    }
    LaunchedEffect(state.snackbarError) {
        state.snackbarError?.let {
            snackBarHostState.showSnackbar(
                if (it.getErrorMessage()?.contains("invalid_grant") == true) {
                    localization(login_error).value
                } else {
                    localization(error_message).value
                }
            )
        }
    }

    if (state.userInfo is Loaded) {
        navigateToHome()
    }

    val scaffoldState = rememberBottomSheetScaffoldState()
    var bottomSheetState by remember { mutableStateOf(false) }

    var qrCode = remember {
        mutableStateOf(false)
    }

    val ipAddress = remember {
        mutableStateOf("")
    }

    LaunchedEffect(state.baseUrl) {
        ipAddress.value = state.baseUrl
    }

    LaunchedEffect(scaffoldState.bottomSheetState.targetValue) {
        if (scaffoldState.bottomSheetState.targetValue == SheetValue.Expanded) {
            bottomSheetState = true
        } else if (scaffoldState.bottomSheetState.targetValue == SheetValue.PartiallyExpanded) {
            bottomSheetState = false
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            LocaleBottomSheet(
                modifier = Modifier.heightIn(min = 100.dp, max = 300.dp).background(white),
                currentLanguage = state.currentLanguageTag,
                onCloseClick = {
                    coroutineScope.launch {
                        scaffoldState.bottomSheetState.partialExpand()
                    }
                },
            ) {
                viewModel.setLocale(it.language, it.name)
                language.value = it.language
                coroutineScope.launch {
                    scaffoldState.bottomSheetState.partialExpand()
                }
            }

        },
        sheetPeekHeight = 0.dp,
        sheetContainerColor = Color.White,
        sheetContentColor = Color.White,
        contentColor = Color.White,
        containerColor = Color.White,
        sheetShape = RoundedCornerShape(
            bottomStart = 0.dp, bottomEnd = 0.dp, topStart = 12.dp, topEnd = 12.dp
        ),
        modifier = modifier,
    ) {
        Scaffold(
            Modifier
                .fillMaxSize()
                .background(white),
            snackbarHost = {
                SnackbarHost(snackBarHostState) { data ->
                    Snackbar(
                        containerColor = red,
                        contentColor = neutralLight0Dark10,
                        snackbarData = data
                    )
                }
            },
        ) { paddingValues ->

            Column(
                horizontalAlignment = CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (!qrCode.value) {
                    TopPagerContent(
                        currentLanguageName = state.currentLanguageName,
                        loginFlowPagerState = loginFlowPagerState,
                        onChangeLocaleClick = {
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.expand()
                            }
                        },
                        onBackClick = { viewModel.setLoginFlowPage(0) },
                    )
                    LoginFlowPager(
                        loginFlowPagerState = loginFlowPagerState,
                        ipAddress = ipAddress.value,
                        isSubmitServerButtonLoading = state.initializeResponse is Loading,
                        isLoginButtonLoading = state.loginResponse is Loading || state.userInfo is Loading,
                        navigateToQrCode = {
                            qrCode.value = true
                        },
                        submitServerAddress = { viewModel.initialize(it) },
                        login = { username, password -> viewModel.login(username, password) },
                    )
                } else {
                    QrScannerScreen(Modifier.fillMaxSize()) {
                        qrCode.value = false
                        ipAddress.value = it
                    }
                }
            }

            if (bottomSheetState) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = BottomSheetDefaults.ScrimColor)
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) {
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.partialExpand()
                            }
                        },
                )
            }
        }
    }

}