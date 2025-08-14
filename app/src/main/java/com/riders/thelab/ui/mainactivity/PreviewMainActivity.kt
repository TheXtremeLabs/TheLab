package com.riders.thelab.ui.mainactivity

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.VectorDrawable
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.compose.component.DynamicIsland
import com.riders.thelab.core.data.local.model.app.App
import com.riders.thelab.core.data.local.model.app.LocalApp
import com.riders.thelab.core.data.local.model.compose.IslandState
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.NoItemFound
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.IslandStatePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.utils.UIManager
import com.riders.thelab.utils.AppBuilderUtils
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun MainContent(
    theme: AppTheme,
    darkTheme: Boolean,
    hasInternetConnection: Boolean,
    dynamicIslandUiState: IslandState,
    isDynamicIslandVisible: Boolean,
    searchedAppRequest: String,
    onSearchAppRequestChanged: (String) -> Unit,
    filteredList: List<App>,
    whatsNewList: List<LocalApp>,
    isMicrophoneEnabled: Boolean,
    onUpdateMicrophoneEnabled: (Boolean) -> Unit,
    isKeyboardVisible: Boolean,
    onUpdateKeyboardVisible: (Boolean) -> Unit,
    isPagerAutoScroll: Boolean,
    uiEvent: (UiEvent) -> Unit
) {
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

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            floatingActionButtonPosition = androidx.compose.material.FabPosition.End,
            floatingActionButton = {
                androidx.compose.material3.FloatingActionButton(
                    modifier = Modifier.padding(bottom = 40.dp),
                    onClick = {
                        scope.launch {
                            scaffoldState.bottomSheetState.apply {
                                if (isCollapsed) expand() else collapse()
                            }
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Icon(
                        Icons.Filled.Info,
                        contentDescription = "info_icon"
                    )
                }
            },
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetContent = { BottomSheetContent(theme = theme, darkTheme = darkTheme) },
            sheetPeekHeight = 0.dp,
            sheetElevation = 8.dp,
            // Defaults to true
            sheetGesturesEnabled = false
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyVerticalGrid(
                    state = lazyState,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .pointerInput(key1 = "user input") {
                            detectTapGestures(
                                onPress = {
                                    UIManager.showToast(context, "Press Detected")

                                    if (isKeyboardVisible) {
                                        // 1. Update value
                                        onUpdateKeyboardVisible(false)
                                        // 2. Clear focus
                                        focusManager.clearFocus(true)
                                        // 3. hide keyboard
                                        keyboardController?.hide()

                                        if (dynamicIslandUiState is IslandState.SearchState) {
                                            uiEvent.invoke(
                                                UiEvent.OnUpdateDynamicIslandState(
                                                    IslandState.DefaultState
                                                )
                                            )
                                            uiEvent.invoke(
                                                UiEvent.OnUpdateDynamicIslandVisible(
                                                    false
                                                )
                                            )
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
                            theme = theme,
                            darkTheme = darkTheme,
                            whatsNewList = whatsNewList,
                            isKeyboardVisible = isKeyboardVisible,
                            pagerAutoScroll = isPagerAutoScroll,
                            onSearchClicked = { uiEvent.invoke(UiEvent.OnSearchClicked) }
                        ) { uiEvent.invoke(UiEvent.OnSettingsClicked) }
                    }

                    items(
                        items = filteredList,
                        key = { it.id }
                    ) { appItem ->
                        AppItem(
                            theme = theme, darkTheme = darkTheme,
                            item = appItem,
                            onAppItemClick = { uiEvent.invoke(UiEvent.OnAppItemClicked(it)) }
                        )
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }

                    if (searchedAppRequest.trim() != "" && filteredList.isEmpty()) {
                        item(span = {
                            // Replace "maxCurrentLineSpan" with the number of spans this item should take.
                            // Use "maxCurrentLineSpan" if you want to take full width.
                            GridItemSpan(maxCurrentLineSpan)
                        }) {
                            NoItemFound(
                                theme = theme,
                                darkTheme = darkTheme,
                                message = "Oops! No item found for value \"$searchedAppRequest\"\nPlease retry..."
                            )
                        }
                    }
                }

                // Dynamic Island
                AnimatedVisibility(
                    modifier = Modifier.align(Alignment.TopCenter),
                    visible = dynamicIslandUiState !is IslandState.DefaultState,
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
                    DynamicIsland(
                        islandState = dynamicIslandUiState,
                        searchedAppRequest = searchedAppRequest,
                        onSearchAppRequestChanged = { uiEvent.invoke(UiEvent.OnUpdateSearchQuery(it)) },
                        isMicrophoneEnabled = isMicrophoneEnabled,
                        onUpdateMicrophoneEnabled = {
                            uiEvent.invoke(
                                UiEvent.OnUpdateMicrophoneEnabled(
                                    it
                                )
                            )
                        },
                        onUpdateKeyboardVisible = onUpdateKeyboardVisible
                    )
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

    LaunchedEffect(hasInternetConnection) {
        if (!hasInternetConnection) {
            uiEvent.invoke(UiEvent.OnUpdateDynamicIslandState(IslandState.NetworkState.Unavailable))
        } else {
            uiEvent.invoke(UiEvent.OnUpdateDynamicIslandState(IslandState.NetworkState.Available))
        }
    }

    /*LaunchedEffect(dynamicIslandUiState) {
        Timber.d("LaunchedEffect | dynamic island state: ${dynamicIslandUiState.javaClass.simpleName}")
        if ((dynamicIslandUiState is IslandState.SearchState ||
                    dynamicIslandUiState is IslandState.CallState ||
                    dynamicIslandUiState is IslandState.NetworkState.Available ||
                    dynamicIslandUiState is IslandState.NetworkState.Lost ||
                    dynamicIslandUiState is IslandState.NetworkState.Unavailable)
            && !isKeyboardVisible
        ) {
            uiEvent.invoke(UiEvent.OnUpdateDynamicIslandVisible(true))
        } else {
            uiEvent.invoke(UiEvent.OnUpdateDynamicIslandVisible(false))
        }
    }*/
}


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewMainContentWithoutInternetConnection(@PreviewParameter(IslandStatePreviewProvider::class) state: IslandState) {
    val context = LocalContext.current
    val appList = AppBuilderUtils.buildActivities(context).take(3).let {
        it.map { localApp ->

            val bitmap: Bitmap? = when (localApp.appDrawableIcon) {
                is BitmapDrawable -> {
                    (localApp.appDrawableIcon as BitmapDrawable).bitmap as Bitmap
                }

                is VectorDrawable -> {
                    App.getBitmap(localApp.appDrawableIcon as VectorDrawable)!!
                }

                else -> {
                    null
                }
            }

            LocalApp(
                localApp.id,
                localApp.appTitle!!,
                localApp.appDescription!!,
                null,
                localApp.appActivity,
                localApp.appDate!!
            ).apply {
                this.bitmap = bitmap
            }
        }
    }

    TheLabTheme(theme = AppTheme.Default) {
        MainContent(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            hasInternetConnection = false,
            dynamicIslandUiState = state,
            isDynamicIslandVisible = true,
            searchedAppRequest = "Colors",
            onSearchAppRequestChanged = { },
            filteredList = appList,
            whatsNewList = appList,
            isMicrophoneEnabled = false,
            onUpdateMicrophoneEnabled = {},
            isKeyboardVisible = true,
            onUpdateKeyboardVisible = {},
            isPagerAutoScroll = true
        ) {}
    }
}

@DevicePreviews
@Composable
private fun PreviewMainContentWithInternetConnection(@PreviewParameter(IslandStatePreviewProvider::class) state: IslandState) {
    val context = LocalContext.current
    val appList = AppBuilderUtils.buildActivities(context).take(3).let {
        it.map { localApp ->

            val bitmap: Bitmap? = when (localApp.appDrawableIcon) {
                is BitmapDrawable -> {
                    (localApp.appDrawableIcon as BitmapDrawable).bitmap as Bitmap
                }

                is VectorDrawable -> {
                    App.getBitmap(localApp.appDrawableIcon as VectorDrawable)!!
                }

                else -> {
                    null
                }
            }

            LocalApp(
                localApp.id,
                localApp.appTitle!!,
                localApp.appDescription!!,
                null,
                localApp.appActivity,
                localApp.appDate!!
            ).apply {
                this.bitmap = bitmap
            }
        }
    }

    TheLabTheme(theme = AppTheme.Default) {
        MainContent(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            hasInternetConnection = true,
            dynamicIslandUiState = state,
            isDynamicIslandVisible = true,
            searchedAppRequest = "Colors",
            onSearchAppRequestChanged = { },
            filteredList = appList,
            whatsNewList = appList,
            isMicrophoneEnabled = false,
            onUpdateMicrophoneEnabled = {},
            isKeyboardVisible = true,
            onUpdateKeyboardVisible = {},
            isPagerAutoScroll = true
        ) {}
    }
}