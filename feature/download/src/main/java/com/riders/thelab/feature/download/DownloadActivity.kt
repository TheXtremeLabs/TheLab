package com.riders.thelab.feature.download

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.IntentFilter
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.data.local.model.Permission
import com.riders.thelab.core.permissions.PermissionManager
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class DownloadActivity : BaseComponentActivity() {

    private val viewModel: DownloadViewModel by viewModels<DownloadViewModel>()

    // The receiver only will be triggered if was registered from your application using
    private var mDownloadReceiver: DownloadReceiver? = null

    private var once: Boolean = false


    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        StrictMode.setVmPolicy(
            VmPolicy.Builder()
                .detectLeakedClosableObjects()
                .penaltyLog()
                .build()
        )

        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                Timber.d("repeatOnLifecycle(Lifecycle.State.CREATED)")

                setContent {
                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            DownloaderContent(
                                downloadListState = viewModel.downloadList,
                                buttonText = if (!viewModel.isDownloadStarted) "Launch Download" else "Stop download",
                                isButtonEnabled = viewModel.canDownload,
                                onButtonClicked = {
                                    if (!viewModel.isDownloadStarted) {
                                        viewModel.updateIsDownloadStarted(true)
                                        viewModel.getSpeedTest()
                                    } else {
                                        viewModel.updateIsDownloadStarted(false)
                                        viewModel.cancelDownloads()
                                    }
                                }
                            )
                        }
                    }
                }
            }

            repeatOnLifecycle(Lifecycle.State.STARTED) {
                Timber.d("repeatOnLifecycle(Lifecycle.State.STARTED)")
            }
        }

        checkPermissions()
    }

    override fun onPause() {
        super.onPause()
        Timber.e("onPause() | unregisterReceiver()")
        unregisterReceiver(mDownloadReceiver)
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag", "NewApi")
    override fun onResume() {
        super.onResume()
        Timber.d("onResume() | registerReceiver()")

        // Init Broadcast receiver
        if (null == mDownloadReceiver) {
            mDownloadReceiver = DownloadReceiver()
        }

        runCatching { /*
         * Safer exporting of context-registered receivers
         * To help make runtime receivers safer, Android 13 introduces the ability for your app to specify whether a registered broadcast receiver should be exported and visible to other apps on the device. On previous versions of Android, any app on the device could send an unprotected broadcast to a dynamically-registered receiver unless that receiver was guarded by a signature permission.
         * This exporting configuration is available on apps that do at least one of the following:
         *
         * Use the ContextCompat class from version 1.9.0 or higher of the AndroidX Core library.
         *
         * Target Android 13 or higher.
         */
            val listenToBroadcastsFromOtherApps = false
            val receiverFlags = if (listenToBroadcastsFromOtherApps) {
                ContextCompat.RECEIVER_EXPORTED
            } else {
                ContextCompat.RECEIVER_NOT_EXPORTED
            }

            // The receiver only will be triggered if was registered from your application using
            if (!LabCompatibilityManager.isTiramisu()) {
                registerReceiver(
                    mDownloadReceiver,
                    IntentFilter().apply {
                        addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                        addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
                        addAction(DownloadManager.ACTION_VIEW_DOWNLOADS)
                    })
            } else {
                /*
                 * Safer exporting of context-registered receivers
                 * To help make runtime receivers safer, Android 13 introduces the ability for your app to specify whether a registered broadcast receiver should be exported and visible to other apps on the device. On previous versions of Android, any app on the device could send an unprotected broadcast to a dynamically-registered receiver unless that receiver was guarded by a signature permission.
                 * This exporting configuration is available on apps that do at least one of the following:
                 *
                 * Use the ContextCompat class from version 1.9.0 or higher of the AndroidX Core library.
                 *
                 * Target Android 13 or higher.
                 */
                ContextCompat.registerReceiver(
                    this@DownloadActivity,
                    mDownloadReceiver,
                    IntentFilter().apply {
                        addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                        addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
                        addAction(DownloadManager.ACTION_VIEW_DOWNLOADS)
                    },
                    receiverFlags
                )
            }
        }
            .onFailure { exception: Throwable ->
                exception.printStackTrace()
                Timber.e("error caught with message: ${exception.message}")
            }
            .onSuccess { Timber.d("Success") }

    }

    override fun backPressed() {
        Timber.e("backPressed()")
        finish()
    }


    override fun onDestroy() {
        super.onDestroy()
        mDownloadReceiver = null
    }


    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////
    @SuppressLint("NewApi")
    private fun checkPermissions() = PermissionManager
        .from(this@DownloadActivity)
        .request(if (!LabCompatibilityManager.isAndroid10()) Permission.Storage else if (LabCompatibilityManager.isTiramisu()) Permission.MediaLocationAndroid13 else Permission.MediaLocation)
        .checkPermission {
            if (!it) {
                Timber.e("checkPermissions() | not granted")
                viewModel.updateCanDownload(false)
            } else {
                Timber.d("checkPermissions() | granted")
                viewModel.updateCanDownload(true)
            }
        }


    fun downloadFinishedForId(downloadId: Long) {
        Timber.d("downloadFinishedForId() | downloadId: $downloadId")
        viewModel.updateFinishedDownloadItem(downloadId)
        once = true
    }

    fun resetOnce(): Unit {
        Timber.e("resetOnce()")
        this.once = false
    }
}