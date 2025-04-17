package com.riders.thelab.feature.koin.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.LabWebView
import com.riders.thelab.core.ui.compose.component.loading.LabLoader
import com.riders.thelab.core.ui.compose.component.snackbar.LabSnackBarDelegate
import com.riders.thelab.core.ui.compose.component.snackbar.SnackBarState
import com.riders.thelab.core.ui.compose.component.snackbar.rememberInstance
import com.riders.thelab.core.ui.compose.component.toolbar.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import kotlinx.coroutines.delay


////////////////////////////////////////////////////////////////////
//
// COMPOSE
//
////////////////////////////////////////////////////////////////////
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun KoinMainContent(htmlContent: String) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val theme = AppTheme.Default

    val snackbarHostState = remember { SnackbarHostState() }
    val lazyListState = rememberLazyListState()
    val snackbarDelegate: LabSnackBarDelegate by rememberInstance()
    /* val snackbarDelegate: LabSnackBarDelegate by remember {
         LabSnackBarDelegate(
             snackbarHostState = snackbarHostState,
             coroutineScope = coroutineScope
         )
     }*/

    TheLabTheme(theme = theme) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {

                TheLabTopAppBar(
                    theme = theme,
                    navigationIconColor = Color.White,
                    withGradientBackground = false,
                    title = stringResource(com.riders.thelab.core.ui.R.string.activity_title_koin)
                )
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarDelegate.snackbarHostState) { data ->
                    val backgroundColor = snackbarDelegate.snackbarBackgroundColor
                    // Snackbar(snackbarData =data , backgroundColor = backgroundColor)
                    Snackbar(contentColor = backgroundColor) {
                        Text(
                            text = data.visuals.message,
                            color = Color.White
                        )
                    }
                }
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                state = lazyListState,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
            ) {
                item {
                    Greeting(
                        name = "Android",
                        modifier = Modifier
                    )
                }

                item {
                    AnimatedContent(
                        targetState = if (LocalInspectionMode.current) true else htmlContent.trim()
                            .isNotBlank()
                    ) { targetState ->
                        if (!targetState) {
                            LabLoader(modifier = Modifier.size(56.dp))
                        } else {
                            LabWebView(
                                modifier = Modifier.fillMaxSize(),
                                htmlRawContent = htmlContent
                            )
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(250L)
        snackbarDelegate.showSnackbar(
            state = SnackBarState.WARNING,
            message = "Testttttttt",
            duration = SnackbarDuration.Long
        )
    }
}


////////////////////////////////////////////////////////////////////
//
// PREVIEWS
//
////////////////////////////////////////////////////////////////////
@PreviewScreenSizes
@PreviewLightDark
@PreviewDynamicColors
@DevicePreviews
@Composable
private fun PreviewGreeting(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        Card { Greeting(modifier = Modifier.padding(16.dp), name = "Android") }
    }
}

@DevicePreviews
@Composable
private fun PreviewKoinMainContent(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        KoinMainContent("Android")
    }
}