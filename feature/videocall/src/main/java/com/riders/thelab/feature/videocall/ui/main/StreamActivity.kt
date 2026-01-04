package com.riders.thelab.feature.videocall.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.base.observeLifecycleEvents
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.feature.videocall.ui.video.VideoCallViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StreamActivity : BaseComponentActivity() {

    private val mViewModel: StreamViewModel by viewModels<StreamViewModel>()
    private val mVideoCallViewModel: VideoCallViewModel by viewModels<VideoCallViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {
                    mViewModel.observeLifecycleEvents(LocalLifecycleOwner.current.lifecycle)

                    val theme: AppTheme by mViewModel
                        .theme
                        .collectAsStateWithLifecycle()
                    val isDarkTheme: Boolean? by mViewModel
                        .isDarkMode
                        .collectAsStateWithLifecycle()

                    val uiState by mViewModel.connectState.collectAsStateWithLifecycle()
                    val videoCallState by mVideoCallViewModel.videoCallState.collectAsStateWithLifecycle()

                    StreamActivityContent(
                        theme = theme,
                        darkTheme = isDarkTheme ?: isSystemInDarkTheme(),
                        uiState = uiState,
                        videoCallState = videoCallState,
                        uiEvent = mViewModel::onEvent,
                        onVideoCallUiEvent = mVideoCallViewModel::onEvent
                    )
                }
            }
        }
    }

    override fun backPressed() {
        finish()
    }

}