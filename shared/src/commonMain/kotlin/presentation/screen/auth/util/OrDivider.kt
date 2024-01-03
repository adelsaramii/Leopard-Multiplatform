package com.attendace.leopard.presentation.screen.auth.util

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.attendace.leopard.util.theme.gray
import com.attendace.leopard.util.theme.localization
import com.attendace.leopard.util.theme.or

@Composable
fun OrDivider() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(color = gray, modifier = Modifier.weight(4f))
        Text(
            localization(or).value,
            color = gray,
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .wrapContentWidth()
                .weight(1f)
        )
        Divider(color = gray, modifier = Modifier.weight(4f))
    }
}

