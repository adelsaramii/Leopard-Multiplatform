package com.attendace.leopard.presentation.screen.profile.list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.attendace.leopard.presentation.screen.components.localization
import com.attendace.leopard.util.theme.*
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ProfileLanguage(
    modifier: Modifier = Modifier,
    onLanguageClick: () -> Unit,
    selectedLanguage: String
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 16.dp, end = 16.dp, start = 16.dp)
            .background(shape = RoundedCornerShape(8.dp), color = gray2)
            .clickable { onLanguageClick() },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource("ic_language.xml"),
                contentDescription = "language",
            )
            Text(
                text = localization(languageName).value,
                style = MaterialTheme.typography.caption,
                color = textColor,
                fontWeight = FontWeight.W500
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = selectedLanguage,
                style = MaterialTheme.typography.body2,
                color = gray3,
                fontWeight = FontWeight.W500
            )
            Image(
                painter = painterResource("angle_right.xml"),
                contentDescription = "angle right",
                modifier = Modifier.localization(),
                colorFilter = ColorFilter.tint(color = textColor)
            )
        }
    }
}