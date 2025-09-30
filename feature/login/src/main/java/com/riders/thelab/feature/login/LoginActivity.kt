package com.riders.thelab.feature.login

import android.annotation.SuppressLint
import android.content.Intent
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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.GoogleApiAvailability
import com.riders.thelab.core.google.BaseGoogleActivity
import com.riders.thelab.core.google.GooglePlayServicesManager
import com.riders.thelab.core.google.GoogleSignInManager
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.utils.UIManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : BaseGoogleActivity() {

    //TODO : Due to Heroku back-end free services ending,
    // Use of the database to store and log users
    private val mViewModel: LoginViewModel by viewModels()

    private val mGoogleApiAvailability: GoogleApiAvailability by lazy {
        GoogleApiAvailability.getInstance()
    }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.retrieveAppVersion(this@LoginActivity)

        // Start a coroutine in the lifecycle scope
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                computeWindowSizeClasses()

                setContent {
                    val theme: AppTheme by mViewModel.uiRepository
                        .getTheme()
                        .collectAsStateWithLifecycle(initialValue = AppTheme.Default)
                    val isDarkTheme: Boolean by mViewModel.uiRepository
                        .isThemeDarkMode()
                        .collectAsStateWithLifecycle(initialValue = false)

                    val loginUiState by mViewModel.loginUiState.collectAsStateWithLifecycle()
                    val loginFieldState by mViewModel.loginFieldUiState.collectAsStateWithLifecycle()
                    val loginHasError by mViewModel.loginHasError.collectAsStateWithLifecycle()
                    val passwordFieldState by mViewModel.passwordFieldUiState.collectAsStateWithLifecycle()

                    TheLabTheme(theme = theme, darkTheme = isDarkTheme) {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            if (isTv) {
                                LoginContentTV(
                                    theme = theme,
                                    darkTheme = isDarkTheme,
                                    version = mViewModel.version,
                                    loginUiState = loginUiState,
                                    loginFieldState = loginFieldState,
                                    login = mViewModel.login,
                                    loginHasError = loginHasError,
                                    loginHasLocalError = mViewModel.loginHasLocalError,
                                    passwordFieldState = passwordFieldState,
                                    password = mViewModel.password,
                                    isRememberCredentialsChecked = mViewModel.isRememberCredentials,
                                    uiEvent = { event ->
                                        when (event) {
                                            is UiEvent.OnSignUpClicked -> goToActivity(
                                                targetActivity = EXTRA_TARGET_ACTIVITY_SIGN_UP_ACTIVITY
                                            )

                                            is UiEvent.OnGoogleButtonLoginClicked -> authenticateWithGoogle()
                                            is UiEvent.OnLaunchMainActivity -> {
                                                goToActivity(targetActivity = EXTRA_TARGET_ACTIVITY_MAIN_ACTIVITY)
                                                // this@LoginActivity.finish()
                                            }

                                            else -> mViewModel.onEvent(event)
                                        }
                                    })
                            } else {
                                LoginContent(
                                    theme = theme,
                                    darkTheme = isDarkTheme,
                                    version = mViewModel.version,
                                    loginUiState = loginUiState,
                                    loginFieldState = loginFieldState,
                                    login = mViewModel.login,
                                    loginHasError = loginHasError,
                                    loginHasLocalError = mViewModel.loginHasLocalError,
                                    passwordFieldState = passwordFieldState,
                                    password = mViewModel.password,
                                    isRememberCredentialsChecked = mViewModel.isRememberCredentials,
                                    uiEvent = { event ->
                                        when (event) {
                                            is UiEvent.OnSignUpClicked -> goToActivity(
                                                targetActivity = EXTRA_TARGET_ACTIVITY_SIGN_UP_ACTIVITY
                                            )

                                            is UiEvent.OnGoogleButtonLoginClicked -> authenticateWithGoogle()
                                            is UiEvent.OnLaunchMainActivity -> {
                                                goToActivity(targetActivity = EXTRA_TARGET_ACTIVITY_MAIN_ACTIVITY)
                                                // this@LoginActivity.finish()
                                            }

                                            else -> mViewModel.onEvent(event)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                if (mViewModel.hasInternetConnection.value) {
                    UIManager.showConnectionStatusInSnackBar(
                        this@LoginActivity,
                        true
                    )
                }
            }
        }

        mViewModel.isGoogleUserLogged()
    }

    override fun onResume() {
        super.onResume()

        if (!GooglePlayServicesManager.checkPlayServices(
                activity = this@LoginActivity,
                googleApiAvailability = mGoogleApiAvailability
            )
        ) {
            Timber.e("Play services are NOT available")
        }
    }

    override fun backPressed() {
        Timber.e("backPressed()")

        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("onDestroy()")
    }

    /////////////////////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////////////////////
    private fun authenticateWithGoogle() {
        Timber.d("authenticateWithGoogle()")

        val signInManager = GoogleSignInManager.getInstance(this@LoginActivity)

        // Check if the user is already signed in.
        if (signInManager.isUserSignedInLegacy()) {
            signInManager.mLastGoogleAccount?.let {
                UIManager.showToast(
                    this@LoginActivity,
                    "User is already signed in with : ${it.email}"
                )
                goToActivity(targetActivity = EXTRA_TARGET_ACTIVITY_MAIN_ACTIVITY)
            }
        }

        // Start the sign-in process.
        signInManager.signInLegacy()
    }

    fun goToActivity(
        targetActivity: String,
        withError: Boolean = false,
        throwable: Exception? = null
    ) {
        Timber.d("goToMainActivity() | throwable: $throwable")

        if (withError) {
            setResult(
                RESULT_CANCELED,
                Intent().apply {
                    putExtra("ERROR_MESSAGE", throwable?.message)
                }
            )
        } else {
            val intent = Intent()
            intent.putExtra(EXTRA_TARGET_ACTIVITY, targetActivity)
            setResult(RESULT_OK)
        }

        finish()
    }


    /////////////////////////////////////////////////////
    //
    // IMPLEMENTS
    //
    /////////////////////////////////////////////////////
    override fun onConnected(account: GoogleSignInAccount) {
        Timber.d("onConnected()")
        mViewModel.updateGoogleUser(
            account,
            onSuccess = { goToActivity(targetActivity = EXTRA_TARGET_ACTIVITY_MAIN_ACTIVITY) },
            onError = {})
    }

    override fun onDisconnected() {
        Timber.e("onDisconnected()")
    }

    companion object {
        const val EXTRA_TARGET_ACTIVITY: String = "EXTRA_TARGET_ACTIVITY_MAIN"
        const val EXTRA_TARGET_ACTIVITY_MAIN_ACTIVITY: String = "MAIN_ACTIVITY"
        const val EXTRA_TARGET_ACTIVITY_SIGN_UP_ACTIVITY: String = "SIGN_UP_ACTIVITY"
    }
}