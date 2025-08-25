package com.riders.thelab.tv.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.feature.home.HomeActivity
import com.riders.thelab.feature.splashscreen.SplashScreenActivity
import timber.log.Timber

class MainActivity : BaseComponentActivity() {

    private val splashscreenResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            Timber.d("splashscreenResultLauncher | result : $result")

            when (result.resultCode) {
                RESULT_CANCELED -> {
                    val errorMeesage = result.data?.getStringExtra("ERROR_MESSAGE")
                    Timber.e("splashscreenResultLauncher | error caught: $errorMeesage")
                    backPressed()
                }

                RESULT_OK -> navigateToHomeScreen()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate()")

        enableEdgeToEdge()

        splashscreenResultLauncher.launch(
            Intent(this@MainActivity, SplashScreenActivity::class.java)
        )
    }

    override fun backPressed() {
        Timber.e("backPressed()")
        finish()
    }

    fun navigateToHomeScreen() {
        Timber.d("navigateToHomeScreen()")
        Intent(this, HomeActivity::class.java)
            .runCatching {
                startActivity(this)
            }
            .onFailure {
                it.printStackTrace()
                Timber.e("navigateToHomeScreen | onFailure | Error caught: ${it.message} (class : ${it.javaClass.canonicalName})")
            }
            .onSuccess {
                Timber.d("navigateToHomeScreen | onSuccess | Activity launched successfully")
                finish()
            }
    }
}