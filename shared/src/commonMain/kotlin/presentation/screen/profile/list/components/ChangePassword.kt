package com.attendace.leopard.presentation.screen.profile.list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.attendace.leopard.presentation.screen.components.localization
import com.attendace.leopard.util.theme.changePasswordText
import com.attendace.leopard.util.theme.gray2
import com.attendace.leopard.util.theme.localization
import com.attendace.leopard.util.theme.textColor
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ChangePassword(
    modifier: Modifier = Modifier,
    onChangePasswordClick: () -> Unit
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 16.dp , end = 16.dp , top = 16.dp)
            .background(shape = RoundedCornerShape(8.dp), color = gray2)
            .clickable { onChangePasswordClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource("ic_change_password.xml"),
                contentDescription = "language",
            )
            Text(
                text = localization(changePasswordText).value,
                style = MaterialTheme.typography.caption,
                color = textColor,
                fontWeight = FontWeight.W500
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource("angle_right.xml"),
                contentDescription = "angle right",
                colorFilter = ColorFilter.tint(color = textColor),
                modifier = Modifier.localization()
            )
        }
    }
}