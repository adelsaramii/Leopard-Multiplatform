package com.attendace.leopard.presentation.screen.profile.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import com.attendace.leopard.presentation.screen.home.monthly.AppBar
import com.attendace.leopard.presentation.screen.profile.list.components.ShimmerProfile
import com.attendace.leopard.presentation.viewmodel.ProfileViewModel
import com.attendace.leopard.util.theme.*
import com.attendace.leopard.presentation.screen.components.portfolioGradientBackground
import com.attendance.leopard.data.model.User
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.client.request.header
import kotlinx.coroutines.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun ProfileListScreen(
    modifier: Modifier = Modifier,
    navigateToLanguage: () -> Unit,
    navigateToChangePassword: () -> Unit,
    openMenuDrawer: () -> Unit,
    selectedLanguage: String,
    state: ProfileViewModel.State,
) {
    ProfileListScreenContent(
        modifier = modifier,
        openMenuDrawer = openMenuDrawer,
        navigateToLanguage = navigateToLanguage,
        navigateToChangePassword = navigateToChangePassword,
        selectedLanguage = selectedLanguage,
        user = state.userInfo,
        baseUrl = state.baseUrl,
        accessToken = state.accessToken,
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ProfileListScreenContent(
    baseUrl: String,
    accessToken: String,
    modifier: Modifier = Modifier,
    selectedLanguage: String,
    navigateToLanguage: () -> Unit,
    navigateToChangePassword: () -> Unit,
    openMenuDrawer: () -> Unit,
    user: LoadableData<User>,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .portfolioGradientBackground()
    ) {
        AppBar(
            title = localization(myProfile).value,
            onSubordinateClick = {},
            onNavigationIconClick = { openMenuDrawer() },
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 8.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        when (user) {
            is Loaded -> {
                val userData = user.data
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .border(width = 4.dp, color = white, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    val painterResource =
                        asyncPainterResource("$baseUrl/NFS.web/Default/Module.Mobile/api/ProfilePictureApi?personId=${userData.id}") {

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
                            .size(72.dp)
                            .clip(CircleShape),
                        onLoading = { progress -> CircularProgressIndicator(progress) },
                        onFailure = {
                            painterResource("user.xml")
                        }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = userData.fullName,
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.W500,
                    color = white,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${userData.roleName} | ${userData.code}",
                    style = MaterialTheme.typography.caption,
                    fontWeight = FontWeight.W500,
                    color = white,
                )
                Spacer(modifier = Modifier.height(32.dp))
            }

            Loading -> {
                ShimmerProfile()
            }

            is Failed -> {}
            NotLoaded -> {}
        }
        Column(
            modifier = Modifier.background(
                white, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            )
        ) {
            ProfileList(
                onLanguageClick = navigateToLanguage,
                onChangePasswordClick = navigateToChangePassword,
                selectedLanguage = selectedLanguage,
            )
        }
    }
}