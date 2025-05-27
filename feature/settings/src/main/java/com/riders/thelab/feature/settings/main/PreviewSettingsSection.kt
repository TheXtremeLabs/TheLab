package com.riders.thelab.feature.settings.main

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.Typography
import kotools.types.text.NotBlankString
import org.kotools.types.ExperimentalKotoolsTypesApi


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@Composable
fun SettingsSectionWithTitle(
    theme: AppTheme,
    darkTheme: Boolean,
    title: NotBlankString,
    content: @Composable () -> Unit
) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                modifier = Modifier.padding(start = 24.dp),
                text = title.toString(),
                style = Typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                colors = CardDefaults.cardColors(contentColor = MaterialTheme.colorScheme.surface)
            ) {
                content.invoke()
            }
        }
    }
}


///////////////////////////////
//
// PREVIEWS
//
///////////////////////////////
@OptIn(ExperimentalKotoolsTypesApi::class)
@DevicePreviews
@Composable
private fun PreviewSettingsSectionWithTitle(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    val deviceInfoUiState = PreviewProviderDeviceInfoUiState().values.toList()[2]

    TheLabTheme(theme = appTheme) {
        SettingsSectionWithTitle(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            title = NotBlankString.create("Device")
        ) {
            DeviceInfoSection(
                theme = appTheme,
                darkTheme = isSystemInDarkTheme(),
                deviceInformationUiState = deviceInfoUiState,
                showModeInfo = true
            ) { }
        }
    }
}