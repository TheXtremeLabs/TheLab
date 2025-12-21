package com.riders.thelab.feature.settings.main

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.model.compose.settings.DeviceInfoUiState
import com.riders.thelab.core.data.local.model.compose.settings.UserUiState
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsPhoneOnly
import com.riders.thelab.core.ui.compose.component.toolbar.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.component.toolbar.ToolbarSize
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@Composable
fun SettingsContent(
    theme: AppTheme,
    darkTheme: Boolean,
    deviceInformationUiState: DeviceInfoUiState,
    userUiState: UserUiState,
    themeOptions: List<String>,
    version: String,
    preselectedDarkModeOption: String,
    showModeInfo: Boolean,
    isVibration: Boolean,
    isActivitiesSplashEnabled: Boolean,
    uiEvent: (UiEvent) -> Unit
) {
    val lazyListState = rememberLazyListState()

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TheLabTopAppBar(
                    theme = theme,
                    toolbarSize = ToolbarSize.MEDIUM,
                    title = stringResource(id = R.string.activity_settings_title),
                    titleColor = MaterialTheme.colorScheme.onSurface,
                    withGradientBackground = true,
                )
            }
        ) { contentPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                state = lazyListState,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Spacer(modifier = Modifier.size(width = 0.dp, height = 8.dp)) }
                item {
                    AppSettingsSection(
                        theme = theme,
                        darkTheme = darkTheme,
                        themeOptions = themeOptions,
                        version = version,
                        preselectedDarkModeOption = preselectedDarkModeOption,
                        isVibration = isVibration,
                        isActivitiesSplashEnabled = isActivitiesSplashEnabled,
                        uiEvent = uiEvent
                    )
                }
                item {
                    DeviceInfoSection(
                        theme = theme, darkTheme = darkTheme,
                        deviceInformationUiState = deviceInformationUiState,
                        showModeInfo = showModeInfo,
                        uiEvent = uiEvent
                    )
                }

                item {
                    UserSection(
                        theme = theme,
                        darkTheme = darkTheme,
                        userUiState = userUiState,
                        uiEvent = uiEvent
                    )
                }
            }
        }
    }
}


///////////////////////////////
//
// PREVIEWS
//
///////////////////////////////
@DevicePreviewsPhoneOnly
@Composable
private fun PreviewSettingsContent(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        SettingsContent(
            theme = appTheme, darkTheme = isSystemInDarkTheme(),
            deviceInformationUiState = DeviceInfoUiState.Loading,
            userUiState = UserUiState.Loading,
            themeOptions = listOf("Light", "Dark"),
            version = "12.14.11",
            preselectedDarkModeOption = "Light",
            showModeInfo = true,
            isVibration = true,
            isActivitiesSplashEnabled = false
        ) {}
    }
}