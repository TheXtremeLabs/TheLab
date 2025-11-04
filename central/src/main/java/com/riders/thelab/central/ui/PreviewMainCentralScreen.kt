package com.riders.thelab.central.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.Icon
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.riders.thelab.central.R
import com.riders.thelab.core.data.local.model.app.PackageApp
import com.riders.thelab.core.data.utils.UiState
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsPhoneOnly
import com.riders.thelab.core.ui.compose.component.loading.LabLoader
import com.riders.thelab.core.ui.compose.component.toolbar.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.component.toolbar.ToolbarSize
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme

///////////////////////////////////////
//
// COMPOSABLE
//
///////////////////////////////////////
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainCentralScreen(
    theme: AppTheme,
    darkTheme: Boolean,
    centralUiState: UiState<List<PackageApp>>
) {
    val bottomSheetState = rememberBottomSheetScaffoldState()

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        BottomSheetScaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TheLabTopAppBar(
                    theme = theme,
                    darkTheme = darkTheme,
                    toolbarSize = ToolbarSize.SMALL,
                    title = stringResource(R.string.app_name),
                    navigationIcon = {
                        Icon(
                            painter = painterResource(if (!isSystemInDarkTheme()) R.drawable.ic_the_lab_central else R.drawable.ic_the_lab_central_white),
                            contentDescription = null
                        )
                    }
                )
            },
            scaffoldState = bottomSheetState,
            sheetContent = {},
            sheetPeekHeight = 128.dp
        ) { innerPadding ->
            AnimatedContent(
                modifier = Modifier.fillMaxWidth(),
                targetState = centralUiState,
                contentAlignment = Alignment.TopCenter
            ) { targetState ->

                when (targetState) {
                    is UiState.Loading -> {
                        LabLoader(modifier = Modifier.size(56.dp))
                    }

                    is UiState.Success -> {
                        val listState = rememberLazyListState()
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            state = listState
                        ) {
                            itemsIndexed(items = targetState.data) { index, item ->
                                Text(text = item.packageName)
                            }

                        }
                    }

                    is UiState.Error -> {
                        Text(text = targetState.error)
                    }

                    else -> {
                        Box(modifier = Modifier)
                    }
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
@DevicePreviewsPhoneOnly
@Composable
private fun PreviewMainCentralScreen() {
    TheLabTheme(theme = AppTheme.Default) {
        MainCentralScreen(
            theme = AppTheme.Default,
            darkTheme = false,
            centralUiState = UiState.Loading
        )
    }
}