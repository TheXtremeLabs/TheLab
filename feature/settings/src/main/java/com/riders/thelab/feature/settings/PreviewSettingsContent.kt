package com.riders.thelab.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.Typography


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@Composable
fun CardRowItem(title: String, subtitle: String? = null, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(3f),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
        ) {

            Text(text = title)

            if (null != subtitle) {
                Text(text = subtitle)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(.5f),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "arrow_right"
            )
        }
    }
}


@Composable
fun AppSettingsSection(viewModel: SettingsViewModel) {
    TheLabTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                modifier = Modifier.padding(start = 24.dp),
                text = "App Settings",
                style = Typography.titleMedium
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1.5f),
                            verticalArrangement = Arrangement.spacedBy(
                                8.dp,
                                Alignment.CenterVertically
                            )
                        ) {
                            Text(text = "Dark Mode")
                            Text(
                                text = if (viewModel.isDarkMode) "Disable Dark Mode" else "Enable Dark Mode",
                                style = TextStyle(
                                    fontSize = 12.sp
                                )
                            )
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(.5f),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.size(30.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Switch(
                                    modifier = Modifier
                                        .size(30.dp)
                                        .padding(horizontal = 24.dp),
                                    checked = viewModel.isDarkMode,
                                    onCheckedChange = {
                                        // viewModel.toggleDarkMode()
                                        viewModel.updateDarkModeDatastore(it)
                                    }
                                )
                            }
                        }
                    }

                    CardRowItem(title = stringResource(id = R.string.activity_about_title)) {}
                    CardRowItem(title = stringResource(id = R.string.activity_title_detail_contacts)) {}
                    CardRowItem(title = stringResource(id = R.string.activity_title_filter_list_view_detail)) {}
                }
            }
        }
    }
}


@Composable
fun DeviceInfoSection() {
    TheLabTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                modifier = Modifier.padding(start = 24.dp),
                text = "Device Info",
                style = Typography.titleMedium
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {

                    CardRowItem(title = stringResource(id = R.string.activity_about_title)) {}
                    CardRowItem(title = stringResource(id = R.string.activity_title_detail_contacts)) {}
                    CardRowItem(title = stringResource(id = R.string.activity_title_filter_list_view_detail)) {}
                    CardRowItem(title = stringResource(id = R.string.activity_about_title)) {}
                    CardRowItem(title = stringResource(id = R.string.activity_title_detail_contacts)) {}
                    CardRowItem(title = stringResource(id = R.string.activity_title_filter_list_view_detail)) {}
                }
            }
        }
    }
}

@Composable
fun UserSection() {
    TheLabTheme {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                modifier = Modifier.padding(start = 24.dp),
                text = "User",
                style = Typography.titleMedium
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {

                    CardRowItem(title = stringResource(id = R.string.activity_about_title)) {}
                    CardRowItem(title = stringResource(id = R.string.activity_title_detail_contacts)) {}
                    CardRowItem(title = stringResource(id = R.string.activity_title_filter_list_view_detail)) {}
                    CardRowItem(title = stringResource(id = R.string.activity_about_title)) {}
                    CardRowItem(title = stringResource(id = R.string.activity_title_detail_contacts)) {}
                    CardRowItem(title = stringResource(id = R.string.activity_title_filter_list_view_detail)) {}
                }
            }
        }
    }
}


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
                item { DeviceInfoSection() }
                item { UserSection() }
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
private fun PreviewAppSettingsSection() {
    val viewModel: SettingsViewModel = hiltViewModel()
    TheLabTheme {
        AppSettingsSection(viewModel)
    }
}

@DevicePreviews
@Composable
private fun PreviewDeviceInfoSection() {
    TheLabTheme {
        DeviceInfoSection()
    }
}

@DevicePreviews
@Composable
private fun PreviewUserSection() {
    TheLabTheme {
        UserSection()
    }
}

@DevicePreviews
@Composable
private fun PreviewSettingsContent() {
    val viewModel: SettingsViewModel = hiltViewModel()
    TheLabTheme {
        SettingsContent(viewModel)
    }
}