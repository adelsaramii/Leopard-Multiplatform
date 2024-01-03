package com.attendace.leopard.presentation.screen.home.monthly.subordinateSelect

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.gray2
import com.attendace.leopard.util.theme.gray3
import com.attendace.leopard.util.theme.primaryColor
import com.attendance.leopard.data.model.Subordinate

@Composable
fun SubordinateListItem(
    subordinate: Subordinate,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                onClick()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = primaryColor,
                unselectedColor = gray2
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${subordinate.fullName} | ${subordinate.personCode}",
            style = MaterialTheme.typography.body1,
            color = gray3,
            modifier = Modifier.weight(1f, true)
        )
    }
}