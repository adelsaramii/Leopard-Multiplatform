package com.attendace.leopard.presentation.screen.components.combo

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import com.attendace.leopard.presentation.screen.components.localization
import com.attendace.leopard.util.theme.black
import com.attendace.leopard.util.theme.gray2
import com.attendace.leopard.util.theme.gray3
import com.attendace.leopard.util.theme.localization
import com.attendace.leopard.util.theme.search
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ComboSearchRow(
    searchText: TextFieldValue,
    onBackPressed: () -> Unit,
    onSearchTextChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackPressed) {
            Icon(
                painter = painterResource("arrow_left.xml"),
                modifier = Modifier.localization(),
                contentDescription = "back",
                tint = black
            )
        }
        OutlinedTextField(
            value = searchText,
            onValueChange = {
                onSearchTextChange(it)
            },
            placeholder = { Text(localization(search).value, color = gray3) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = gray2,
                textColor = black,
                unfocusedBorderColor = Color.Transparent
            ),
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = "search", tint = black)
            },
            shape = RoundedCornerShape(50),
            singleLine = true,
            modifier = Modifier.weight(1f, true),
            trailingIcon = {
                if (searchText.text.isNotEmpty())
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "clear text",
                        tint = black,
                        modifier = Modifier
                            .clickable {
                                onSearchTextChange(TextFieldValue())
                            }
                    )
            }
        )
    }
}