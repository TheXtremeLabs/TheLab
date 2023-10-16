package com.riders.thelab.feature.musicrecognition.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.musicrecognition.ui.acrcloud.ACRCloudActivity
import kotlinx.coroutines.launch
import timber.log.Timber

class MusicRecognitionChooserActivity : BaseComponentActivity() {

    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {
                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            MusicRecognitionContent()
                        }
                    }
                }
            }
        }
    }

    override fun backPressed() {
        finish()
    }

    fun launchACRCloudActivity() = Intent(this, ACRCloudActivity::class.java)
        .apply {
            Timber.d("launchACRCloudActivity()")
        }
        .run {
            startActivity(this)
        }

    /*fun launchShazamActivity() {
        Timber.d("launchShazamActivity()")
    }*/


}