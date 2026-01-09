package com.riders.thelab.call.ui.call

import android.os.Bundle
import android.telecom.Call
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.riders.thelab.call.data.local.compose.CallState
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class LabCallActivity : ComponentActivity() {

    private val mViewModel: LabCallViewModel by viewModels<LabCallViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val phoneNumber = intent.data?.schemeSpecificPart.also {
            Timber.d("CallActivity | Phone number: $it")
        }

        setContent {

            val theme: AppTheme by mViewModel
                .theme
                .collectAsStateWithLifecycle()
            val isDarkTheme: Boolean? by mViewModel
                .isDarkMode
                .collectAsStateWithLifecycle()

            val callState by mViewModel.callState.collectAsStateWithLifecycle()
            val elapsedSeconds by mViewModel
                .elapsedSeconds
                .collectAsStateWithLifecycle()

            TheLabTheme(theme = theme, darkTheme = isDarkTheme ?: isSystemInDarkTheme()) {
                CallScreen(
                    contactName = CallState.Outgoing(
                        phoneNumber.toString()
                    ).number,
                    contactAvatar = null,
                    callState = if (Call.STATE_ACTIVE == callState) "active" else "Calling...",
                    elapsedSeconds = elapsedSeconds,
                    uiEvent = { event ->
                        when (event) {
                            is UiEvent.OnHangUp -> {
                                phoneNumber?.let { mViewModel.hangUp(it) }
                                finish()
                            }

                            else -> {
                                mViewModel.onEvent(event)
                            }
                        }
                    })
            }
        }


        lifecycleScope.launch {
            mViewModel.callState.collect { state ->
                when (state) {
                    Call.STATE_ACTIVE -> startTimer()
                    Call.STATE_DISCONNECTED -> finish()
                }
            }
        }
    }

    private fun startTimer() {
        lifecycleScope.launch {
            while (isActive) {
                val seconds = mViewModel.elapsedTime()
                Timber.d("startTimer() | Elapsed seconds: $seconds")
                delay(1000)
            }
        }
    }
}