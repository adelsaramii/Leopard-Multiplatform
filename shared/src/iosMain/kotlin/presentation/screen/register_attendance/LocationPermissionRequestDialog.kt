package com.attendace.leopard.presentation.screen.register_attendance

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.attendace.leopard.presentation.screen.components.LeopardLoadingButton
import com.attendace.leopard.util.theme.gray
import com.attendace.leopard.util.theme.white
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun LocationPermissionRequestDialog(
    textToShow: String,
    onLaunchPermissionClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = modifier
                .background(color = white, shape = RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(42.dp),
                painter = painterResource("ic_location.xml"),
                colorFilter = ColorFilter.tint(
                    gray
                ),
                contentDescription = "location"
            )
            Text(textToShow)
            LeopardLoadingButton(
                modifier = Modifier.padding(8.dp),
                onClick = onLaunchPermissionClick,
            ) {
                Text(
                    text = "Request permission", //todo
                    style = TextStyle(color = white)
                )
            }
        }
    }
}