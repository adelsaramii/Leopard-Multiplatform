package com.attendance.leopard.android.ui.register_attendance


import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.attendace.leopard.util.theme.check_in
import com.attendace.leopard.util.theme.check_out
import com.attendace.leopard.util.theme.localization
import com.attendace.leopard.util.theme.primaryColor
import com.attendace.leopard.util.theme.secondaryColor
import com.attendace.leopard.util.theme.white
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalComposeUiApi::class, ExperimentalTextApi::class, ExperimentalResourceApi::class)
@Composable
fun RegisterAttendanceButtonRow(
    isRegisterAttendanceEnabled: Boolean,
    enterDrawableId: String,
    exitDrawableId: String,
    enterButtonState: ButtonState,
    exitButtonState: ButtonState,
    buttonMinWidth: Int,
    onCancel: () -> Unit,
    onEnter: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(48.dp)
            .padding(0.dp, 0.dp, 0.dp, 4.dp)
            .fillMaxWidth(),
    ) {
        val enterButtonTransition = updateTransition(
            enterButtonState,
            label = "transition1"
        )
        val enterAlphaAnimation by enterButtonTransition.animateFloat(
            label = "float1",
            transitionSpec = {
                when {
                    exitButtonState isTransitioningTo ButtonState.Pressed -> {
                        tween(durationMillis = 1000)
                    }
                    else -> {
                        tween(durationMillis = 1500)
                    }
                }
            }) {
            if (it == ButtonState.Released)
                1f
            else
                0f
        }

        val exitButtonTransition = updateTransition(
            exitButtonState,
            label = "transition2"
        )
        val exitAlphaAnimation by exitButtonTransition.animateFloat(
            label = "float2",
            transitionSpec = {
                when {
                    exitButtonState isTransitioningTo ButtonState.Pressed -> {
                        tween(durationMillis = 1000)
                    }
                    else -> {
                        tween(durationMillis = 1500)
                    }
                }
            }) {
            if (it == ButtonState.Released)
                1f
            else
                0f
        }

        //enter
        Button(
            onClick = onEnter,
            enabled = isRegisterAttendanceEnabled,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(horizontal = 16.dp)
//                .alpha(exitAlphaAnimation)
//                .conditional((enterButtonTransition.currentState == enterButtonTransition.targetState) ,
//                    ifTrue = {
//                        enterButtonState.value = ButtonState.Loading
//                        onEnter()
//                        Modifier
//                    })
//                .conditional(
//                    enterSizeAnimation == buttonMinWidth.dp,
//                    ifTrue = {
//                        pointerInteropFilter {
//                            when (it.action) {
//                                MotionEvent.ACTION_DOWN -> {
//                                    enterButtonState.value = ButtonState.Pressed
//                                }
//                                MotionEvent.ACTION_UP -> {
//                                    enterButtonState.value = ButtonState.Released
//                                }
//                            }
//                            true
//                        }
//                    }, ifFalse = {
//                        pointerInteropFilter {
//                            when (it.action) {
//                                MotionEvent.ACTION_DOWN -> {}
//                                MotionEvent.ACTION_MOVE -> {}
//                                else -> {
//                                    enterButtonState.value =
//                                        if ((enterButtonTransition.targetState == ButtonState.Loading) && enterSizeAnimation == maxWidth) {
//                                            ButtonState.Loading
//                                        } else {
//                                            onCancel()
//                                            ButtonState.Released
//                                        }
//                                }
//                            }
//                            true
//                        }
//                    })
            ,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = secondaryColor,
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.elevation()
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = localization(check_in).value,
                    fontSize = 16.sp,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
                if (enterButtonState == ButtonState.Loading)
                    CircularProgressIndicator(
                        modifier = Modifier.size(30.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                else
                    Icon(painterResource(enterDrawableId), "enter", tint = white)
            }
        }

        //exit
        Button(
            onClick = onExit,
            enabled = isRegisterAttendanceEnabled,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(horizontal = 16.dp)
            ,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = primaryColor,
                contentColor = Color.White
            ),
            elevation = ButtonDefaults.elevation()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (exitButtonState == ButtonState.Loading)
                    CircularProgressIndicator(
                        modifier = Modifier.size(30.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                else
                    Icon(painterResource(exitDrawableId), "exit")
                Text(
                    text = localization(check_out).value,
                    fontSize = 16.sp,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
            }
        }
    }

}

@Preview
@Composable
fun RegisterAttendanceLayoutPreview() {
    MaterialTheme {
        val enterButtonState by remember { mutableStateOf(ButtonState.Released) }
        val exitButtonState by remember { mutableStateOf(ButtonState.Released) }

        RegisterAttendanceButtonRow(
            enterDrawableId = "drawable_enter.xml",
            exitDrawableId = "drawable_exit.xml",
            enterButtonState = enterButtonState,
            exitButtonState = exitButtonState,
            buttonMinWidth = 112,
            onCancel = {

            },
            onEnter = {

            },
            onExit = {

            },
            isRegisterAttendanceEnabled = true
        )
    }
}

enum class ButtonState {
    Released, Pressed, Loading
}
