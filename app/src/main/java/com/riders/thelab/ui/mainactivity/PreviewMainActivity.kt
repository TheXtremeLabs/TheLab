@file:OptIn(ExperimentalMaterialApi::class)

package com.riders.thelab.ui.mainactivity

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.riders.thelab.R
import com.riders.thelab.core.compose.annotation.DevicePreviews
import com.riders.thelab.core.compose.ui.theme.TheLabTheme
import com.riders.thelab.core.compose.ui.theme.md_theme_dark_background
import com.riders.thelab.core.compose.ui.theme.md_theme_light_background
import com.riders.thelab.core.compose.utils.keyboardAsState
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun MainContent(viewModel: MainActivityViewModel) {

    val context = LocalContext.current

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isFocus by interactionSource.collectIsFocusedAsState()
    val focusManager: FocusManager = LocalFocusManager.current

    val keyboardController = LocalSoftwareKeyboardController.current
    val isKeyboardOpen by keyboardAsState()

    val lazyState = rememberLazyListState()

    // Declaring a Boolean value to
    // store bottom sheet collapsed state
    val scaffoldState =
        rememberBottomSheetScaffoldState(bottomSheetState = BottomSheetState(initialValue = BottomSheetValue.Collapsed))

    // Declaring Coroutine scope
    val scope = rememberCoroutineScope()

    TheLabTheme {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            modifier = Modifier.fillMaxSize(),
            topBar = { TheLabMainTopAppBar(viewModel, focusManager) },
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
        ) { contentPadding ->
            LazyColumn(
                state = lazyState,
                modifier = Modifier
                    .background(if (!isSystemInDarkTheme()) md_theme_light_background else md_theme_dark_background)
                    .fillMaxSize()
                    .padding(contentPadding)
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

                                if (viewModel.keyboardVisible.value){
                                    // 1. Update value
                                    viewModel.updateKeyboardVisible(false)
                                    // 2. Clear focus
                                    focusManager.clearFocus(true)
                                    // 3. hide keyboard
                                    keyboardController?.hide()
                                }
                            }
                        )
                    }
            ) {
                item {

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Welcome to ")
                        Spacer(modifier = Modifier.size(8.dp))
                        Image(
                            modifier = Modifier.height(16.dp),
                            painter = painterResource(id = R.drawable.ic_lab_6_the),
                            contentDescription = "the_icon",
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Image(
                            modifier = Modifier.height(16.dp),
                            painter = painterResource(id = R.drawable.ic_lab_6_lab),
                            contentDescription = "lab_icon",
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Image(
                            modifier = Modifier.height(16.dp),
                            painter = painterResource(id = R.drawable.ic_the_lab_12_logo_white),
                            contentDescription = "lab_twelve",
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }

                    Spacer(modifier = Modifier.size(24.dp))

                    AnimatedVisibility(visible = !viewModel.keyboardVisible.value) {
                        Text(
                            "What's new",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Thin
                        )

                        Spacer(modifier = Modifier.size(16.dp))

                        WhatsNewList(viewModel = viewModel)
                    }

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

                    // AppList(viewModel = viewModel)
                }

                items(items = viewModel.appList.value.filter {
                    (it.appName != null && it.appName?.contains(
                        viewModel.searchedAppRequest.value, ignoreCase = true
                    )!!)
                            || (it.appTitle != null && it.appTitle?.contains(
                        viewModel.searchedAppRequest.value, ignoreCase = true
                    )!!)
                }, key = { it.id }) { appItem ->
                    App(item = appItem)
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
}


///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@OptIn(ExperimentalMaterialApi::class)
@DevicePreviews
@Composable
private fun PreviewMainContent() {

    val focusManager: FocusManager = LocalFocusManager.current
    val viewModel: MainActivityViewModel = hiltViewModel()

    val lazyState = rememberLazyListState()

    // Declaring a Boolean value to
    // store bottom sheet collapsed state
    val scaffoldState =
        rememberBottomSheetScaffoldState(bottomSheetState = BottomSheetState(initialValue = BottomSheetValue.Collapsed))

    // Declaring Coroutine scope
    val scope = rememberCoroutineScope()

    TheLabTheme {
        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            modifier = Modifier.fillMaxSize(),
            topBar = { TheLabMainTopAppBar(viewModel, focusManager) },
            floatingActionButtonPosition = androidx.compose.material.FabPosition.End,
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    modifier = Modifier.padding(bottom = 96.dp),
                    onClick = {
                        scope.launch {
                            scaffoldState.bottomSheetState.apply {
                                if (isCollapsed) expand() else collapse()
                            }
                        }
                    },
                    icon = {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = "Favorite"
                        )
                    },
                    text = { Text("Like") }
                )
            },
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetContent = {
                BottomSheetContent()
            },
            sheetPeekHeight = 0.dp,
            sheetElevation = 8.dp,
            // Defaults to true
            sheetGesturesEnabled = false
        ) { contentPadding ->
            LazyColumn(
                state = lazyState,
                modifier = Modifier
                    .background(if (!isSystemInDarkTheme()) md_theme_light_background else MaterialTheme.colorScheme.background)
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Whats New List
                item {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Welcome to ", color = Color.White)
                            Spacer(modifier = Modifier.size(8.dp))
                            Image(
                                modifier = Modifier.height(16.dp),
                                painter = painterResource(id = R.drawable.ic_lab_6_the),
                                contentDescription = "the_icon",
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Image(
                                modifier = Modifier.height(16.dp),
                                painter = painterResource(id = R.drawable.ic_lab_6_lab),
                                contentDescription = "lab_icon",
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Image(
                                modifier = Modifier.height(16.dp),
                                painter = painterResource(id = R.drawable.ic_the_lab_12_logo_white),
                                contentDescription = "lab_twelve",
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                        }

                        Spacer(modifier = Modifier.size(24.dp))

                        Text(
                            "What's new",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Thin
                        )

                        Spacer(modifier = Modifier.size(32.dp))

                        WhatsNewList(viewModel = viewModel)

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

                        //AppList(viewModel = viewModel)
                    }
                }

                items(viewModel.appList.value) { appItem ->
                    App(item = appItem)
                }
            }
        }
    }
}