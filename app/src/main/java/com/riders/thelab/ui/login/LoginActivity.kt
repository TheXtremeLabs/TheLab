package com.riders.thelab.ui.login

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.window.layout.WindowMetricsCalculator
import com.riders.thelab.BuildConfig
import com.riders.thelab.core.common.network.LabNetworkManagerNewAPI
import com.riders.thelab.core.data.local.model.compose.WindowSizeClass
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.utils.UIManager
import com.riders.thelab.navigator.Navigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    //TODO : Due to Heroku back-end free services ending,
    // Use of the database to store and log users
    private val mViewModel: LoginViewModel by viewModels()

    private var mNetworkManager: LabNetworkManagerNewAPI? = null
    private lateinit var navigator: Navigator

    private var isChecked: Boolean = false

    var networkState: Boolean = false

    private var windowSize: WindowSizeClass? = null

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (BuildConfig.DEBUG) {
            mNetworkManager = LabNetworkManagerNewAPI.getInstance(this@LoginActivity)
            val isOnline = mNetworkManager?.isOnline()
            Timber.d("Is app online : $isOnline")
        }

        navigator = Navigator(this)

        mViewModel.retrieveAppVersion(this@LoginActivity)

        initViewModelObservers()

        // Start a coroutine in the lifecycle scope
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                computeWindowSizeClasses()

                setContent {
                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            LoginContent(
                                activity = this@LoginActivity,
                                viewModel = mViewModel,
                                navigator = navigator
                            )
                        }
                    }
                }
            }
        }

        // Start a coroutine in the lifecycle scope
        lifecycleScope.launch {
            // repeatOnLifecycle launches the block in a new coroutine every time the
            // lifecycle is in the STARTED state (or above) and cancels it when it's STOPPED.
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Trigger the flow and start listening for values.
                // Note that this happens when lifecycle is STARTED and stops
                // collecting when the lifecycle is STOPPED
                mViewModel.networkState.collect { state ->
                    // New value received
                    when (state) {
                        is NetworkState.Available -> {
                            networkState = state.available
                            //enableEditTexts()
                            //enableButton()
                            // hideMainActivityButton()
                        }

                        is NetworkState.Disconnected -> {
                            networkState = state.disconnected
                            //disableEditTexts()
                            // disableButton()
                            //showGoToMainActivityButton()
                        }
                    }
                }
            }
        }
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

    private fun initViewModelObservers() {
        Timber.d("initViewModelObservers()")
        /*mViewModel.getDataStoreEmail().observe(this) { binding.inputEmail.setText(it) }
        mViewModel.getDataStorePassword().observe(this) { binding.inputPassword.setText(it) }
        mViewModel.getDataStoreRememberCredentials()
            .observe(this) { binding.cbRememberMe.isChecked = it }*/

        mNetworkManager?.getConnectionState()?.observe(
            this
        ) {
            UIManager.showConnectionStatusInSnackBar(
                this,
                it
            )
        }
    }
}