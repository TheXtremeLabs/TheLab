package com.riders.thelab.feature.home

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import com.riders.thelab.core.data.local.model.app.LocalApp
import com.riders.thelab.core.data.local.model.compose.WindowSizeClass
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsTV
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.LocalAppPreviewProvider
import com.riders.thelab.core.ui.compose.previewprovider.WindowSizeClassPreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.TheLabThemeTV
import timber.log.Timber


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun HomeScreen(
    theme: AppTheme = AppTheme.Default,
    darkTheme: Boolean = isSystemInDarkTheme(),
    windowSize: WindowSizeClass?
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    // val windowSize: WindowSizeClass? = remember { (context.findActivity() as HomeActivity).windowSize }

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        when (windowSize) {
            WindowSizeClass.COMPACT -> if (Configuration.ORIENTATION_PORTRAIT == configuration.orientation) HomeScreenPortrait() else HomeScreenLandscape()
            WindowSizeClass.MEDIUM -> if (Configuration.ORIENTATION_PORTRAIT == configuration.orientation) HomeScreenTabletPortrait() else HomeScreenTabletLandscape()
            WindowSizeClass.EXPANDED -> if (Configuration.ORIENTATION_PORTRAIT == configuration.orientation) HomeScreenPortrait() else HomeScreenLandscape()
            null -> {
                Timber.e("RECOMPOSITION | Unable to get window size class. windowSize is null")
                Box() {}
            }
        }
    }
}

@Composable
fun HomeScreenPortrait(
    theme: AppTheme = AppTheme.Default,
    darkTheme: Boolean = isSystemInDarkTheme()
) {

    TheLabTheme(theme = theme, darkTheme = darkTheme) {

    }
}

@Composable
fun HomeScreenLandscape(
    theme: AppTheme = AppTheme.Default,
    darkTheme: Boolean = isSystemInDarkTheme()
) {

    TheLabTheme(theme = theme, darkTheme = darkTheme) {

    }
}

@Composable
fun HomeScreenTabletPortrait(
    theme: AppTheme = AppTheme.Default,
    darkTheme: Boolean = isSystemInDarkTheme()
) {

    TheLabTheme(theme = theme, darkTheme = darkTheme) {

    }
}

@Composable
fun HomeScreenTabletLandscape(
    theme: AppTheme = AppTheme.Default,
    darkTheme: Boolean = isSystemInDarkTheme()
) {

    TheLabTheme(theme = theme, darkTheme = darkTheme) {

    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HomeScreenTV(
    theme: AppTheme = AppTheme.Default,
    darkTheme: Boolean = isSystemInDarkTheme(),
    whatsNewList: List<LocalApp>,
) {
    val context = LocalContext.current
    val bringIntoViewRequester = remember { BringIntoViewRequester() }

    TheLabThemeTV(theme = theme, darkTheme = darkTheme) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {}
        ) { contentPadding ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .bringIntoViewRequester(bringIntoViewRequester)
                        .size(width = this.maxWidth, height = this.maxHeight)
                ) {
                    item {
                        WhatsNewContentTV(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(376.dp),
                            theme = theme,
                            darkTheme = darkTheme,
                            whatsNewList = whatsNewList
                        )
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
@DevicePreviews
@Composable
private fun PreviewHomeScreen(@PreviewParameter(WindowSizeClassPreviewProvider::class) windowSizeClass: WindowSizeClass) {
    TheLabTheme(theme = AppTheme.Default) {
        HomeScreen(windowSize = windowSizeClass)
    }
}

@DevicePreviewsTV
@Composable
private fun PreviewHomeScreenTV() {
    val context = LocalContext.current
    val localApps: List<LocalApp> = remember {
        LocalAppPreviewProvider(context = context).values.map { it as LocalApp }.toList()
    }

    TheLabThemeTV(theme = AppTheme.Default) {
        HomeScreenTV(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            whatsNewList = localApps
        )
    }
}


