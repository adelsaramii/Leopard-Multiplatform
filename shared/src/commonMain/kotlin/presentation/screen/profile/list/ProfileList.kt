package com.attendace.leopard.presentation.screen.profile.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.attendace.leopard.presentation.screen.profile.list.components.ChangePassword
import com.attendace.leopard.presentation.screen.profile.list.components.ProfileLanguage

@Composable
fun ProfileList(
    modifier: Modifier = Modifier,
    onLanguageClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    selectedLanguage: String
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
        ) {
            ProfileLanguage(onLanguageClick = onLanguageClick, selectedLanguage = selectedLanguage)
            ChangePassword(onChangePasswordClick = onChangePasswordClick)
        }
    }
}