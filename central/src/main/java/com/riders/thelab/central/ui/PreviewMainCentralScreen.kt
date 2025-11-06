@file:OptIn(ExperimentalMaterial3Api::class)

package com.riders.thelab.central.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.riders.thelab.central.BuildConfig
import com.riders.thelab.central.R
import com.riders.thelab.core.data.local.model.app.PackageApp
import com.riders.thelab.core.data.local.model.compose.IslandState
import com.riders.thelab.core.data.local.model.compose.WindowSizeClass
import com.riders.thelab.core.data.utils.UiState
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsPhoneOnly
import com.riders.thelab.core.ui.compose.component.dynamicisland.DynamicIsland
import com.riders.thelab.core.ui.compose.component.loading.LabLoader
import com.riders.thelab.core.ui.compose.component.toolbar.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.component.toolbar.ToolbarSize
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.utils.UIManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

///////////////////////////////////////
//
// COMPOSABLE
//
///////////////////////////////////////
@Composable
fun CentralToolbar(
    theme: AppTheme,
    darkTheme: Boolean,
    scope: CoroutineScope,
    bottomSheetState: SheetState,
    isBottomSheetExpended: Boolean,
    searchModeEnabled: Boolean,
    searchQuery: String,
    uiEvent: (UiEvent) -> Unit
) {
    val animatedToolbarHeight by animateDpAsState(
        targetValue = if (!searchModeEnabled) 56.dp else 96.dp,
        label = "animated_toolbar_height_for_dynamic_island"
    )

    TheLabTopAppBar(
        theme = theme,
        darkTheme = darkTheme,
        toolbarSize = ToolbarSize.SMALL,
        toolbarHeight = animatedToolbarHeight,
        mainCustomContent = {
            AnimatedContent(targetState = searchModeEnabled) { targetState ->
                if (!targetState) {
                    Text(text = stringResource(R.string.app_name))
                } else {
                    DynamicIsland(
                        islandState = IslandState.SearchState(),
                        searchedAppRequest = searchQuery,
                        onSearchAppRequestChanged = { newQuery ->
                            uiEvent.invoke(UiEvent.OnUpdateSearchQuery(newQuery = newQuery))
                        },
                        onUpdateKeyboardVisible = {},
                        onUpdateMicrophoneEnabled = {},
                        isMicrophoneEnabled = false
                    )
                }
            }
        },
        titleColor = if (!darkTheme) Color.Black else Color.White,
        navigationIcon = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(40.dp)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    modifier = Modifier.matchParentSize(),
                    targetState = searchModeEnabled
                ) { targetState ->
                    if (!targetState) {
                        Icon(
                            modifier = Modifier.fillMaxSize(),
                            painter = painterResource(if (!darkTheme) R.drawable.ic_the_lab_central else R.drawable.ic_the_lab_central_white),
                            contentDescription = null
                        )
                    } else {
                        IconButton(
                            modifier = Modifier.matchParentSize(),
                            onClick = { uiEvent.invoke(UiEvent.OnUpdateSearchMode(!searchModeEnabled)) }) {
                            Icon(
                                modifier = Modifier.fillMaxSize(),
                                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        },
        actions = {
            AnimatedVisibility(visible = !searchModeEnabled) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { uiEvent.invoke(UiEvent.OnUpdateSearchMode(enabled = true)) }) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "search_icon"
                        )
                    }

                    IconButton(
                        onClick = {
                            scope.launch {
                                if (!isBottomSheetExpended) {
                                    bottomSheetState.show()
                                } else {
                                    bottomSheetState.hide()
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = "info_icon"
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun CentralScreenPortrait(
    theme: AppTheme,
    darkTheme: Boolean,
    windowSize: WindowSizeClass,
    centralUiState: UiState<List<PackageApp>>,
    searchModeEnabled: Boolean,
    searchQuery: String,
    isHideBottomSheetContentRequested: Boolean,
    uiEvent: (UiEvent) -> Unit
) {
    val scope = rememberCoroutineScope()
    val bottomSheetState: SheetState = rememberModalBottomSheetState()
    val bottomSheetScaffoldState =
        rememberBottomSheetScaffoldState(bottomSheetState = bottomSheetState)

    val isBottomSheetExpended by derivedStateOf { bottomSheetScaffoldState.bottomSheetState.isVisible }

    val animatedSheetPeekHeight by animateDpAsState(
        targetValue = if (!isBottomSheetExpended) 0.dp else 200.dp,
        label = "animated_bottom_sheet_peek_height"
    )

    var isTooltipVisible by remember { mutableStateOf(false) }

    LaunchedEffect(isHideBottomSheetContentRequested) {
        Timber.d("Recomposition | LaunchedEffect | is hide BottomSheet content requested: $isHideBottomSheetContentRequested")

        if (isHideBottomSheetContentRequested) {
            if (bottomSheetScaffoldState.bottomSheetState.isVisible) {
                bottomSheetScaffoldState.bottomSheetState.hide()
            }
        }
    }

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        BottomSheetScaffold(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding(),
            topBar = {
                CentralToolbar(
                    theme = theme,
                    darkTheme = darkTheme,
                    scope = scope,
                    bottomSheetState = bottomSheetState,
                    isBottomSheetExpended = isBottomSheetExpended,
                    searchModeEnabled = searchModeEnabled,
                    searchQuery = searchQuery,
                    uiEvent = uiEvent
                )
            },
            scaffoldState = bottomSheetScaffoldState,
            sheetContent = {
                BottomSheetContent(
                    theme = theme,
                    darkTheme = darkTheme,
                    onTooltipVisibilityChanged = { isVisible ->
                        Timber.d("Recomposition | onTooltipVisibilityChanged.isTooltipVisible: $isVisible")
                        isTooltipVisible = isVisible
                    },
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
                ) { centralTargetState ->

                    when (centralTargetState) {
                        is UiState.Loading -> {
                            LabLoader(modifier = Modifier.size(56.dp))
                        }

                        is UiState.Success -> {
                            val isPackageFound by derivedStateOf {
                                if (searchQuery.trim().isEmpty()) true else centralTargetState
                                    .data
                                    .any { packageApp ->
                                        packageApp.name.contains(searchQuery, true) ||
                                                packageApp.packageName.contains(
                                                    searchQuery,
                                                    true
                                                )
                                    }
                            }
                            val lazyGridState = rememberLazyGridState()

                            AnimatedContent(targetState = isPackageFound) { targetState ->
                                if (!isPackageFound) {
                                    Text(text = "No package found for \"$searchQuery\"")
                                } else {
                                    LazyVerticalGrid(
                                        modifier = Modifier.matchParentSize(),
                                        state = lazyGridState,
                                        columns = GridCells.Fixed(2)
                                    ) {
                                        itemsIndexed(
                                            items = if (searchQuery.trim().isEmpty()) {
                                                centralTargetState.data
                                            } else {
                                                centralTargetState.data.filter { packageApp ->
                                                    packageApp.name.contains(searchQuery, true) ||
                                                            packageApp.packageName.contains(
                                                                searchQuery,
                                                                true
                                                            )
                                                }
                                            }
                                        ) { _, item ->
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
                        }

                        is UiState.Error -> {
                            Text(text = centralTargetState.error)
                        }

                        else -> {
                            Box(modifier = Modifier)
                        }
                    }
                }

                AnimatedVisibility(
                    modifier = Modifier.fillMaxSize(),
                    visible = isTooltipVisible
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable(enabled = false, onClick = { null })
                            .background(color = Color.Black.copy(alpha = 0.5f))
                            .zIndex(2f)
                    ) {}
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
                        IconButton(onClick = { }) {
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
    searchModeEnabled: Boolean,
    searchQuery: String,
    isHideBottomSheetContentRequested: Boolean,
    uiEvent: (UiEvent) -> Unit
) {
    val configuration = LocalConfiguration.current

    if (Configuration.ORIENTATION_PORTRAIT == configuration.orientation) {
        CentralScreenPortrait(
            theme = theme,
            darkTheme = darkTheme,
            windowSize = windowSize ?: WindowSizeClass.COMPACT,
            centralUiState = centralUiState,
            searchModeEnabled = searchModeEnabled,
            searchQuery = searchQuery,
            isHideBottomSheetContentRequested = isHideBottomSheetContentRequested,
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
@Preview(showBackground = true)
@Composable
private fun PreviewCentralToolbarSearchModeDisabled(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {

    val bottomSheetState: SheetState = rememberModalBottomSheetState()

    TheLabTheme(theme = appTheme) {
        CentralToolbar(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            scope = rememberCoroutineScope(),
            bottomSheetState = bottomSheetState,
            searchModeEnabled = false,
            searchQuery = "",
            isBottomSheetExpended = bottomSheetState.isVisible,
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewCentralToolbarSearchModeEnabled(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {

    val bottomSheetState: SheetState = rememberModalBottomSheetState()

    TheLabTheme(theme = appTheme) {
        CentralToolbar(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            scope = rememberCoroutineScope(),
            bottomSheetState = bottomSheetState,
            searchModeEnabled = true,
            searchQuery = "The",
            isBottomSheetExpended = bottomSheetState.isVisible,
        ) {}
    }
}

@DevicePreviewsPhoneOnly
@Composable
private fun PreviewCentralScreen(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    val context = LocalContext.current

    val filteredList : List<PackageApp> = listOf(
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

    TheLabTheme(theme = appTheme) {
        CentralScreen(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            windowSize = WindowSizeClass.COMPACT,
            centralUiState = UiState.Success(filteredList),
            searchModeEnabled = true,
            isHideBottomSheetContentRequested = false,
            searchQuery = "The",
        ) {}
    }
}