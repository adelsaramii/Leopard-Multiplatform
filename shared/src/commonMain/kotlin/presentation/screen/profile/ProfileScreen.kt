package com.attendace.leopard.presentation.screen.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.attendace.leopard.presentation.screen.auth.util.locale.LocaleBottomSheet
import com.attendace.leopard.presentation.screen.profile.list.ProfileListScreen
import com.attendace.leopard.presentation.screen.profile.list.components.ChangePasswordBottomSheet
import com.attendace.leopard.presentation.viewmodel.util.state
import com.attendace.leopard.presentation.viewmodel.ProfileViewModel
import com.attendace.leopard.util.theme.language
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    openMenuDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = koinInject()
) {
    val state by viewModel.state()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = remember {
        mutableStateOf(true)
    }
    val bottomSheetSwitch = remember {
        mutableStateOf(true)
    }

    LaunchedEffect(scaffoldState.bottomSheetState.targetValue) {
        if (scaffoldState.bottomSheetState.targetValue == SheetValue.Expanded) {
            bottomSheetState.value = true
        } else if (scaffoldState.bottomSheetState.targetValue == SheetValue.PartiallyExpanded) {
            bottomSheetState.value = false
        }
    }

    BottomSheetScaffold(
        sheetContent = {
            if (bottomSheetSwitch.value) {
                LocaleBottomSheet(
                    modifier = modifier.heightIn(min = 100.dp, max = 300.dp),
                    currentLanguage = state.currentLanguageTag,
                    onCloseClick = {
                        coroutineScope.launch {
                            scaffoldState.bottomSheetState.partialExpand()
                        }
                    },
                    onClick = {
                        coroutineScope.launch {
                            scaffoldState.bottomSheetState.partialExpand()
                        }
                        viewModel.setLocale(it.language, it.name)
                        language.value = it.language
                    },
                )
            } else {
                ChangePasswordBottomSheet(
                    changePassword = state.changePassword,
                    onChangePasswordButtonClick = { oldPassword, newPassword ->
                        viewModel.changePassword(oldPassword, newPassword)
                    },
                    onCloseClick = {
                        coroutineScope.launch {
                            scaffoldState.bottomSheetState.partialExpand()
                        }
                        viewModel.resetChangePasswordState()
                    },
                )
            }
        },
        sheetContainerColor = Color.White,
        sheetContentColor = Color.White,
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp
    ) {
        ProfileListScreen(
            navigateToLanguage = {
                coroutineScope.launch {
                    bottomSheetSwitch.value = true
                    scaffoldState.bottomSheetState.expand()
                }
            },
            navigateToChangePassword = {
                coroutineScope.launch {
                    bottomSheetSwitch.value = false
                    scaffoldState.bottomSheetState.expand()
                }
            },
            openMenuDrawer = openMenuDrawer,
            selectedLanguage = language.value.name,
            state = state,
        )

        if (bottomSheetState.value) {
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
