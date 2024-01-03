package com.attendace.leopard.presentation.screen.register_request.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.attendace.leopard.util.theme.*
import com.attendance.leopard.data.model.FormItem
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TextFieldComponent(
    modifier: Modifier = Modifier,
    placeholder: String = "",
    formItem: FormItem,
    onValueChange: (String) -> Unit = {}
) {
    var value by remember { mutableStateOf(formItem.stringValue ?: "") }

    LaunchedEffect(key1 = Unit) {
        onValueChange("")
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
        TextField(
            modifier = modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = gray, shape = RoundedCornerShape(8.dp)),
            value = value,
            isError = formItem.hasError,
            readOnly = !formItem.editable,
            textStyle = MaterialTheme.typography.body2,
            onValueChange = {
                value = it
                onValueChange(it)
            },
            placeholder = { Text(text = placeholder, color = black, fontSize = 14.sp) },
            trailingIcon = {
                if (!formItem.editable) {
                    Image(
                        modifier = Modifier.padding(end = 4.dp),
                        imageVector = Icons.Outlined.Lock,
                        contentDescription = "Lock",
                        colorFilter = ColorFilter.tint(gray3)
                    )
                }
            },
            leadingIcon = {
                Image(painterResource("ic_description.xml"), "description")
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = white,
                textColor = textColor,
                focusedIndicatorColor = Color.Transparent,
                cursorColor = primary,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }

}