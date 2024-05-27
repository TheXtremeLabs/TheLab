package com.riders.thelab.feature.settings.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.model.compose.settings.DeviceInfoUiState
import com.riders.thelab.core.data.local.model.compose.settings.UserUiState
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.toolbar.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.theme.TheLabTheme


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@Composable
fun SettingsContent(
    deviceInformationUiState: DeviceInfoUiState,
    userUiState: UserUiState,
    isDarkMode: Boolean,
    themeOptions: List<String>,
    version: String,
    showModeInfo: Boolean,
    isVibration: Boolean,
    isActivitiesSplashEnabled: Boolean,
    uiEvent: (UiEvent) -> Unit
) {
    val lazyListState = rememberLazyListState()

    TheLabTheme(darkTheme = isDarkMode) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TheLabTopAppBar(
                    isDarkMode = isDarkMode,
                    title = stringResource(id = R.string.activity_settings_title)
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
                item {
                    AppSettingsSection(
                        isDarkMode = isDarkMode,
                        themeOptions = themeOptions,
                        version = version,
                        isVibration = isVibration,
                        isActivitiesSplashEnabled = isActivitiesSplashEnabled,
                        uiEvent = uiEvent
                    )
                }
                item {
                    DeviceInfoSection(
                        deviceInformationUiState = deviceInformationUiState,
                        showModeInfo = showModeInfo,
                        uiEvent = uiEvent
                    )
                }

                item {
                    UserSection(userUiState = userUiState, uiEvent = uiEvent)
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
@DevicePreviews
@Composable
private fun PreviewSettingsContent() {
    TheLabTheme {
        SettingsContent(
            deviceInformationUiState = DeviceInfoUiState.Loading,
            userUiState = UserUiState.Loading,
            isDarkMode = true,
            themeOptions = listOf("Light", "Dark"),
            version = "12.14.11",
            showModeInfo = true,
            isVibration = true,
            isActivitiesSplashEnabled = false
        ) {}
    }
}