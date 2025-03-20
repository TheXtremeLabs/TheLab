package com.riders.thelab.feature.kat.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.riders.thelab.core.data.local.model.kat.KatModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.kat.utils.FirebaseUtils


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@Composable
fun OtherSender(theme: AppTheme, darkTheme: Boolean, message: String) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 96.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
            verticalAlignment = Alignment.Bottom
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                verticalAlignment = Alignment.Bottom
            ) {
                Column(modifier = Modifier.weight(.4f), verticalArrangement = Arrangement.Bottom) {
                    UserIcon(
                        card = MaterialTheme.colorScheme.tertiary,
                        iconColor = MaterialTheme.colorScheme.onTertiary,
                    )
                }
                Card(
                    modifier = Modifier.weight(2f),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
                ) {
                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = message,
                        color = if (!isSystemInDarkTheme()) Color.White else Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun Me(theme: AppTheme, darkTheme: Boolean, message: String) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
            verticalAlignment = Alignment.Bottom
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 96.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
                verticalAlignment = Alignment.Bottom
            ) {
                Card(modifier = Modifier.weight(2f)) {
                    Text(modifier = Modifier.padding(16.dp), text = message)
                }
                Column(modifier = Modifier.weight(.4f), verticalArrangement = Arrangement.Bottom) {
                    UserIcon()
                }
            }
        }
    }
}

@Composable
fun KatItem(theme: AppTheme, darkTheme: Boolean, isValid: Boolean, chatItem: KatModel) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        if (isValid) {
            if (FirebaseUtils.getCurrentUserID()!! == chatItem.senderId) {
                Me(theme = theme, darkTheme = darkTheme, message = chatItem.message)
            } else {
                OtherSender(theme = theme, darkTheme = darkTheme, message = chatItem.message)
            }
        } else {
            Text(text = "Error", color = Color.Red, fontWeight = FontWeight.ExtraBold)
        }
    }
}


///////////////////////////////
//
// PREVIEWS
//
///////////////////////////////
@DevicePreviews
@Composable
private fun PreviewOtherSender(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        OtherSender(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            "Hi how are you today? It is just a quick reminder to tell you that the sun light is very hitting hard right now. With high temperature and so on, we need to stay hydrated"
        )
    }
}

@DevicePreviews
@Composable
private fun PreviewMe(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        Me(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            "I'm fine and you. How are you doing?"
        )
    }
}

@DevicePreviews
@Composable
private fun PreviewKatItem(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    val chatItem = KatModel("Hello How are you?", "ksdjbnsdkjnvojbdshuovbsdjbvsdv", Timestamp.now())
    TheLabTheme(theme = appTheme) {
        Column {
            KatItem(theme = appTheme, darkTheme = isSystemInDarkTheme(), true, chatItem)
            KatItem(theme = appTheme, darkTheme = isSystemInDarkTheme(), false, chatItem)
        }
    }
}