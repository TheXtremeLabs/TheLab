package com.riders.thelab.feature.youtube.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.common.network.LabNetworkManager
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.utils.UIManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class YoutubeActivity : BaseComponentActivity() {

    private val mViewModel: YoutubeViewModel by viewModels<YoutubeViewModel>()

    private val mNetworkManager: LabNetworkManager by lazy {
        LabNetworkManager(context = this@YoutubeActivity, lifecycle = this.lifecycle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.observeNetworkState(mNetworkManager)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {

                    val uiState by mViewModel.youtubeUiState.collectAsStateWithLifecycle()

                    TheLabTheme(mViewModel.isDarkMode) {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            YoutubeContent(uiState = uiState)
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        //Test the internet's connection
        if (!mViewModel.hasInternetConnection) {
            Timber.e("No Internet connection")
            UIManager.showToast(
                this@YoutubeActivity,
                getString(com.riders.thelab.core.ui.R.string.network_status_disconnected)
            )
        } else {
            mViewModel.fetchVideos()
        }
    }

    override fun backPressed() {
        finish()
    }
}