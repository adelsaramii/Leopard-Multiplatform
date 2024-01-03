package com.attendace.leopard.presentation.screen.components.drawer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.attendace.leopard.data.base.Failed
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.data.base.Loading
import com.attendace.leopard.data.base.NotLoaded
import com.attendace.leopard.util.theme.gray
import com.attendace.leopard.util.theme.neutralLight5Dark5
import com.attendace.leopard.util.theme.primaryColor
import com.attendance.leopard.data.model.User
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.client.request.header
import kotlinx.coroutines.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun DrawerHeader(
    user: LoadableData<User>,
    baseUrl: String,
    accessToken: String,
    modifier: Modifier = Modifier,
) {
    when (user) {
        is Failed -> {
            //todo
        }

        is Loaded -> {
            DrawerHeaderContent(
                user = user.data, baseUrl = baseUrl, accessToken = accessToken, modifier = modifier
            )
        }

        Loading -> {
            ShimmerDrawerHeader()
        }

        NotLoaded -> {
            ShimmerDrawerHeader()
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun DrawerHeaderContent(
    user: User, baseUrl: String, accessToken: String, modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        val painterResource = asyncPainterResource("$baseUrl/NFS.web/Default/Module.Mobile/api/ProfilePictureApi?personId=${user.id}") {

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
                .size(56.dp)
                .clip(CircleShape),
            onLoading = { progress -> CircularProgressIndicator(progress) },
            onFailure = {
                painterResource("user.xml")
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = user.fullName,
            style = MaterialTheme.typography.body1,
            color = primaryColor,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = user.roleName,
            style = MaterialTheme.typography.caption,
            color = neutralLight5Dark5,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(32.dp))
        Divider(color = gray)
    }
}