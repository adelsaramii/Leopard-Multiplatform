package com.attendace.leopard.presentation.screen.personnel_status_report.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.attendace.leopard.presentation.screen.components.NeverOverScroll
import com.attendace.leopard.data.base.PageFailed
import com.attendace.leopard.data.base.PageLoading
import com.attendace.leopard.data.model.PersonnelReportStatusModel
import com.attendace.leopard.data.model.PersonnelReportStatusTypeEnum
import com.attendace.leopard.presentation.screen.components.NoDataPage
import com.attendace.leopard.util.theme.*
import com.attendace.leopard.presentation.screen.portfolio.list.components.ShimmerPortfolioItem
import com.attendace.leopard.util.date.DayStructure
import com.attendace.leopard.presentation.screen.components.ErrorPage
import com.attendace.leopard.util.date.toDateHumanReadable
import com.attendace.leopard.presentation.screen.components.refresh.PullRefreshIndicator
import com.attendace.leopard.presentation.screen.components.refresh.PullRefreshState
import com.attendace.leopard.presentation.screen.components.refresh.pullRefresh
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.client.request.header
import kotlinx.coroutines.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PersonnelReportStatusList(
    listState: LazyListState,
    paginationData: com.attendace.leopard.data.base.PaginationLoadableData<List<PersonnelReportStatusModel>>,
    baseUrl: String,
    accessToken: String,
    modifier: Modifier = Modifier,
    pullRefreshState: PullRefreshState,
    isRefreshing: Boolean,
    retry: () -> Unit
) {

    val data = paginationData.data ?: emptyList()
    when (paginationData) {
        is com.attendace.leopard.data.base.PageInitialFailed -> {
            ErrorPage(
                modifier = Modifier.fillMaxSize(),
                description = paginationData.throwable.message.toString()
            ) {
                retry()
            }
        }

        is com.attendace.leopard.data.base.PageInitialLoad, is com.attendace.leopard.data.base.PageLoad, is com.attendace.leopard.data.base.PageLoading, is com.attendace.leopard.data.base.PageFailed -> {
            Box(
                modifier = modifier
                    .pullRefresh(pullRefreshState)
                    .clipToBounds()
            ) {
                if (data.isEmpty()) {
                    LazyColumn(
                        modifier = modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            NoDataPage()
                        }
                    }
                    PullRefreshIndicator(
                        isRefreshing, pullRefreshState, Modifier.align(Alignment.TopCenter)
                    )
                } else {
                    NeverOverScroll {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            state = listState,
                            modifier = modifier
                                .padding(top = 8.dp)
                                .fillMaxSize(),
                        ) {
                            items(
                                data,
                            ) {
                                if (it.isHeader == true) {
                                    Column {
                                        Text(
                                            modifier = Modifier.padding(top = 12.dp, start = 16.dp),
                                            style = MaterialTheme.typography.subtitle1,
                                            color = neutralLight9Dark1,
                                            text = "${
                                                when (it.status) {
                                                    PersonnelReportStatusTypeEnum.Absents.name -> {
                                                        localization(absents).value
                                                    }

                                                    PersonnelReportStatusTypeEnum.Overtime.name -> {
                                                        localization(overtime).value
                                                    }

                                                    PersonnelReportStatusTypeEnum.Rest.name -> {
                                                        localization(rest).value
                                                    }

                                                    PersonnelReportStatusTypeEnum.Presents.name -> {
                                                        localization(presents).value
                                                    }

                                                    else -> {
                                                        ""
                                                    }
                                                }
                                            } (${it.totalCount})"
                                        )
                                        PersonnelReportStatusItem(
                                            user = it,
                                            baseUrl = baseUrl,
                                            accessToken = accessToken
                                        )
                                    }
                                } else {
                                    PersonnelReportStatusItem(
                                        user = it,
                                        baseUrl = baseUrl,
                                        accessToken = accessToken
                                    )
                                }
                            }
                            item {
                                if (paginationData is PageLoading) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.align(
                                                Alignment.Center
                                            )
                                        )
                                    }
                                } else if (paginationData is PageFailed) {
                                    Spacer(modifier = Modifier.height(16.dp))
                                    ErrorPage(
                                        modifier = Modifier.fillMaxWidth(),
                                        description = paginationData.throwable.message.toString()
                                    ) {
                                        retry()
                                    }
                                }
                            }
                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                        PullRefreshIndicator(
                            isRefreshing, pullRefreshState, Modifier.align(Alignment.TopCenter)
                        )
                    }
                }
            }
        }

        is com.attendace.leopard.data.base.PageInitialLoading -> {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                repeat(9, action = {
                    ShimmerPortfolioItem()
                })
            }
        }

        is com.attendace.leopard.data.base.PageInitialNotLoaded -> {}
    }

}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PersonnelReportStatusItem(
    user: PersonnelReportStatusModel,
    baseUrl: String,
    accessToken: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(start = 16.dp, end = 16.dp, top = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .wrapContentHeight()
            .background(gray2)
            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            val painterResource = asyncPainterResource("$baseUrl/NFS.web${user.imageUrl}") {

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
                    .clip(CircleShape)
                    .border(
                        2.5.dp, when (user.status) {
                            PersonnelReportStatusTypeEnum.Absents.name -> redText
                            PersonnelReportStatusTypeEnum.Presents.name -> green
                            PersonnelReportStatusTypeEnum.Overtime.name -> yellow
                            PersonnelReportStatusTypeEnum.Rest.name -> gray3
                            else -> gray3
                        }, CircleShape
                    ),
                onLoading = { progress -> CircularProgressIndicator(progress) },
                onFailure = {
                    painterResource("user.xml")
                }
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = user.fullName.toString(),
                style = MaterialTheme.typography.body1,
                color = black,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = user.position.toString(),
                style = MaterialTheme.typography.caption,
                color = gray3,
            )
        }
        Box(Modifier.fillMaxWidth()) {
            Column(Modifier.align(Alignment.CenterEnd), horizontalAlignment = Alignment.End) {
                Text(
                    text = user.structureName.toString(),
                    style = MaterialTheme.typography.body1,
                    color = black,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${localization(lastAtt).value} ${
                        user.lastAtt?.toDateHumanReadable().let {
                            when (it?.first) {
                                DayStructure.Today -> "${localization(today).value} ${it.second}"
                                DayStructure.Yesterday -> "${localization(yesterday).value} ${it.second}"
                                DayStructure.Other -> it.second
                                null -> localization(noRecord).value
                            }
                        }
                    }",
                    style = MaterialTheme.typography.caption,
                    color = gray3,
                )
            }
        }
    }
}