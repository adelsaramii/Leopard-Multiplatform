package com.attendace.leopard.presentation.screen.auth.util

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.attendace.leopard.util.theme.secondary

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginFlowPager(
    loginFlowPagerState: PagerState,
    ipAddress: String,
    isSubmitServerButtonLoading: Boolean,
    isLoginButtonLoading: Boolean,
    navigateToQrCode: () -> Unit,
    submitServerAddress: (String) -> Unit,
    login: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    HorizontalPager(
        modifier = modifier
            .fillMaxWidth(0.85f)
            .fillMaxHeight(1f),
        state = loginFlowPagerState,
        userScrollEnabled = false
    ) { page ->
        when (page) {
            0 -> {
                ServerAddressPage(
                    ipAddress = ipAddress,
                    buttonLoading = isSubmitServerButtonLoading,
                    navigateToQrCode = navigateToQrCode,
                    onNextButtonClick = submitServerAddress
                )
            }
            1 -> {
                UsernamePasswordPage(
                    buttonLoading = isLoginButtonLoading,
                    onLoginButtonClick = { username, password ->
                        login(username, password)
                    }
                )
            }
        }
    }

}
