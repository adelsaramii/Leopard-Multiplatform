package com.attendace.leopard.presentation.screen.components.calendar.header

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.attendace.leopard.presentation.screen.components.localization
import com.attendace.leopard.util.theme.unavailableDayColor
import com.attendace.leopard.util.theme.white
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CalendarHeaderArrowIconButton(
    iconId: String, onClick: () -> Unit, enabled: Boolean, modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .size(36.dp)
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = if (enabled) rememberRipple(bounded = true) else null,
        ) {
            if (enabled) onClick()
        }.localization()) {
        Icon(
            painter = painterResource(iconId),
            contentDescription = "arrow",
            tint = if (enabled) white else unavailableDayColor,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}