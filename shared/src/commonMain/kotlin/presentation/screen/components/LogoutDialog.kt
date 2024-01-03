package com.attendace.leopard.presentation.screen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.accept
import com.attendace.leopard.util.theme.cancel
import com.attendace.leopard.util.theme.localization
import com.attendace.leopard.util.theme.logout
import com.attendace.leopard.util.theme.logoutSubtitle
import com.attendace.leopard.util.theme.primary
import com.attendace.leopard.util.theme.textColor
import com.attendace.leopard.util.theme.white
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun LogoutDialog(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit,
    onAcceptClick: () -> Unit,
    onCancelClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .background(
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp), color = white
            ),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = localization(logout).value,
                style = MaterialTheme.typography.subtitle1,
                color = textColor,
                fontWeight = FontWeight.Medium,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = localization(logoutSubtitle).value,
                style = MaterialTheme.typography.subtitle1,
                color = textColor,
                fontWeight = FontWeight.Medium,
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(bottom = 16.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            LeopardLoadingButton(
                onClick = {
                    onAcceptClick()
                },
                colors = leopardButtonColors(backgroundColor = primary),
                modifier = Modifier
                    .height(46.dp)
                    .weight(0.4f)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(8.dp))
            ) {
                Text(
                    text = localization(accept).value,
                    color = white,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold,
                )
            }
            OutlinedButton(
                onClick = {
                    onCancelClick()
                },
                modifier = Modifier
                    .height(46.dp)
                    .weight(0.4f)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = white,
                    contentColor = primary,
                ),
                border = BorderStroke(2.dp, primary)
            ) {
                Text(
                    text = localization(cancel).value,
                    color = primary,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
