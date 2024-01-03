package com.attendace.leopard.presentation.screen.auth.util

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.attendace.leopard.util.theme.localization
import com.attendace.leopard.util.theme.login
import com.attendace.leopard.util.theme.primaryColor
import com.attendace.leopard.util.theme.required_message
import com.attendace.leopard.util.theme.splashGradiantEndColor
import com.attendace.leopard.util.theme.textColor
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
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class, ExperimentalResourceApi::class
)
@Composable
fun UsernamePasswordPage(
    buttonLoading: Boolean,
    onLoginButtonClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {

    val focusRequester = remember { FocusRequester() }

    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val userNameValidators =
        listOf(Validator.NotEmpty(message = localization(required_message).value))
    val passwordValidators =
        listOf(Validator.NotEmpty(message = localization(required_message).value))
    var username by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    var usernameFieldErrorString: String? by remember {
        mutableStateOf(null)
    }
    var passwordFieldErrorString: String? by remember {
        mutableStateOf(null)
    }
    var isPasswordVisible = false


    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        val fieldsData = listOf(
            FormTextFieldData(
                label = localization(com.attendace.leopard.util.theme.username).value,
                value = username,
                scope = coroutineScope,
                leadingIcon = {
                    Image(
                        painter = painterResource("user.xml"),
                        "Person Icon",
                        colorFilter = ColorFilter.tint(textColor)
                    )
                },
                onKeyboardNextClicked = {
                    focusRequester.requestFocus()
                },
                onKeyboardDoneClicked = {},
                keyboardController = keyboardController,
                focusManager = focusManager,
                onValueChange = {
                    username = it
                    if (usernameFieldErrorString?.isNotEmpty() == true) {
                        usernameFieldErrorString =
                            userNameValidators.extractErrorMessage(username).orEmpty()
                    }
                },
                imeAction = ImeAction.Next,
                errorDescription = usernameFieldErrorString,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
            ),
            FormTextFieldData(
                label = localization(com.attendace.leopard.util.theme.password).value,
                value = password,
                scope = coroutineScope,
                singleLine = true,
                leadingIcon = {
                    Image(
                        painter = painterResource("ic_lock.xml"),
                        "Lock Icon"
                    )
                },
                onKeyboardDoneClicked = {
                    password = it
                    if (passwordFieldErrorString?.isNotEmpty() == true) {
                        passwordFieldErrorString =
                            passwordValidators.extractErrorMessage(password).orEmpty()
                    }
                },
                focusRequester = focusRequester,
                isPassword = true,
                keyboardController = keyboardController,
                focusManager = focusManager,
                onValueChange = {
                    password = it
                    if (passwordFieldErrorString?.isNotEmpty() == true) {
                        passwordFieldErrorString =
                            passwordValidators.extractErrorMessage(password).orEmpty()
                    }
                },
                onTrailingIconClick = {
                    isPasswordVisible = !isPasswordVisible
                },
                isPasswordVisible = isPasswordVisible,
                errorDescription = passwordFieldErrorString
            )
        )
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                localization(login).value,
                style = MaterialTheme.typography.h6,
                color = primaryColor
            )
            Spacer(modifier = Modifier.height(16.dp))

            Form(
                fields = fieldsData.toImmutableList(),
                modifier = Modifier.fillMaxWidth()
            ) { data ->
                LeopardTextField(
                    data,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Spacer(modifier = Modifier.height(4.dp))
        }
        LeopardLoadingButton(
            onClick = {
                keyboardController?.hide()
                usernameFieldErrorString =
                    userNameValidators.extractErrorMessage(username).orEmpty()
                passwordFieldErrorString =
                    passwordValidators.extractErrorMessage(password).orEmpty()
                if (
                    usernameFieldErrorString.isNullOrEmpty() && passwordFieldErrorString.isNullOrEmpty()
                ) {
                    onLoginButtonClick(username, password)
                }
            },
            isLoading = buttonLoading,
            colors = ButtonDefaults.buttonColors(backgroundColor = splashGradiantEndColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        localization(login).value,
                        style = MaterialTheme.typography.button,
                        color = white
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource("arrow_right.xml"),
                        contentDescription = "Login",
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