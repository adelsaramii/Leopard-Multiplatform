package com.attendace.leopard.presentation.screen.portfolio.list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.attendace.leopard.presentation.screen.components.LeopardLoadingButton
import com.attendace.leopard.util.theme.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PortfolioConfirmation(
    modifier: Modifier = Modifier,
    confirmLoading: Boolean,
    rejectLoading: Boolean,
    onConfirmClick: () -> Unit,
    onRejectClick: () -> Unit,
) {
    Column(modifier = modifier) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(4.dp)
                .shadow(2.dp)
        )
        Row(
            modifier = Modifier
                .background(white, RectangleShape)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            LeopardLoadingButton(
                onClick = { onConfirmClick() },
                isLoading = confirmLoading,
                colors = ButtonDefaults.buttonColors(backgroundColor = primaryColor),
                modifier = Modifier
                    .padding(16.dp)
                    .height(48.dp)
                    .weight(1f),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource("ic_confirm.xml"),
                        contentDescription = "confirm"
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    Text(
                        text = localization(confirm).value,
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Bold,
                        color = white
                    )
                }

            }
            LeopardLoadingButton(
                isLoading = rejectLoading,
                onClick = { onRejectClick() },
                modifier = Modifier
                    .padding(16.dp)
                    .height(48.dp)
                    .weight(1f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource("ic_reject.xml"),
                        contentDescription = "reject",
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                    Text(
                        text = localization(reject).value,
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Bold,
                        color = white
                    )
                }

            }
        }
    }
}