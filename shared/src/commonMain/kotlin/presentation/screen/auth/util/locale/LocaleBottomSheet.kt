package com.attendace.leopard.presentation.screen.auth.util.locale

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.MyLanguage
import com.attendace.leopard.util.theme.gray
import com.attendace.leopard.util.theme.localization
import com.attendace.leopard.util.theme.onSurface
import com.attendace.leopard.util.theme.select_language
import com.attendace.leopard.util.theme.white
import com.attendace.leopard.util.localization.LanguageTypeEnum
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun LocaleBottomSheet(
    modifier: Modifier,
    currentLanguage: LanguageTypeEnum,
    onCloseClick: () -> Unit,
    onClick: (MyLanguage) -> Unit
) {
    val locales = listOf(MyLanguage.English, MyLanguage.Kurdish, MyLanguage.Arabic)

    Scaffold(
        modifier = Modifier.heightIn(min = 100.dp, max = 300.dp),
        contentColor = white,
        containerColor = white
    ) { _ ->
        Column(
            modifier = Modifier.background(white).padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = localization(select_language).value,
                    style = MaterialTheme.typography.h6,
                    color = onSurface
                )
            }
            Spacer(Modifier.height(8.dp))
            Divider(color = gray.copy(alpha = 0.5f), modifier = Modifier.fillMaxWidth())
            LazyColumn {
                items(locales.toList()) {
                    LocaleRow(locale = it, currentLanguage) { locale ->
                        onClick(locale)
                    }
                }
            }
        }

    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun LocaleRow(
    locale: MyLanguage,
    currentLanguage: LanguageTypeEnum,
    onClick: (MyLanguage) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                onClick(locale)
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            Image(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.FillBounds,
                painter = painterResource(locale.image),
                contentDescription = locale.name
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = locale.name,
                style = MaterialTheme.typography.body1,
                color = onSurface
            )
        }

        RadioButton(
            selected = currentLanguage == locale.language,
            onClick = {
                onClick(locale)
            })

    }
}
