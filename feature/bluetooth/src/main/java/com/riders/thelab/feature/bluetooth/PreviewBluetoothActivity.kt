package com.riders.thelab.feature.bluetooth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.toolbar.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.theme.TheLabTheme


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun BluetoothContent(viewModel: BluetoothViewModel) {
    val bluetoothState by viewModel.isBluetoothEnabled.collectAsStateWithLifecycle()

    TheLabTheme {
        Scaffold(
            topBar = {
                TheLabTopAppBar(
                    title = stringResource(id = R.string.activity_bluetooth_title),
                    navigationIcon = {})
            }
        ) { contentPadding ->
            AnimatedContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                targetState = bluetoothState,
                transitionSpec = { fadeIn() + slideInVertically() togetherWith slideOutHorizontally() + fadeOut() },
                label = ""
            ) { bluetoothTargetState ->
                if (!bluetoothTargetState) {
                    BluetoothDisabledContent(viewModel)
                } else {
                    BluetoothEnabledContent(viewModel)
                }
            }
        }
    }
}

///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewBluetoothContent() {
    TheLabTheme {
        BluetoothContent(viewModel = BluetoothViewModel())
    }
}