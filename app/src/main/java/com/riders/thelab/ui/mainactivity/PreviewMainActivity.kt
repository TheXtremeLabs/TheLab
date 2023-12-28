package com.riders.thelab.ui.mainactivity

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riders.thelab.core.compose.component.DynamicIsland
import com.riders.thelab.core.data.local.model.app.LocalApp
import com.riders.thelab.core.data.local.model.compose.IslandState
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_background
import com.riders.thelab.core.ui.compose.theme.md_theme_light_background
import com.riders.thelab.core.ui.utils.UIManager
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun MainContent(viewModel: MainActivityViewModel) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // Declaring Coroutine scope
    val scope = rememberCoroutineScope()
    val lazyState = rememberLazyGridState()

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isFocus by interactionSource.collectIsFocusedAsState()
    val focusManager: FocusManager = LocalFocusManager.current

    // Declaring a Boolean value to
    // store bottom sheet collapsed state
    val scaffoldState =
        rememberBottomSheetScaffoldState(
            bottomSheetState = BottomSheetState(
                initialValue = BottomSheetValue.Collapsed,
                density = density
            )
        )

    val dynamicIslandUiState by viewModel.dynamicIslandState.collectAsStateWithLifecycle()
    val appList by viewModel.appList.collectAsStateWithLifecycle()
    val whatsNewList: List<LocalApp> by viewModel.whatsNewAppList.collectAsStateWithLifecycle()


    TheLabTheme {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            modifier = Modifier
                .fillMaxSize()
                .background(if (!isSystemInDarkTheme()) md_theme_light_background else md_theme_dark_background),
            //topBar = { TheLabMainTopAppBar(viewModel, focusManager) },
            floatingActionButtonPosition = androidx.compose.material.FabPosition.End,
            floatingActionButton = {
//                androidx.compose.material3.ExtendedFloatingActionButton(
                androidx.compose.material3.FloatingActionButton(
                    modifier = Modifier.padding(bottom = 40.dp),
                    onClick = {
                        scope.launch {
                            scaffoldState.bottomSheetState.apply {
                                if (isCollapsed) expand() else collapse()
                            }
                        }
                    },
                ) {
                    Icon(
                        Icons.Filled.Info,
                        contentDescription = "info_icon"
                    )
                }
            },
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetContent = { BottomSheetContent() },
            sheetPeekHeight = 0.dp,
            sheetElevation = 8.dp,
            // Defaults to true
            sheetGesturesEnabled = false
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyVerticalGrid(
                    state = lazyState,
                    modifier = Modifier
                        .background(if (!isSystemInDarkTheme()) md_theme_light_background else md_theme_dark_background)
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .pointerInput(key1 = "user input") {
                            detectTapGestures(
                                onPress = {
                                    UIManager.showToast(context, "Press Detected")

                                    if (viewModel.keyboardVisible) {
                                        // 1. Update value
                                        viewModel.updateKeyboardVisible(false)
                                        // 2. Clear focus
                                        focusManager.clearFocus(true)
                                        // 3. hide keyboard
                                        keyboardController?.hide()

                                        if (dynamicIslandUiState is IslandState.SearchState) {
                                            viewModel.updateDynamicIslandState(IslandState.DefaultState)
                                            viewModel.updateDynamicIslandVisible(false)
                                        }
                                    }
                                }
                            )
                        },
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    columns = GridCells.Fixed(2)
                ) {
                    // TODO : Remove top screen padding when no dynamic island is displayed
                    item(span = {
                        // Replace "maxCurrentLineSpan" with the number of spans this item should take.
                        // Use "maxCurrentLineSpan" if you want to take full width.
                        GridItemSpan(maxCurrentLineSpan)
                    }) {
                        Header(
                            viewModel = viewModel,
                            whatsNewList = whatsNewList,
                            isKeyboardVisible = viewModel.keyboardVisible,
                            pagerAutoScroll = viewModel.isPagerAutoScroll,
                            onSearchClicked = {
                                viewModel.updateKeyboardVisible(true)
                                viewModel.updateDynamicIslandState(IslandState.SearchState())
                            },
                            onSettingsClicked = { viewModel.launchSettings() }
                        )
                    }

                    items(
                        items = viewModel.filteredList,
                        key = { it.id }
                    ) { appItem ->
                        App(item = appItem)
                    }

                    if (viewModel.filteredList.isEmpty()) {
                        item(span = {
                            // Replace "maxCurrentLineSpan" with the number of spans this item should take.
                            // Use "maxCurrentLineSpan" if you want to take full width.
                            GridItemSpan(maxCurrentLineSpan)
                        }) {
                            NoItemFound(viewModel.searchedAppRequest)
                        }
                    }
                }

                // Dynamic Island
                AnimatedVisibility(
                    modifier = Modifier.align(Alignment.TopCenter),
                    visible = viewModel.isDynamicIslandVisible,
                    enter = slideInVertically {
                        // Slide in from 40 dp from the top.
                        with(density) { -40.dp.roundToPx() }
                    } + fadeIn(
                        // Fade in with the initial alpha of 0.3f.
                        initialAlpha = 0.3f
                    ),
                    exit = slideOutVertically {
                        // Slide in from 40 dp from the top.
                        with(density) { -40.dp.roundToPx() }
                    } + fadeOut()
                ) {
                    DynamicIsland(viewModel, dynamicIslandUiState)
                }
            }
        }
    }

    LaunchedEffect(interactionSource) {
        if (isPressed) {
            Timber.d("Pressed")
        }
        if (isFocus) {
            Timber.d("Focused")
        }
    }

    LaunchedEffect(dynamicIslandUiState) {
        Timber.d("LaunchedEffect | dynamic island state: ${dynamicIslandUiState.javaClass.simpleName}")
        if ((dynamicIslandUiState is IslandState.SearchState ||
                    dynamicIslandUiState is IslandState.CallState ||
                    dynamicIslandUiState is IslandState.NetworkState.Available ||
                    dynamicIslandUiState is IslandState.NetworkState.Lost ||
                    dynamicIslandUiState is IslandState.NetworkState.Unavailable)
            && viewModel.keyboardVisible
        ) {
            viewModel.updateDynamicIslandVisible(true)
        } else {
            viewModel.updateDynamicIslandVisible(false)

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
private fun PreviewMainContent() {
    TheLabTheme {
        MainContent(viewModel = MainActivityViewModel())
    }
}