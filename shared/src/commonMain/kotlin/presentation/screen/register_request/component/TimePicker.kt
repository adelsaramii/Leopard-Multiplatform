package com.attendace.leopard.presentation.screen.register_request.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.*
import com.attendance.leopard.data.model.FormItem
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogProperties
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@Composable
fun TimePicker(
    formItem: FormItem, onValueChangeListener: (String) -> Unit
) {
    val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time

    var pickerValue by remember { mutableStateOf(LocalTime(now.hour, now.minute, now.second)) }
    val dialogState = rememberMaterialDialogState()

    LaunchedEffect(key1 = Unit) {
        onValueChangeListener("${pickerValue.hour}:${pickerValue.minute}")
    }

    MaterialDialog(
        properties = MaterialDialogProperties(),
        dialogState = dialogState,
        buttons = {
            positiveButton("Ok")
            negativeButton("Cancel")
        }
    ) {
        timepicker { time ->
            pickerValue = time
            onValueChangeListener("${time.hour}:${time.minute}")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (formItem.editable) 1f else 0.8f)
            .padding(8.dp)
    ) {
        Text(
            modifier = Modifier.padding(vertical = 4.dp),
            text = formItem.title,
            color = textColor,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.W500,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = gray, shape = RoundedCornerShape(8.dp))
                .padding(12.dp)
                .clickable {
                    if (formItem.editable) {
                        dialogState.show()
                    }
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier.padding(start = 4.dp),
                imageVector = Icons.Outlined.Timer,
                contentDescription = "Timer",
                colorFilter = ColorFilter.tint(gray3)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {

                if (!formItem.editable) {
                    Text(
                        text = formItem.stringValue ?: "",
                        style = MaterialTheme.typography.body2,
                        color = textColor,
                    )
                } else {
                    Text(
                        text = "${pickerValue.hour}:${pickerValue.minute}",
                        style = MaterialTheme.typography.body2,
                        color = textColor,
                    )
                }
            }

            if (!formItem.editable) {
                Image(
                    modifier = Modifier.padding(end = 4.dp),
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = "Lock",
                    colorFilter = ColorFilter.tint(gray3)
                )
            }


        }
    }

}