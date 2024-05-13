package com.riders.thelab.ui.login

import android.annotation.SuppressLint
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
import androidx.window.layout.WindowMetricsCalculator
import com.riders.thelab.core.common.network.LabNetworkManager
import com.riders.thelab.core.data.local.model.compose.WindowSizeClass
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.utils.UIManager
import com.riders.thelab.navigator.Navigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : BaseComponentActivity() {

    //TODO : Due to Heroku back-end free services ending,
    // Use of the database to store and log users
    private val mViewModel: LoginViewModel by viewModels()

    private var mLabNetworkManager: LabNetworkManager? = null
    private var mNavigator: Navigator? = null

    private var windowSize: WindowSizeClass? = null

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mLabNetworkManager = LabNetworkManager
            .getInstance(this, lifecycle)
            .also { mViewModel.observeNetworkState(it) }

        mNavigator = Navigator(this)

        mViewModel.retrieveAppVersion(this@LoginActivity)

        // Start a coroutine in the lifecycle scope
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                computeWindowSizeClasses()

                setContent {
                    val loginUiState by mViewModel.loginUiState.collectAsStateWithLifecycle()
                    val loginFieldState by mViewModel.loginFieldUiState.collectAsStateWithLifecycle()
                    val loginHasError by mViewModel.loginHasError.collectAsStateWithLifecycle()
                    val passwordFieldState by mViewModel.passwordFieldUiState.collectAsStateWithLifecycle()

                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            LoginContent(
                                version = mViewModel.version,
                                loginUiState = loginUiState,
                                loginFieldState = loginFieldState,
                                login = mViewModel.login,
                                onUpdateLogin = mViewModel::updateLogin,
                                loginHasLocalError = mViewModel.loginHasLocalError,
                                loginHasError = loginHasError,
                                passwordFieldState = passwordFieldState,
                                password = mViewModel.password,
                                onUpdatePassword = mViewModel::updatePassword,
                                isRememberCredentialsChecked = mViewModel.isRememberCredentials,
                                onUpdateIsRememberCredentials = mViewModel::updateIsRememberCredentials,
                                onLoginButtonClicked = mViewModel::login
                            )
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                if (mViewModel.hasInternetConnection) {
                    UIManager.showConnectionStatusInSnackBar(
                        this@LoginActivity,
                        true
                    )
                }
            }
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
    private fun computeWindowSizeClasses() {
        Timber.d("computeWindowSizeClasses()")

        val metrics = WindowMetricsCalculator
            .getOrCreate()
            .computeCurrentWindowMetrics(this)

        val widthDp = metrics.bounds.width() /
                resources.displayMetrics.density
        val widthWindowSizeClass = when {
            widthDp < 600f -> WindowSizeClass.COMPACT
            widthDp < 840f -> WindowSizeClass.MEDIUM
            else -> WindowSizeClass.EXPANDED
        }

        Timber.i("widthWindowSizeClass: $widthWindowSizeClass")

        val heightDp = metrics.bounds.height() /
                resources.displayMetrics.density
        val heightWindowSizeClass = when {
            heightDp < 480f -> WindowSizeClass.COMPACT
            heightDp < 900f -> WindowSizeClass.MEDIUM
            else -> WindowSizeClass.EXPANDED
        }
        Timber.i("heightWindowSizeClass: $heightWindowSizeClass")

        // Use widthWindowSizeClass and heightWindowSizeClass.
        windowSize = widthWindowSizeClass
    }

    fun getDeviceWindowsSizeClass(): WindowSizeClass {
        Timber.d("getDeviceWindowsSizeClass()")
        return windowSize!!
    }

    fun launchSignUpActivity() = mNavigator?.callSignUpActivity()

    fun launchMainActivity() {
        mNavigator?.callMainActivity()
        finish()
    }
}