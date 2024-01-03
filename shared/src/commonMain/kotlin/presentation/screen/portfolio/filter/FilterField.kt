package com.attendace.leopard.presentation.screen.portfolio.filter

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.gray3

@Composable
fun FilterField(
    data: PortfolioFilterElement,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = data.title)
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .border(width = 1.dp, color = gray3, shape = RoundedCornerShape(12.dp))
                .padding(vertical = 16.dp, horizontal = 8.dp)
        ) {
            Text(text = data.placeholderText, modifier = Modifier.weight(1f) ,style = MaterialTheme.typography.subtitle2)
            Spacer(modifier = Modifier.height(8.dp))
            Icon(
                painter = rememberVectorPainter(image = data.icon),
                contentDescription = data.title ,
                 tint = gray3
            )
        }
    }

}