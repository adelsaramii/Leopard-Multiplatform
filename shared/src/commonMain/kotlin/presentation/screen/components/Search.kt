package com.attendace.leopard.presentation.screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.attendace.leopard.util.theme.borderColor
import com.attendace.leopard.util.theme.gray3
import com.attendace.leopard.util.theme.localization
import com.attendace.leopard.util.theme.search
import com.attendace.leopard.util.theme.white
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun LeopardSearch(
    onSearchTextChange: (TextFieldValue) -> Unit,
    searchText: TextFieldValue,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(color = white)
            .height(52.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = searchText,
            textStyle = TextStyle(fontSize = 14.sp),
            onValueChange = { onSearchTextChange(it) },
            placeholder = { Text(localization(search).value, color = gray3, fontSize = 14.sp) },
            trailingIcon = {
                Image(painterResource("ic_search_portfolio.xml"), "search")
            },
            shape = RoundedCornerShape(25),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = white,
                textColor = gray3,
                unfocusedBorderColor = borderColor,
                focusedBorderColor = borderColor
            ),
            modifier = Modifier
                .weight(0.9f)
                .padding(0.dp)
        )
        /*Box(
            modifier = modifier
                .padding(vertical = 4.dp)
                .border(1.dp, primary, RoundedCornerShape(25))
                .width(64.dp)
                .height(56.dp)
                .clickable { },
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_filter), contentDescription = "filter"
            )
        }*/
    }
}