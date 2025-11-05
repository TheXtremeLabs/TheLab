@file:OptIn(ExperimentalMaterial3Api::class)

package com.riders.thelab.central.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.riders.thelab.central.BuildConfig
import com.riders.thelab.central.R
import com.riders.thelab.core.data.local.model.app.PackageApp
import com.riders.thelab.core.data.local.model.compose.WindowSizeClass
import com.riders.thelab.core.data.utils.UiState
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.loading.LabLoader
import com.riders.thelab.core.ui.compose.component.toolbar.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.component.toolbar.ToolbarSize
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.utils.UIManager
import kotlinx.coroutines.launch

private val gridHorizontalArrangement = Arrangement.spacedBy(16.dp)
private val gridVerticalArrangement = Arrangement.spacedBy(12.dp)

///////////////////////////////////////
//
// COMPOSABLE
//
///////////////////////////////////////
@Composable
fun CentralScreenPortrait(
    theme: AppTheme,
    darkTheme: Boolean,
    windowSize: WindowSizeClass,
    centralUiState: UiState<List<PackageApp>>,
    uiEvent: (UiEvent) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val bottomSheetState: SheetState = rememberModalBottomSheetState()
    val bottomSheetScaffoldState =
        rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

    val isBottomSheetExpended by derivedStateOf { bottomSheetScaffoldState.bottomSheetState.isVisible }

    val animatedSheetPeekHeight by animateDpAsState(
        targetValue = if (!isBottomSheetExpended) 0.dp else 128.dp,
        label = "animated_bottom_sheet_peek_height"
    )

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        BottomSheetScaffold(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            topBar = {
                TheLabTopAppBar(
                    theme = theme,
                    darkTheme = darkTheme,
                    toolbarSize = ToolbarSize.SMALL,
                    title = stringResource(R.string.app_name),
                    titleColor = if (!darkTheme) Color.Black else Color.White,
                    navigationIcon = {
                        Box(
                            modifier = Modifier.padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.matchParentSize(),
                                painter = painterResource(if (!darkTheme) R.drawable.ic_the_lab_central else R.drawable.ic_the_lab_central_white),
                                contentDescription = null
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    if (!isBottomSheetExpended) {
                                        bottomSheetScaffoldState.bottomSheetState.show()
                                    } else {
                                        bottomSheetScaffoldState.bottomSheetState.hide()
                                    }
                                }
                                uiEvent.invoke(UiEvent.OnInfoClicked)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "info_icon"
                            )
                        }
                    }
                )
            },
            scaffoldState = bottomSheetScaffoldState,
            sheetContent = {
                BottomSheetContent(
                    theme = theme,
                    darkTheme = darkTheme,
                    uiEvent = uiEvent
                )
            },
            sheetPeekHeight = animatedSheetPeekHeight
        ) { innerPadding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.TopCenter
            ) {
                AnimatedContent(
                    modifier = Modifier.matchParentSize(),
                    targetState = centralUiState,
                    contentAlignment = Alignment.TopCenter
                ) { targetState ->

                    when (targetState) {
                        is UiState.Loading -> {
                            LabLoader(modifier = Modifier.size(56.dp))
                        }

                        is UiState.Success -> {
                            val lazyGridState = rememberLazyGridState()

                            LazyVerticalGrid(
                                modifier = Modifier.matchParentSize(),
                                state = lazyGridState,
                                columns = GridCells.Fixed(2)
                            ) {
                                itemsIndexed(items = targetState.data) { _, item ->
                                    CentralPackageItem(
                                        theme = theme,
                                        darkTeme = darkTheme,
                                        packageItem = item,
                                        uiEvent = uiEvent
                                    )
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
}

@Composable
fun CentralScreenLandscape(
    theme: AppTheme,
    darkTheme: Boolean,
    windowSize: WindowSizeClass,
    centralUiState: UiState<List<PackageApp>>,
    uiEvent: (UiEvent) -> Unit
) {
    val context = LocalContext.current
    val bottomSheetState = rememberModalBottomSheetState()
    val bottomSheetScaffoldState =
        rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)


    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        BottomSheetScaffold(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            topBar = {
                TheLabTopAppBar(
                    theme = theme,
                    darkTheme = darkTheme,
                    toolbarSize = ToolbarSize.SMALL,
                    title = stringResource(R.string.app_name),
                    titleColor = if (!darkTheme) Color.Black else Color.White,
                    backgroundColor = MaterialTheme.colorScheme.background,
                    navigationIcon = {
                        Box(
                            modifier = Modifier.padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                modifier = Modifier.matchParentSize(),
                                painter = painterResource(if (!darkTheme) R.drawable.ic_the_lab_central else R.drawable.ic_the_lab_central_white),
                                contentDescription = null
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { uiEvent.invoke(UiEvent.OnInfoClicked) }) {
                            Icon(
                                imageVector = Icons.Outlined.Info,
                                contentDescription = "info_icon"
                            )
                        }
                    }
                )
            },
            scaffoldState = bottomSheetScaffoldState,
            sheetContent = {
                BottomSheetContent(
                    theme = theme,
                    darkTheme = darkTheme,
                    uiEvent = uiEvent
                )
            },
            sheetPeekHeight = 128.dp
        ) { innerPadding ->

            AnimatedContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                targetState = centralUiState,
                contentAlignment = Alignment.TopCenter
            ) { targetState ->

                when (targetState) {
                    is UiState.Loading -> {
                        LabLoader(modifier = Modifier.size(56.dp))
                    }

                    is UiState.Success -> {
                        val lazyGridState = rememberLazyGridState()

                        BoxWithConstraints(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            LazyVerticalGrid(
                                modifier = Modifier.size(
                                    width = this.maxWidth,
                                    height = this.maxHeight
                                ),
                                state = lazyGridState,
                                columns = GridCells.Adaptive(128.dp),
                            ) {
                                itemsIndexed(items = targetState.data) { index, item ->
                                    CentralPackageItem(
                                        theme = theme,
                                        darkTeme = darkTheme,
                                        packageItem = item,
                                        uiEvent = uiEvent
                                    )
                                }
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CentralScreen(
    theme: AppTheme,
    darkTheme: Boolean,
    windowSize: WindowSizeClass? = null,
    centralUiState: UiState<List<PackageApp>>,
    uiEvent: (UiEvent) -> Unit
) {
    val configuration = LocalConfiguration.current

    if (Configuration.ORIENTATION_PORTRAIT == configuration.orientation) {
        CentralScreenPortrait(
            theme = theme,
            darkTheme = darkTheme,
            windowSize = windowSize ?: WindowSizeClass.COMPACT,
            centralUiState = centralUiState,
            uiEvent = uiEvent
        )
    } else {
        CentralScreenLandscape(
            theme = theme,
            darkTheme = darkTheme,
            windowSize = windowSize ?: WindowSizeClass.COMPACT,
            centralUiState = centralUiState,
            uiEvent = uiEvent
        )
    }
}


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewCentralScreen() {
    val context = LocalContext.current

    TheLabTheme(theme = AppTheme.Default) {
        CentralScreen(
            theme = AppTheme.Default,
            darkTheme = false,
            centralUiState = UiState.Success(
                listOf<PackageApp>(
                    PackageApp(
                        name = stringResource(R.string.app_name),
                        drawableIcon = UIManager.getDrawable(
                            context = context,
                            drawableResId = com.riders.thelab.core.ui.R.drawable.ic_lab_6_lab
                        )!!,
                        version = BuildConfig.VERSION_NAME,
                        packageName = BuildConfig.APPLICATION_ID
                    )
                )
            )
        ) {}
    }
}