package com.riders.thelab.feature.videocall.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.riders.thelab.core.ui.compose.annotation.DevicePreviewsPhoneOnly
import com.riders.thelab.core.ui.compose.component.toolbar.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.component.toolbar.ToolbarSize
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.executeOnBackPressed
import com.riders.thelab.core.ui.compose.utils.findActivity
import com.riders.thelab.core.ui.data.local.model.compose.Screen
import com.riders.thelab.feature.videocall.data.CallState
import com.riders.thelab.feature.videocall.data.ConnectState
import com.riders.thelab.feature.videocall.data.VideoCallState
import com.riders.thelab.feature.videocall.ui.video.VideoCallRoute
import com.riders.thelab.feature.videocall.ui.video.VideoCallScreen
import com.riders.thelab.feature.videocall.ui.video.VideoCallUiEvent
import io.getstream.video.android.core.StreamVideoBuilder
import io.getstream.video.android.model.User
import io.getstream.video.android.model.UserType
import timber.log.Timber
import java.util.Locale


@Composable
fun StreamActivityContent(
    theme: AppTheme,
    darkTheme: Boolean,
    uiState: ConnectState,
    videoCallState: VideoCallState,
    uiEvent: (UiEvent) -> Unit,
    onVideoCallUiEvent: (VideoCallUiEvent) -> Unit
) {
    val context = LocalContext.current
    val navHostController: NavHostController = rememberNavController()
    val currentDestination = navHostController.currentDestination

    var currentScreen: Screen? by remember { mutableStateOf(null) }

    navHostController.addOnDestinationChangedListener { controller, destination, arguments ->
        Timber.d("addOnDestinationChangedListener() | destination: ${destination.toString()}, arguments: $arguments")
        if (ConnectRoute.route == destination.route) {
            currentScreen = ConnectRoute
        }
        if (VideoCallRoute.route == destination.route) {
            currentScreen = VideoCallRoute
        }
        Timber.d("addOnDestinationChangedListener() | currentScreen: $currentScreen")
    }

    BackHandler {
        when (currentDestination?.route) {
            VideoCallRoute.route -> {
                navHostController.popBackStack()
            }

            else -> {
                (context.findActivity() as StreamActivity).backPressed()
            }
        }
    }

    TheLabTheme(
        theme = theme,
        darkTheme = darkTheme
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TheLabTopAppBar(
                    theme = theme,
                    darkTheme = darkTheme,
                    modifier = Modifier.fillMaxWidth(),
                    toolbarSize = ToolbarSize.SMALL,
                    title = currentScreen?.route?.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                    },
                    titleColor = if (!darkTheme) Color.Black else Color.White,
                    mainCustomContent = null,
                    navigationIcon = {
                        IconButton(onClick = { context.executeOnBackPressed() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                navController = navHostController,
                startDestination = ConnectRoute
            ) {
                composable<ConnectRoute> {
                    LaunchedEffect(uiState.isConnected) {
                        if (uiState.isConnected) {
                            navHostController.navigate(VideoCallRoute) {
                                popUpTo(ConnectRoute) {
                                    inclusive = true
                                }
                            }
                        }
                    }

                    ConnectScreen(
                        theme = theme,
                        darkTheme = darkTheme,
                        connectState = uiState,
                        uiEvent = uiEvent
                    )
                }

                composable<VideoCallRoute> {
                    LaunchedEffect(videoCallState) {
                        if (CallState.DISCONNECTED == videoCallState.state) {
                            navHostController.navigate(ConnectRoute) {
                                popUpTo(VideoCallRoute) {
                                    inclusive = true
                                }
                            }
                        }
                    }

                    VideoCallScreen(
                        theme = theme,
                        darkTheme = darkTheme,
                        callUiState = videoCallState,
                        uiEvent = onVideoCallUiEvent
                    )
                }
            }
        }
    }
}


@DevicePreviewsPhoneOnly
@Composable
private fun PreviewStreamActivityContent() {
    val context = LocalContext.current
    val streamClient = remember {
        StreamVideoBuilder(
            context = context,
            apiKey = "djmy2f7dpjk8",
            user = User(
                id = "123",
                name = "123",
                type = UserType.Guest
            )
        )
            .build()
    }

    StreamActivityContent(
        theme = AppTheme.Default,
        darkTheme = isSystemInDarkTheme(),
        uiState = ConnectState(
            name = "Mike",
            isConnected = false,
            errorMessage = null
        ),
        videoCallState = VideoCallState(
            call = streamClient.call("default", "123"),
            state = null,
            error = null
        ),
        uiEvent = {}
    ) {}
}