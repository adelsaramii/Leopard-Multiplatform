package com.attendace.leopard.presentation.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.platform.appVersionCode
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.koin.compose.rememberKoinInject


@OptIn(ExperimentalResourceApi::class)
@Composable
fun SplashScreen() {

    Box {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource("splash_bg.png"),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource("leopard-logo.xml"),
                contentDescription = null,
                modifier = Modifier
                    .width(240.dp)
                    .fillMaxHeight(0.8f)

            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(30.dp),
                    color = Color.White,
                    strokeWidth = 1.dp
                )
                Text(
                    text = "Patris Company",
                    style = MaterialTheme.typography.subtitle1,
                    color = Color.White
                )
                Text(
                    modifier = Modifier.padding(bottom = 48.dp, top = 8.dp),
                    text = "V${appVersionCode}",
                    style = MaterialTheme.typography.subtitle1,
                    color = Color.White
                )
            }

        }
    }

}