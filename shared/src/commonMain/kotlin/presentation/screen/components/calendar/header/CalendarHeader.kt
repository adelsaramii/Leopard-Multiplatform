package com.attendace.leopard.presentation.screen.components.calendar.header

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.white
import com.attendance.leopard.data.model.WorkPeriod
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CalendarHeader(
    workPeriod: WorkPeriod?,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
    enableChangeMonth: Boolean = true,
    onMonthChangeClick: () -> Unit = {},
    nextEnable: Boolean = true,
    previousEnable: Boolean = true,
) {

    var isGoingForward by remember { mutableStateOf(true) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(start = 8.dp, bottom = 16.dp, top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CalendarHeaderArrowIconButton(
            "angle_left.xml",
            onClick = {
                isGoingForward = true
                onPreviousClick()
            },
            enabled = previousEnable
        )
        Row(
            modifier = Modifier
                .weight(1f)
                .height(36.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Row(modifier = Modifier.clickable {
                if (enableChangeMonth)
                    onMonthChangeClick()
            }) {
                AnimatedContent(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .wrapContentHeight()
                        .wrapContentWidth()
                        .align(Alignment.CenterVertically),
                    targetState = workPeriod?.name,
                    transitionSpec = {
                        addAnimation(isNext = isGoingForward).using(
                            SizeTransform(clip = false)
                        )
                    }
                ) {
                    it?.let { it1 ->
                        Text(
                            text = it1,
                            color = white,
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                if (enableChangeMonth) {
                    Icon(
                        painter = painterResource("angle_down.xml"),
                        contentDescription = "down",
                        tint = white
                    )
                }
            }

        }


        CalendarHeaderArrowIconButton(
            "angle_right.xml",
            onClick = {
                isGoingForward = false
                onNextClick()
            },
            enabled = nextEnable
        )
    }

}

@OptIn(ExperimentalAnimationApi::class)
internal fun addAnimation(duration: Int = 500, isNext: Boolean): ContentTransform {
    return slideInVertically(animationSpec = tween(durationMillis = duration)) { height -> if (isNext) height else -height } + fadeIn(
        animationSpec = tween(durationMillis = duration)
    ) with slideOutVertically(animationSpec = tween(durationMillis = duration)) { height -> if (isNext) -height else height } + fadeOut(
        animationSpec = tween(durationMillis = duration)
    )
}