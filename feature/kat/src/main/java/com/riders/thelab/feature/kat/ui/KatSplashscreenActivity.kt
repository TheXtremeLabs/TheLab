package com.riders.thelab.feature.kat.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.kat.utils.FirebaseUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class KatSplashscreenActivity : BaseComponentActivity() {

    private val mViewModel: KatSplashscreenViewModel by viewModels<KatSplashscreenViewModel>()

    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate()")

        // Check if activities splash screens are enabled
        if (!mViewModel.isActivitiesSplashEnabled) {
            Timber.e("Activities' splashscreen are disabled. Call launchKatActivity()")
            launchKatActivity()
            return
        }

        // If user already authenticated launch Kat main activity
        if (FirebaseUtils.isLoggedIn()) {
            launchKatActivity()
            return
        }

        // else display splashscreen
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
    fun launchKatActivity() = Intent(this@KatSplashscreenActivity, KatMainActivity::class.java)
        .apply {
            Timber.d("launchKatActivity()")
        }
        .runCatching {
            startActivity(this)
        }
        .onFailure { throwable ->
            Timber.e("launchKatActivity() | onFailure | error caught with message: ${throwable.message} (class: ${throwable.javaClass.simpleName})")
        }
        .onSuccess {
            Timber.d("launchKatActivity() | onSuccess | Activity launched successfully")
        }
}