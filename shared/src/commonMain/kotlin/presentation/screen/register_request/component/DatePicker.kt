package com.attendace.leopard.presentation.screen.register_request.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.outlined.Lock
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.date.nowLocalDateTime
import com.attendace.leopard.util.theme.*
import com.attendace.leopard.util.theme.white
import com.attendance.leopard.data.model.FormItem
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.datetime.LocalDate

@Composable
fun DatePicker(
    formItem: FormItem, modifier: Modifier = Modifier, onValueChangeListener: (String) -> Unit
) {
    val dialogState = rememberMaterialDialogState(initialValue = false)
    var date by remember {
        val now = nowLocalDateTime()
        mutableStateOf(LocalDate(now.year, now.monthNumber, now.dayOfMonth))
    }

    DatePickerDialog(defaultValue = date, dialogState = dialogState) {
        date = it
        onValueChangeListener(it.toString())
    }

    LaunchedEffect(key1 = Unit) {
        onValueChangeListener("${date.year}-${date.monthNumber}-${date.dayOfMonth}")
    }
    Column(
        modifier = modifier
            .background(white)
            .fillMaxWidth()
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
                .alpha(if (formItem.editable) 1f else 0.8f)
                .clickable {
                    if (formItem.editable) {
                        dialogState.show()
                    }
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Image(
                modifier = Modifier.padding(start = 4.dp),
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = "CalendarMonth",
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
                        text = "${date.year}/${date.monthNumber}/${date.dayOfMonth}",
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

@Composable
fun DatePickerDialog(
    modifier: Modifier = Modifier,
    defaultValue: LocalDate,
    dialogState: MaterialDialogState,
    onOkClicked: (LocalDate) -> Unit = {},
) {
    var date = defaultValue
    Box(modifier = modifier) {
        MaterialDialog(dialogState = dialogState, buttons = {
            positiveButton(text = "OK",
                textStyle = TextStyle(color = primary, fontWeight = FontWeight.Medium),
                onClick = { onOkClicked(date) })
            negativeButton(
                text = "CANCEL",
                textStyle = TextStyle(color = primary, fontWeight = FontWeight.Medium)
            )
        }) {
            datepicker(
                initialDate = date,
                colors = DatePickerDefaults.colors(
                    headerBackgroundColor = primary,
                    dateActiveBackgroundColor = primary,
                    headerTextColor = white,
                    dateActiveTextColor = white,
                )
            ) { date = it }
        }
    }
}