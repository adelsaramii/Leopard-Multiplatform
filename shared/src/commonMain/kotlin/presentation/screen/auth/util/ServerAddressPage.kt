package com.attendace.leopard.presentation.screen.auth.util

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.attendace.leopard.util.theme.enter_server_address
import com.attendace.leopard.util.theme.localization
import com.attendace.leopard.util.theme.next
import com.attendace.leopard.util.theme.primaryColor
import com.attendace.leopard.util.theme.required_message
import com.attendace.leopard.util.theme.scan_qr_code
import com.attendace.leopard.util.theme.server_address
import com.attendace.leopard.util.theme.white
import com.attendace.leopard.presentation.screen.components.Form
import com.attendace.leopard.presentation.screen.components.FormTextFieldData
import com.attendace.leopard.presentation.screen.components.LeopardLoadingButton
import com.attendace.leopard.presentation.screen.components.LeopardTextField
import com.attendace.leopard.util.helper.Validator
import com.attendace.leopard.util.helper.extractErrorMessage
import com.attendace.leopard.util.helper.openUrl
import com.attendace.leopard.util.theme.secondary
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class,
    ExperimentalResourceApi::class,
)
@Composable
fun ServerAddressPage(
    ipAddress: String,
    buttonLoading: Boolean,
    navigateToQrCode: () -> Unit,
    onNextButtonClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()
    var ip by rememberSaveable(ipAddress) {
        mutableStateOf(ipAddress)
    }
    val ipValidators = listOf(
        Validator.NotEmpty(
            localization(required_message).value
        ),
        Validator.MinimumLength(3, message = "minimum length for IP is 3")
    )
    val isFormSubmitted by remember { mutableStateOf(false) }

    var errorString: String? by remember {
        mutableStateOf(null)
    }

    val relocationRequester = remember { BringIntoViewRequester() }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                localization(enter_server_address).value,
                style = MaterialTheme.typography.h6,
                color = primaryColor
            )
            Spacer(modifier = Modifier.height(16.dp))

            val formData = listOf(
                FormTextFieldData(
                    value = ipAddress,
                    label = localization(server_address).value,
                    relocationRequester = relocationRequester,
                    onKeyboardDoneClicked = {
                        ip = it
                        errorString = ipValidators.extractErrorMessage(ip).orEmpty()
                        if (errorString.isNullOrEmpty()) {
                            coroutineScope.launch {
                                onNextButtonClick(ip)
                            }
                        }
                    },
                    scope = coroutineScope,
                    singleLine = true,
                    onValueChange = {
                        ip = it
                        if (errorString?.isNotEmpty() == true) {
                            errorString = ipValidators.extractErrorMessage(ip).orEmpty()
                        }
                    },
                    errorDescription = errorString
                )
            )

            Form(
                fields = formData.toImmutableList(),
                modifier = Modifier.fillMaxWidth()
            ) { data ->
                LeopardTextField(
                    data = data,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            OrDivider()
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = {
                    navigateToQrCode()
                },
                border = BorderStroke(1.dp, primaryColor),
                modifier = Modifier
                    .heightIn(min = 56.dp)
                    .fillMaxWidth()
                    .background(white),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = white,
                    contentColor = primaryColor
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    painter = painterResource("ic_qr_code.xml"),
                    contentDescription = "qr code"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = localization(scan_qr_code).value,
                    style = MaterialTheme.typography.button,
                    color = primaryColor
                )
            }

        }
        LeopardLoadingButton(
            onClick = {
                keyboardController?.hide()
                errorString = ipValidators.extractErrorMessage(ip).orEmpty()
                if (errorString.isNullOrEmpty()) {
                    coroutineScope.launch {
                        onNextButtonClick(ip)
                    }
                }
            },
            isLoading = buttonLoading,
            isEnabled = errorString.isNullOrBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .bringIntoViewRequester(relocationRequester),
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(
                        text = localization(next).value,
                        style = MaterialTheme.typography.button,
                        color = white
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = "Next",
                        tint = white
                    )
                }
            }

        }

        Text(
            "©Copyrights 2024 Patris company. All rights reserved",
            color = secondary,
            fontSize = 10.sp,
            modifier = Modifier.clickable {
                openUrl("http://www.patrisco.com")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

    }
}