package com.riders.thelab.ui.mainactivity

import android.widget.Toast
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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.riders.thelab.R
import com.riders.thelab.core.compose.annotation.DevicePreviews
import com.riders.thelab.core.compose.component.DynamicIsland
import com.riders.thelab.core.compose.ui.theme.TheLabTheme
import com.riders.thelab.core.compose.ui.theme.md_theme_dark_background
import com.riders.thelab.core.compose.ui.theme.md_theme_light_background
import com.riders.thelab.core.compose.utils.keyboardAsState
import com.riders.thelab.data.local.model.compose.IslandState
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class,
    ExperimentalLifecycleComposeApi::class
)
@Composable
fun MainContent(viewModel: MainActivityViewModel) {

    val context = LocalContext.current

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isFocus by interactionSource.collectIsFocusedAsState()
    val focusManager: FocusManager = LocalFocusManager.current

    val keyboardController = LocalSoftwareKeyboardController.current
    val isKeyboardOpen by keyboardAsState()

    // Declaring Coroutine scope
    val scope = rememberCoroutineScope()
    val lazyState = rememberLazyGridState()

    // Declaring a Boolean value to
    // store bottom sheet collapsed state
    val scaffoldState =
        rememberBottomSheetScaffoldState(bottomSheetState = BottomSheetState(initialValue = BottomSheetValue.Collapsed))

    val density = LocalDensity.current
    val isVisible = remember { mutableStateOf(false) }

    val appList by viewModel.appList.collectAsStateWithLifecycle()

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
                    modifier = Modifier.padding(bottom = 96.dp),
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
            sheetContent = {
                BottomSheetContent()
            },
            sheetPeekHeight = 0.dp,
            sheetElevation = 8.dp,
            // Defaults to true
            sheetGesturesEnabled = false
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                LazyVerticalGrid(
                    state = lazyState,
                    modifier = Modifier
                        .background(if (!isSystemInDarkTheme()) md_theme_light_background else md_theme_dark_background)
                        .fillMaxSize()
//                        .padding(contentPadding)
                        .padding(16.dp)
                        .pointerInput(key1 = "user input") {
                            detectTapGestures(
                                onPress = {
                                    Toast
                                        .makeText(
                                            context,
                                            "Press Detected",
                                            Toast.LENGTH_SHORT
                                        )
                                        .show()

                                    if (viewModel.keyboardVisible.value) {
                                        // 1. Update value
                                        viewModel.updateKeyboardVisible(false)
                                        // 2. Clear focus
                                        focusManager.clearFocus(true)
                                        // 3. hide keyboard
                                        keyboardController?.hide()
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

                        Column {

                            Header(viewModel)

                            Spacer(modifier = Modifier.size(32.dp))

                            Text(
                                text = stringResource(id = R.string.app_list_placeholder),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.W600
                            )

                            Spacer(modifier = Modifier.size(8.dp))

                            Text(
                                text = stringResource(id = R.string.app_list_detail_placeholder),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Thin
                            )

                            Spacer(modifier = Modifier.size(16.dp))
                        }
                    }

                    items(items = appList.filter {
                        (it.appName != null && it.appName?.contains(
                            viewModel.searchedAppRequest.value, ignoreCase = true
                        )!!)
                                || (it.appTitle != null && it.appTitle?.contains(
                            viewModel.searchedAppRequest.value, ignoreCase = true
                        )!!)
                    }/*, key = { it.id }*/) { appItem ->
                        App(item = appItem)
                    }
                }

                // Dynamic Island
                AnimatedVisibility(
                    modifier = Modifier.align(Alignment.TopCenter),
                    visible = isVisible.value,
                    enter = slideInVertically() {
                        // Slide in from 40 dp from the top.
                        with(density) { -40.dp.roundToPx() }
                    }
                            + fadeIn(
                        // Fade in with the initial alpha of 0.3f.
                        initialAlpha = 0.3f
                    ),
                    exit = slideOutVertically() {
                        // Slide in from 40 dp from the top.
                        with(density) { -40.dp.roundToPx() }
                    }
                            //+ shrinkVertically()
                            + fadeOut()
                ) {
                    DynamicIsland(viewModel, islandState = viewModel.dynamicIslandState.value)
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

    /*LaunchedEffect(Unit) {
        delay(750L)
        isVisible.value = true
    }*/

    isVisible.value =
        viewModel.dynamicIslandState.value is IslandState.SearchState && viewModel.keyboardVisible.value
}


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewMainContent() {

    val viewModel: MainActivityViewModel = hiltViewModel()

    TheLabTheme {
        MainContent(viewModel = viewModel)
    }
}