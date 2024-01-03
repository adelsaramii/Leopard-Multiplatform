package com.attendace.leopard.presentation.screen.portfolio.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.*
import com.attendace.leopard.data.model.Portfolio
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.client.request.header
import kotlinx.coroutines.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun UserHeader(
    user: Portfolio,
    baseUrl: String,
    accessToken: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color? = null
) {
    Row(
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                backgroundColor ?: MaterialTheme.colors.surface
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            contentAlignment = Alignment.Center, modifier = Modifier.size(44.dp),
        ) {
            val painterResource = asyncPainterResource("$baseUrl/NFS.web${user.personnelImageUrl}") {

                // CoroutineContext to be used while loading the image.
                coroutineContext = Job() + Dispatchers.IO

                // Customizes HTTP request
                requestBuilder { // this: HttpRequestBuilder
                    header("Authorization", "bearer $accessToken")
                }

            }
            KamelImage(
                resource = painterResource,
                contentDescription = "Profile",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                onLoading = { progress -> CircularProgressIndicator(progress) },
                onFailure = {
                    painterResource("user.xml")
                }
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = user.personnelName,
                style = MaterialTheme.typography.body1,
                color = black,
                modifier = Modifier.padding(start = 8.dp)
            )
            Text(
                text = user.personnelPosition,
                style = MaterialTheme.typography.caption,
                color = gray3,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}