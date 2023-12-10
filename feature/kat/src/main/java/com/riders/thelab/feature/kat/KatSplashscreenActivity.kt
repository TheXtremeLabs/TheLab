package com.riders.thelab.feature.kat

import android.annotation.SuppressLint
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
import kotlinx.coroutines.launch
import timber.log.Timber

@SuppressLint("CustomSplashScreen")
class KatSplashscreenActivity : BaseComponentActivity() {
    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate()")

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {
                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            KatSplashScreenContent()
                        }
                    }
                }
            }
        }
    }

    override fun backPressed() {
        Timber.e("backPressed()")
        finish()
    }


    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////
    fun launchKatActivity() = startActivity(Intent(this, KatActivity::class.java))
}