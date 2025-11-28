package com.riders.thelab.feature.nfc

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.nfc.NFCUiState
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.base.observeLifecycleEvents
import com.riders.thelab.core.ui.compose.data.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class NFCActivity : BaseComponentActivity() {

    private val mViewModel: NFCViewModel by viewModels<NFCViewModel>()

    private val nfcSettingsResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->

        val code = result.resultCode
        val extras = result.data?.extras

        Timber.d("nfcSettingsResultLauncher | code: $code | extras: $extras")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.initWeakReference(activity = this@NFCActivity)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {
                    // Register lifecycle events
                    mViewModel.observeLifecycleEvents(lifecycle = LocalLifecycleOwner.current.lifecycle)

                    val theme: AppTheme by mViewModel.uiRepository
                        .getTheme()
                        .collectAsStateWithLifecycle(initialValue = AppTheme.Default)
                    val isDarkTheme: Boolean by mViewModel.uiRepository
                        .isThemeDarkMode()
                        .collectAsStateWithLifecycle(initialValue = false)

                    val nfcUiState: NFCUiState by mViewModel.mLabNFCManager?.nfcState?.collectAsStateWithLifecycle()!!

                    NFCScreen()
                }
            }
        }

    }

    override fun onPause() {
        super.onPause()
        mViewModel.mLabNFCManager?.disableNfcForegroundDispatch()
    }

    override fun onResume() {
        super.onResume()

        if (true == mViewModel.mLabNFCManager?.isNfcSupported() && false == mViewModel.mLabNFCManager?.isNfcEnabled()) {
            mViewModel.mLabNFCManager
                ?.createNfcSettingsIntent()
                ?.let { settingIntent -> nfcSettingsResultLauncher.launch(settingIntent) }
            return
        }

        mViewModel.mLabNFCManager?.enableNfcForegroundDispatch()
    }

    override fun backPressed() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}