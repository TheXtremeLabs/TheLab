@file:OptIn(ExperimentalMaterialApi::class)

package com.riders.thelab.ui.mainactivity

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainContent(viewModel: MainActivityViewModel) {
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
            topBar = { TheLabMainTopAppBar(viewModel) },
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
                    /*icon = {
                        androidx.compose.material3.Icon(
                            Icons.Filled.Info,
                            contentDescription = "Favorite"
                        )
                    },*/
                    //text = { androidx.compose.material3.Text("Like") }
                ) {
                    Icon(
                        Icons.Filled.Info,
                        contentDescription = "Favorite"
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
            ) {
                item {

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

                    Text(
                        "What's new",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Thin
                    )

                    Spacer(modifier = Modifier.size(16.dp))

                    WhatsNewList(viewModel = viewModel)

                    Spacer(modifier = Modifier.size(16.dp))

                    Text(
                        text = stringResource(id = R.string.app_list_placeholder),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.W600
                    )

                    Text(
                        text = stringResource(id = R.string.app_list_detail_placeholder),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Thin
                    )

                    Spacer(modifier = Modifier.size(16.dp))

                    // AppList(viewModel = viewModel)
                }

                items(viewModel.appList.value) { appItem ->
                    App(item = appItem)
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
@OptIn(ExperimentalMaterialApi::class)
@DevicePreviews
@Composable
private fun PreviewMainContent() {

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
            topBar = { TheLabMainTopAppBar(viewModel) },
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
                    .background(if (!isSystemInDarkTheme()) md_theme_light_background else md_theme_dark_background)
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Whats New List
                item {

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

                items(viewModel.appList.value) { appItem ->
                    App(item = appItem)
                }

            }
        }
    }
}