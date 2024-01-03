package com.attendace.leopard.presentation.screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.borderColor
import com.attendace.leopard.util.theme.onSurface
import com.attendace.leopard.util.theme.secondaryColor

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LeopardTextField(
    data: FormTextFieldData,
    modifier: Modifier = Modifier
) {
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }
    var value by rememberSaveable(data.value) {
        mutableStateOf(data.value)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Column(modifier = modifier) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(data.focusRequester)
                    .padding(vertical = 2.dp),
                label = {
                    Text(data.label, style = MaterialTheme.typography.caption)
                },
                isError = !data.errorDescription.isNullOrBlank(),
                leadingIcon = data.leadingIcon,
                visualTransformation = if (data.isPassword && !isPasswordVisible) PasswordVisualTransformation() else VisualTransformation.None,
                trailingIcon = {
                    if (data.isPassword) {
                        val description =
                            if (isPasswordVisible) "Hide password" else "Show password"

                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                if (isPasswordVisible)
                                    Icons.Filled.Visibility
                                else
                                    Icons.Filled.VisibilityOff,
                                description
                            )
                        }
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = secondaryColor,
                    focusedLabelColor = secondaryColor,
                    textColor = onSurface,
                    unfocusedBorderColor = borderColor,
                ),
                keyboardOptions = data.keyboardOptions,
                singleLine = data.singleLine,
                value = value,
                onValueChange = {
                    if (data.singleLine && it.contains("\n"))
                        return@OutlinedTextField
                    value = it
                    data.onValueChange(it)

                },
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        data.onKeyboardDoneClicked(value)
                    },
                    onNext = {
                        data.onKeyboardNextClicked()
                    }
                ),
                textStyle = MaterialTheme.typography.subtitle1.copy(textAlign = TextAlign.Left),
                shape = RoundedCornerShape(8.dp)
            )

            if (!data.errorDescription.isNullOrEmpty()) {
                Text(
                    text = data.errorDescription,
                    color = colors.error,
                    style = MaterialTheme.typography.caption,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}