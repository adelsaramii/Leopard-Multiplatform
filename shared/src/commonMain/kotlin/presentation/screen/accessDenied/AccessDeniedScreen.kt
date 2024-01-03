package com.attendace.leopard.presentation.screen.accessDenied

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.*
import com.attendace.leopard.presentation.screen.home.monthly.AppBar
import com.attendace.leopard.presentation.screen.components.portfolioGradientBackground
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AccessDeniedScreen(openMenuDrawer: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .portfolioGradientBackground()
    ) {
        AppBar(
            title = localization(accessDenied).value,
            onSubordinateClick = {},
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 8.dp),
            onNavigationIconClick = {
                openMenuDrawer()
            },
            hasRefresh = false,
        )

        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                Modifier.align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource("ic_error_page.xml"),
                    contentDescription = "opened box",
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    modifier = Modifier.requiredWidth(200.dp),
                    text = localization(accessDeniedMessage).value,
                    style = MaterialTheme.typography.body2,
                    color = neutralLight0Dark10,
                    fontWeight = FontWeight.W500,
                    textAlign = TextAlign.Center
                )
            }

        }

    }

}