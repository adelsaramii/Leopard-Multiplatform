package com.attendace.leopard.presentation.screen.profile.list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.attendace.leopard.data.base.Failed
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.data.base.Loading
import com.attendace.leopard.presentation.screen.components.LeopardLoadingButton
import com.attendace.leopard.presentation.screen.components.Form
import com.attendace.leopard.presentation.screen.components.FormTextFieldData
import com.attendace.leopard.presentation.screen.components.LeopardTextField
import com.attendace.leopard.util.helper.Validator
import com.attendace.leopard.util.helper.extractErrorMessage
import com.attendace.leopard.util.theme.changePasswordText
import com.attendace.leopard.util.theme.confirm
import com.attendace.leopard.util.theme.currentPassword
import com.attendace.leopard.util.theme.localization
import com.attendace.leopard.util.theme.neutralLight0Dark10
import com.attendace.leopard.util.theme.neutralLight10Dark0
import com.attendace.leopard.util.theme.newPasswordText
import com.attendace.leopard.util.theme.oldPasswordIncorrect
import com.attendace.leopard.util.theme.onSurface
import com.attendace.leopard.util.theme.required_message
import com.attendace.leopard.util.theme.splashGradiantEndColor
import com.attendace.leopard.util.theme.textColor
import com.attendace.leopard.util.theme.white
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalComposeUiApi::class, ExperimentalResourceApi::class)
@Composable
fun ChangePasswordBottomSheet(
    changePassword: LoadableData<Unit>,
    onChangePasswordButtonClick: (String, String) -> Unit,
    onCloseClick: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember {
        SnackbarHostState()
    }

    LaunchedEffect(changePassword) {
        when (changePassword) {
            is Failed -> {
                snackBarHostState.showSnackbar(
                    localization(oldPasswordIncorrect).value
                )
            }

            is Loaded -> {
                onCloseClick()
            }

            else -> {}
        }
    }

    var oldPassword by remember {
        mutableStateOf("")
    }
    var newPassword by remember {
        mutableStateOf("")
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    var oldPasswordFieldErrorString: String? by remember {
        mutableStateOf(null)
    }
    var newPasswordFieldErrorString: String? by remember {
        mutableStateOf(null)
    }
    var oldPasswordVisible = false
    var newPasswordVisible = false
    val oldPasswordValidators =
        listOf(Validator.NotEmpty(message = localization(required_message).value))
    val newPasswordValidators =
        listOf(Validator.NotEmpty(message = localization(required_message).value))

    Scaffold(
        modifier = Modifier.height(300.dp),
        contentColor = white,
        containerColor = white,
        snackbarHost = {
            SnackbarHost(snackBarHostState) { data ->
                Snackbar(
                    containerColor = neutralLight0Dark10,
                    contentColor = neutralLight10Dark0,
                    snackbarData = data
                )
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier.background(shape = RoundedCornerShape(16.dp), color = white)
        ) {
            Column(
                modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = localization(changePasswordText).value,
                        style = MaterialTheme.typography.subtitle2,
                        color = onSurface
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    Form(
                        fields = listOf(
                            FormTextFieldData(
                                label = localization(currentPassword).value,
                                value = oldPassword,
                                scope = coroutineScope,
                                leadingIcon = {
                                    Image(
                                        painter = painterResource("ic_lock.xml"),
                                        "Person Icon",
                                        colorFilter = ColorFilter.tint(textColor)
                                    )
                                },
                                onKeyboardNextClicked = {
                                    focusRequester.requestFocus()
                                },
                                singleLine = true,
                                onKeyboardDoneClicked = {},
                                keyboardController = keyboardController,
                                focusManager = focusManager,
                                isPassword = true,
                                onValueChange = {
                                    oldPassword = it
                                    if (oldPasswordFieldErrorString?.isNotEmpty() == true) {
                                        oldPasswordFieldErrorString =
                                            oldPasswordValidators.extractErrorMessage(oldPassword)
                                                .orEmpty()
                                    }
                                },
                                imeAction = ImeAction.Next,
                                onTrailingIconClick = {
                                    oldPasswordVisible = !oldPasswordVisible
                                },
                                isPasswordVisible = oldPasswordVisible,
                                errorDescription = oldPasswordFieldErrorString,
                                keyboardOptions = KeyboardOptions(
                                    imeAction = ImeAction.Next
                                ),
                            ),
                            FormTextFieldData(
                                label = localization(newPasswordText).value,
                                value = newPassword,
                                scope = coroutineScope,
                                singleLine = true,
                                leadingIcon = {
                                    Image(
                                        painter = painterResource("ic_change_password.xml"),
                                        "Lock Icon"
                                    )
                                },
                                onKeyboardDoneClicked = {
                                    newPassword = it
                                    if (newPasswordFieldErrorString?.isNotEmpty() == true) {
                                        oldPasswordFieldErrorString =
                                            newPasswordValidators.extractErrorMessage(newPassword)
                                                .orEmpty()
                                    }
                                },
                                focusRequester = focusRequester,
                                isPassword = true,
                                keyboardController = keyboardController,
                                focusManager = focusManager,
                                onValueChange = {
                                    newPassword = it
                                    if (newPasswordFieldErrorString?.isNotEmpty() == true) {
                                        newPasswordFieldErrorString =
                                            newPasswordValidators.extractErrorMessage(newPassword)
                                                .orEmpty()
                                    }
                                },
                                onTrailingIconClick = {
                                    newPasswordVisible = !newPasswordVisible
                                },
                                isPasswordVisible = newPasswordVisible,
                                errorDescription = newPasswordFieldErrorString
                            )
                        ).toImmutableList(),
                        modifier = Modifier.fillMaxWidth()
                    ) { data ->
                        LeopardTextField(
                            data,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                    LeopardLoadingButton(
                        onClick = {
                            keyboardController?.hide()
                            oldPasswordFieldErrorString =
                                oldPasswordValidators.extractErrorMessage(oldPassword).orEmpty()
                            newPasswordFieldErrorString =
                                newPasswordValidators.extractErrorMessage(newPassword).orEmpty()
                            if (
                                oldPasswordFieldErrorString.isNullOrEmpty() && newPasswordFieldErrorString.isNullOrEmpty()
                            ) {
                                onChangePasswordButtonClick(oldPassword, newPassword)
                            }
                        },
                        isLoading = when (changePassword) {
                            Loading -> true
                            else -> false
                        },
                        colors = ButtonDefaults.buttonColors(backgroundColor = splashGradiantEndColor),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    localization(confirm).value,
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
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }


}