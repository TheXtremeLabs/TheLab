package com.riders.thelab.feature.nfc

import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
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
import com.riders.thelab.core.ui.utils.UIManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@AndroidEntryPoint
class NFCActivity : BaseComponentActivity(), NfcAdapter.ReaderCallback {

    private val mViewModel: NFCViewModel by viewModels<NFCViewModel>()

    // Tools for Debouncing
    private val uiHandler = Handler(Looper.getMainLooper())
    private var isTagConsideredPresent = false

    // The task that runs if we haven't seen the tag for 500ms
    private val confirmTagLostRunnable = Runnable {
        isTagConsideredPresent = false
        // UIManager.showToast(this@NFCActivity, "Tag Removed")
        handleTagLost()

        mViewModel.stopScanning()
    }
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

                    val theme: AppTheme by mViewModel
                        .theme
                        .collectAsStateWithLifecycle()
                    val isDarkTheme: Boolean? by mViewModel
                        .isDarkMode
                        .collectAsStateWithLifecycle()

                    val nfcUiState: NFCUiState by mViewModel.mLabNFCManager?.nfcState?.collectAsStateWithLifecycle()!!

                    NFCScreen(
                        theme = theme,
                        darkTheme = isDarkTheme ?: isSystemInDarkTheme(),
                        uiState = nfcUiState,
                        isScanning = mViewModel.isScanningNFCTag,
                        isCustomMessageVisible = mViewModel.isCustomMessageVisible,
                        customMessage = mViewModel.customMessage,
                        uiEvent = mViewModel::onEvent
                    )
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        // Disable Foreground Dispatch to avoid interference with other apps
        mViewModel.mLabNFCManager?.disableNfcForegroundDispatch()

        // Cleanup: If app is minimized, assume tag is lost
        uiHandler.removeCallbacks(confirmTagLostRunnable)
    }

    override fun onResume() {
        super.onResume()

        if (true == mViewModel.mLabNFCManager?.isNfcSupported() && false == mViewModel.mLabNFCManager?.isNfcEnabled()) {
            Timber.w("onResume() | NFC is not enabled. Should launch NFC Settings")
            return
        }

        // Enable Foreground Dispatch when the app is active
        mViewModel.mLabNFCManager?.enableNfcForegroundDispatch()
    }

    override fun backPressed() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.cancelScanningJob()
    }

    fun launchNFCSettingsIntent() {
        mViewModel.mLabNFCManager
            ?.createNfcSettingsIntent()
            ?.let { settingIntent -> nfcSettingsResultLauncher.launch(settingIntent) }
    }

    /**
     * Called by system when a tag is found.
     * If the user shifted their hand, this might fire again quickly.
     */
    override fun onTagDiscovered(detectedTag: Tag?) {
        // CANCEL the "Lost" timer immediately
        uiHandler.removeCallbacks(confirmTagLostRunnable)

        if (!mViewModel.isScanningNFCTag) {
            Timber.w("onTagDiscovered() | NFC is not scanning. Make sure that you've launched the scanning process before trying to read the tag")

            lifecycleScope.launch {
                /*withTimeout(
                    timeout = 3_000.toDuration(DurationUnit.MILLISECONDS),
                    onTimeoutComplete = { mViewModel.updateIsCustomMessageVisible(false) }
                ) {
                    Timber.d("onTagDiscovered() | withTimeout(3_000)")

                }*/
                mViewModel.updateIsCustomMessageVisible(
                    true,
                    "Make sure that you've launched the scanning process before trying to read the tag"
                )

                delay(5_000.toDuration(DurationUnit.MILLISECONDS))

                mViewModel.updateIsCustomMessageVisible(false)
            }
        } else {

            Timber.d("onTagDiscovered() | detected tag: $detectedTag")

            // Update UI only if this is a "new" session
            if (!isTagConsideredPresent) {
                isTagConsideredPresent = true
                runOnUiThread {
                    val text = "Tag Detected & Stable"
                    Timber.d("onTagDiscovered() | $text")
                    // UIManager.showToast(this@NFCActivity, text)
                }
            }

            detectedTag?.let { tag ->
                val result: String? = mViewModel.mLabNFCManager?.onTagDiscovered(tag)?.getOrNull()
                UIManager.showToast(this@NFCActivity, result.toString())

                // Start the presence watcher
                startPresenceWatcher(tag)
            }
        }
    }

    private fun startPresenceWatcher(tag: Tag) {
        Timber.d("startPresenceWatcher() | tag: $tag")

        // Use a specific Tag Technology. IsoDep is common, but you can use NfcA or others.
        // If you don't know the type, you can iterate through tag.techList
        val isoDep = IsoDep.get(tag)

        try {
            // Connect to the tag
            isoDep?.connect()

            // The Heartbeat Loop
            while (true == isoDep?.isConnected) {
                // Sleep to avoid spamming the CPU
                Thread.sleep(500)

                // Optional: For robust checking, you can try sending a dummy byte here.
                // However, simply checking .isConnected is often enough for basic proximity.
            }

            // If the loop breaks naturally, the tag is disconnected
            throw IOException("Tag disconnected")

        } catch (e: Exception) {
            // Tag Lost Event

            // Connection broke! Start the Grace Period.
            Timber.e("startPresenceWatcher() | Connection broken. Waiting for grace period...")

            // We do NOT update the UI yet. We post the runnable.
            // If onTagDiscovered fires again within 500ms, this runnable is canceled.
            uiHandler.postDelayed(confirmTagLostRunnable, GRACE_PERIOD_MS)
        } finally {
            try {
                isoDep?.close()
            } catch (e: Exception) { /* Ignore close errors */
            }
        }
    }

    private fun handleTagLost() {
        Timber.e("handleTagLost() | Tag was removed")

        isTagConsideredPresent = false
//        runOnUiThread {
//            UIManager.showToast(context = this@NFCActivity, message = "Tag Removed")
//        }
    }

    companion object {
        private const val GRACE_PERIOD_MS = 500L // 0.5 seconds forgiveness

    }
}