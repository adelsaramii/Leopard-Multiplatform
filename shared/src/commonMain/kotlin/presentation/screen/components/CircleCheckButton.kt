package com.attendace.leopard.presentation.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.primary
import com.attendace.leopard.util.theme.white
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CircleCheckIconToggleButton(
    modifier: Modifier = Modifier,
    isCheck: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {}
) {
//    var isChecked by rememberSaveable(isCheck) {
//        mutableStateOf(isCheck)
//    }
    IconToggleButton(checked = isCheck, onCheckedChange = {
//        isChecked = it
        onCheckedChange(it)
    }) {
        if (isCheck) {
            Box(
                modifier = modifier
                    .background(color = primary, shape = CircleShape)
                    .size(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource("ic_check_button.xml"),
                    contentDescription = "Checked",
                    tint = white
                )
            }
        } else {
            Box(
                modifier = modifier
                    .background(color = white, shape = CircleShape)
                    .size(24.dp)
                    .border(2.dp, primary, CircleShape)
            )
        }
    }
}
