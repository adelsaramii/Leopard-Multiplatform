package com.attendace.leopard.presentation.screen.home.monthly

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.base.NotLoaded
import com.attendace.leopard.presentation.screen.components.localization
import com.attendace.leopard.util.theme.neutralLight0Dark10
import com.attendace.leopard.util.theme.white
import com.attendance.leopard.data.model.User
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.client.request.header
import kotlinx.coroutines.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    title: String,
    baseUrl: String = "",
    accessToken: String = "",
    hasRefresh: Boolean = false,
    user: LoadableData<User?> = NotLoaded,
    onSubordinateClick: () -> Unit,
    navigationIconId: String = "ic_bars.xml",
    trailingIconId: String = "ic_redo.xml",
    onNavigationIconClick: () -> Unit = {},
    onTrailingIconClick: () -> Unit = {},
) {
    if (user.data?.hasSubordinate == true) {   //todo enhance animation
        Row(
            modifier = modifier
                .height(56.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            IconButton(onClick = { onNavigationIconClick() }) {
                Image(
                    painter = painterResource(navigationIconId),
                    modifier = Modifier.padding(8.dp).localization(),
                    alignment = Alignment.Center,
                    contentDescription = "Menu",
                    colorFilter = ColorFilter.tint(white)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.body1,
                    color = neutralLight0Dark10
                )
                user.data?.let {
                    SubordinateView(
                        user = it,
                        onClick = onSubordinateClick,
                        baseUrl = baseUrl,
                        accessToken = accessToken,
                    )
                }
            }

        }
    } else {
        Box(
            modifier = modifier
                .height(56.dp)
                .fillMaxWidth(),
        ) {

            IconButton(onClick = { onNavigationIconClick() }) {
                Image(
                    painter = painterResource(navigationIconId),
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.CenterStart)
                        .localization(),
                    alignment = Alignment.Center,
                    contentDescription = "Menu",
                    colorFilter = ColorFilter.tint(white)
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.body1,
                color = neutralLight0Dark10,
                modifier = Modifier.align(Alignment.Center)
            )
            if (hasRefresh) {
                IconButton(modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = { onTrailingIconClick() }) {
                    Image(
                        painter = painterResource(trailingIconId),
                        modifier = Modifier.padding(8.dp),
                        alignment = Alignment.Center,
                        contentDescription = painterResource(trailingIconId).toString(),
                        colorFilter = ColorFilter.tint(white)
                    )
                }
            }
        }
    }

}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SubordinateView(
    user: User,
    baseUrl: String,
    accessToken: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(end = 8.dp)
            .clickable { onClick() },
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .border(width = 2.dp, color = white, shape = CircleShape),
        ) {
            val painterResource = asyncPainterResource("$baseUrl/NFS.web/Default/Module.Mobile/api/ProfilePictureApi/GetProfilePicture?personId=${user.id}") {

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
                    .size(32.dp)
                    .clip(CircleShape),
                onLoading = { progress -> CircularProgressIndicator(progress) },
                onFailure = {
                    painterResource("user.xml")
                }
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Text(
                text = user.fullName, style = MaterialTheme.typography.body1, color = white,
            )
            if (user.roleName.isNotEmpty()) {
                Text(
                    text = user.roleName,
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.Normal,
                    color = white
                )
            }
        }
        Image(
            painter = painterResource("angle_down.xml"),
            contentDescription = "ExpandMore",
            colorFilter = ColorFilter.tint(
                white
            )
        )

    }
}