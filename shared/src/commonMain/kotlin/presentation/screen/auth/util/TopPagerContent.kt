package com.attendace.leopard.presentation.screen.auth.util

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.attendace.leopard.presentation.screen.components.localization
import com.attendace.leopard.presentation.screen.components.NeverOverScroll
import com.attendace.leopard.util.theme.desc_att_intro
import com.attendace.leopard.util.theme.desc_portfolio_intro
import com.attendace.leopard.util.theme.desc_submit_req_intro
import com.attendace.leopard.util.theme.inactiveWhite
import com.attendace.leopard.util.theme.localization
import com.attendace.leopard.util.theme.sub_title_att_intro
import com.attendace.leopard.util.theme.sub_title_portfolio_intro
import com.attendace.leopard.util.theme.sub_title_submit_req_intro
import com.attendace.leopard.util.theme.title_att_intro
import com.attendace.leopard.util.theme.title_portfolio_intro
import com.attendace.leopard.util.theme.title_submit_req_intro
import com.attendace.leopard.util.theme.white
import com.attendace.leopard.util.helper.tickerFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalFoundationApi::class, ExperimentalResourceApi::class)
@Composable
fun TopPagerContent(
    currentLanguageName: String,
    loginFlowPagerState: PagerState,
    onChangeLocaleClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight(0.5f)
            .fillMaxWidth()
            .clip(RoundedCornerShape(0.dp, 0.dp, 28.dp, 28.dp))
    ) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource("splash_bg.png"),
            contentDescription = "splash_bg",
            contentScale = ContentScale.FillBounds
        )
        Column(verticalArrangement = Arrangement.SpaceBetween) {
            val scope = rememberCoroutineScope()
            val pagerState =
                rememberPagerState(initialPage = 0, initialPageOffsetFraction = 0f) { 3 }

            LaunchedEffect(key1 = Unit) {
                tickerFlow(period = 3.seconds, initialDelay = 3.seconds).onEach {
                    pagerState.animateScrollToPage((pagerState.currentPage + 1) % pagerState.pageCount)
                }.launchIn(scope)
            }

            val shouldShowBack = loginFlowPagerState.currentPage != 0

            Box(Modifier.fillMaxWidth().padding(start = 22.dp, end = 22.dp, top = 22.dp)) {
                LanguageChangeRow(
                    currentLanguageName,
                    onBackClick = {
                        onBackClick()
                    },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                ) {
                    onChangeLocaleClick()
                }

                if (shouldShowBack) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "arrow",
                        tint = white,
                        modifier = Modifier
                            .clickable { onBackClick() }
                            .align(Alignment.CenterStart)
                            .localization(),

                        )
                }

            }

            NeverOverScroll {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.wrapContentHeight(),
                ) { page ->
                    LoginGuidePagerItem(
                        page = page, modifier = Modifier
                            .fillMaxHeight(0.9f)
                            .fillMaxWidth()
                            .padding(start = 30.dp, end = 30.dp, bottom = 24.dp, top = 36.dp)
                    )
                }
            }

            Row(
                Modifier
                    .height(50.dp)
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val color =
                        if (pagerState.currentPage == iteration) white else inactiveWhite
                    Box(
                        modifier = Modifier
                            .padding(3.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(30.dp)
                    )
                }
            }
        }

    }

}


@Composable
fun LoginGuidePagerItem(page: Int, modifier: Modifier = Modifier) {
    when (page) {
        0 -> {
            GuideItem(
                modifier = modifier,
                title = localization(title_att_intro).value,
                subtitle = localization(sub_title_att_intro).value,
                description = localization(desc_att_intro).value,
                imageId = "ic_intro_att.png"
            )
        }

        1 -> {
            GuideItem(
                modifier = modifier,
                title = localization(title_portfolio_intro).value,
                subtitle = localization(sub_title_portfolio_intro).value,
                description = localization(desc_portfolio_intro).value,
                imageId = "ic_intro_simple.png"
            )

        }

        2 -> {
            GuideItem(
                modifier = modifier,
                title = localization(title_submit_req_intro).value,
                subtitle = localization(sub_title_submit_req_intro).value,
                description = localization(desc_submit_req_intro).value,
                imageId = "ic_submit_req.png"
            )
        }
    }

}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun GuideItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    description: String,
    imageId: String
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(vertical = 4.dp),
            text = title,
            style = MaterialTheme.typography.h6,
            color = white
        )
        Image(
            painter = painterResource(imageId), contentDescription = "ic_intro_att"
        )
        Column(
            modifier = Modifier.padding(bottom = 16.dp),
            horizontalAlignment = CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = subtitle,
                style = MaterialTheme.typography.body1,
                color = white
            )
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = description,
                style = MaterialTheme.typography.caption,
                color = white,
                textAlign = TextAlign.Center
            )
        }

    }
}
