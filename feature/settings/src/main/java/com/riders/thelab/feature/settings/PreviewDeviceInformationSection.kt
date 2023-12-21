package com.riders.thelab.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.Typography


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
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


///////////////////////////////
//
// PREVIEWS
//
///////////////////////////////
@DevicePreviews
@Composable
private fun PreviewDeviceInfoSection() {
    TheLabTheme {
        DeviceInfoSection()
    }
}
