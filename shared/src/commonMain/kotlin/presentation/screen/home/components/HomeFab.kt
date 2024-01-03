package com.attendace.leopard.presentation.screen.home.components

import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeFab(scaffoldState: BottomSheetScaffoldState) {
    val scope = rememberCoroutineScope()
    FloatingActionButton(
        onClick = {
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(
                    "Snackbar", actionLabel = "Action"
                )
            }
        }
    ) {
        Text("FAB")
    }
}
