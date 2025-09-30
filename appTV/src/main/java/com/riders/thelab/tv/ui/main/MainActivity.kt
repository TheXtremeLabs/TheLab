package com.riders.thelab.tv.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.feature.home.HomeActivity
import com.riders.thelab.feature.login.LoginActivity
import com.riders.thelab.feature.splashscreen.SplashScreenActivity
import timber.log.Timber

class MainActivity : BaseComponentActivity() {

    private val loginResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            Timber.d("loginResultLauncher | result : $result")

            when (result.resultCode) {
                RESULT_CANCELED -> {
                    val errorMessage = result.data?.getStringExtra("ERROR_MESSAGE")
                    Timber.e("splashscreenResultLauncher | error caught: $errorMessage")
                    backPressed()
                }

                RESULT_OK -> {

                    val targetActivityValue: String? =
                        intent.extras?.getString(LoginActivity.EXTRA_TARGET_ACTIVITY)

                    targetActivityValue?.let { target ->
                        if (LoginActivity.EXTRA_TARGET_ACTIVITY_MAIN_ACTIVITY == target) {
                            navigateToHomeScreen()
                        }

                        if (LoginActivity.EXTRA_TARGET_ACTIVITY_SIGN_UP_ACTIVITY == target) {
                            navigateToSignUpScreen()
                        }
                    }
                }
            }
        }

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

                RESULT_OK -> loginResultLauncher.launch(
                    Intent(
                        this@MainActivity,
                        LoginActivity::class.java
                    )
                )
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

    fun navigateToSignUpScreen() {
        Timber.d("navigateToSignUpScreen()")
        Intent(this, LoginActivity::class.java)
            .runCatching {
                startActivity(this)
            }
            .onFailure {
                it.printStackTrace()
                Timber.e("navigateToSignUpScreen | onFailure | Error caught: ${it.message} (class : ${it.javaClass.canonicalName})")
            }
            .onSuccess {
                Timber.d("navigateToSignUpScreen | onSuccess | Activity launched successfully")
                finish()
            }
    }


    companion object {
        const val EXTRA_TARGET_ACTIVITY: String = "EXTRA_TARGET_ACTIVITY_MAIN"
        const val EXTRA_TARGET_ACTIVITY_MAIN_ACTIVITY: String = "MAIN_ACTIVITY"
        const val EXTRA_TARGET_ACTIVITY_SIGN_UP_ACTIVITY: String = "SIGN_UP_ACTIVITY"
    }
}