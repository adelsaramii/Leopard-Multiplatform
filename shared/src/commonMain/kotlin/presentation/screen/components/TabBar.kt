package com.attendace.leopard.presentation.screen.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.attendace.leopard.data.base.Failed
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.data.base.Loading
import com.attendace.leopard.data.base.NotLoaded
import com.attendace.leopard.util.theme.all
import com.attendace.leopard.util.theme.localization
import com.attendace.leopard.util.theme.primary
import com.attendace.leopard.util.theme.white
import com.attendance.leopard.data.model.LeopardTabBarTypes

@Composable
fun LeopardTabBar(
    list: LoadableData<List<LeopardTabBarTypes>>,
    modifier: Modifier = Modifier,
    hasAllInFirst: Boolean = true,
    retry: () -> Unit,
    onTabClicked: (String) -> Unit = {},
) {
    when (list) {
        is Failed -> {
            ErrorPage(
                modifier = Modifier.fillMaxSize(),
                description = "retry"
            ) {
                retry()
            }
        }

        is Loaded -> {
            var selectedIndex by rememberSaveable { mutableStateOf(0) }
            CustomScrollableTabRow(
                tabs = list.data,
                selectedTabIndex = selectedIndex,
                modifier = modifier,
                hasAllInFirst = hasAllInFirst
            ) { tabIndex, id ->
                if (selectedIndex != tabIndex) onTabClicked(id)
                selectedIndex = tabIndex
            }
        }

        Loading -> {
            ShimmerTabBar(tabCount = 3)
        }

        NotLoaded -> {}
    }
}

@Composable
fun CustomScrollableTabRow(
    tabs: List<LeopardTabBarTypes>,
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    hasAllInFirst: Boolean,
    onTabClicked: (Int, String) -> Unit
) {
    val density = LocalDensity.current
    val tabWidths = remember {
        val tabWidthStateList = mutableStateListOf<Dp>()
        repeat(tabs.size) { tabWidthStateList.add(0.dp) }
        tabWidthStateList
    }
    ScrollableTabRow(selectedTabIndex = selectedTabIndex,
        backgroundColor = Color.Transparent,
        edgePadding = 12.dp,
        modifier = modifier
            .fillMaxWidth(),
        indicator = { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = modifier
                    .customTabIndicatorOffset(
                        currentTabPosition = tabPositions[selectedTabIndex],
                        tabWidth = tabWidths[selectedTabIndex]
                    )
                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp)),
                height = 5.dp,
                color = white
            )
        }) {
        tabs.forEachIndexed { tabIndex, tab ->
            Tab(selected = selectedTabIndex == tabIndex,
                onClick = { onTabClicked(tabIndex, tab.id) },
                text = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = if (tabIndex == 0 && hasAllInFirst) localization(all).value
                        else if (tab.name == "All") localization(all).value
                        else tab.name,
                            style = MaterialTheme.typography.body2,
                            color = white,
                            onTextLayout = { textLayoutResult ->
                                tabWidths[tabIndex] =
                                    with(density) { textLayoutResult.size.width.toDp() }
                            })
                        Box(
                            modifier = modifier
                                .background(white, CircleShape)
                                .size(24.dp, 16.dp)
                        ) {
                            Text(
                                style = TextStyle(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.Center
                                ),
                                modifier = Modifier.align(Alignment.Center),
                                text = tab.count.toString(),
                                color = primary,
                            )
                        }

                    }
                })
        }
    }
}

fun Modifier.customTabIndicatorOffset(
    currentTabPosition: TabPosition, tabWidth: Dp
): Modifier = composed(inspectorInfo = debugInspectorInfo {
    name = "customTabIndicatorOffset"
    value = currentTabPosition
}) {
    val currentTabWidth by animateDpAsState(
        targetValue = tabWidth + 26.dp,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    val indicatorOffset by animateDpAsState(
        targetValue = ((currentTabPosition.left + currentTabPosition.right - (tabWidth + 26.dp)) / 2),
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .width(currentTabWidth)
}