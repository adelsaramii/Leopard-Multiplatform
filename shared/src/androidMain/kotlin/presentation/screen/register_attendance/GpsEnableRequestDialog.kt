//package com.attendace.leopard.presentation.screen.register_attendance
//
//import android.app.Activity
//import android.content.Context
//import android.util.Log
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.ColorFilter
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.window.Dialog
//import com.attendace.leopard.util.checkLocationSetting
//import com.attendace.leopard.util.component.LeopardLoadingButton
//import com.attendace.leopard.util.theme.gray
//import com.attendace.leopard.util.theme.localization
//import com.attendace.leopard.util.theme.turn_on
//import com.attendace.leopard.util.theme.turn_on_gps
//import com.attendace.leopard.util.theme.white
//import org.jetbrains.compose.resources.ExperimentalResourceApi
//import org.jetbrains.compose.resources.painterResource
//
//@OptIn(ExperimentalResourceApi::class)
//@Composable
//fun GpsEnableDialog(
//    onDismiss: () -> Unit,
//    onEnabled: () -> Unit
//) {
//    val context: Context = LocalContext.current
//
//    val settingResultRequest = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.StartIntentSenderForResult()
//    ) { activityResult ->
//        if (activityResult.resultCode == Activity.RESULT_OK)
//            onEnabled()
//        else
//            Log.d("appDebug", "Denied")
//    }
//
//
//    Dialog(onDismissRequest = { onDismiss() }) {
//        Column(
//            modifier = Modifier
//                .background(color = white, shape = RoundedCornerShape(16.dp))
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//
//            Image(
//                modifier = Modifier.size(42.dp),
//                painter = painterResource("ic_location.xml"),
//                colorFilter = ColorFilter.tint(gray),
//                contentDescription = "ic_location"
//            )
//            Text(localization(turn_on_gps).value)
//            LeopardLoadingButton(
//                modifier = Modifier.padding(8.dp),
//                onClick = {
//                    context.checkLocationSetting(
//                        onDisabled = { intentSenderRequest ->
//                            settingResultRequest.launch(intentSenderRequest)
//                        },
//                        onEnabled = {
//                            onEnabled()
//                        }
//                    )
//                },
//            ) {
//                Text(
//                    localization(turn_on).value, style = TextStyle(color = white)
//                )
//            }
//        }
//
//    }
//
//}
