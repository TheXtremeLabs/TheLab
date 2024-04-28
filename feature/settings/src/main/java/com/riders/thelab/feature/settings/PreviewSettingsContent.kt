package com.riders.thelab.feature.settings

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
import androidx.hilt.navigation.compose.hiltViewModel
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
fun SettingsContent(viewModel: SettingsViewModel) {
    val lazyListState = rememberLazyListState()

    TheLabTheme(darkTheme = viewModel.isDarkMode) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TheLabTopAppBar(
                    viewModel = viewModel,
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
                item { AppSettingsSection(viewModel) }
                item { DeviceInfoSection(viewModel) }

                if (null != viewModel.user) {
                    item {
                        UserSection(viewModel.user!!.username, viewModel.user!!.email) {
                            viewModel.logout()
                        }
                    }
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
    val viewModel: SettingsViewModel = hiltViewModel()
    TheLabTheme {
        SettingsContent(viewModel)
    }
}