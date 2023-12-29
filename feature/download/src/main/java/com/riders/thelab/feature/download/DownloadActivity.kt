package com.riders.thelab.feature.download

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.IntentFilter
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
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.data.local.model.Permission
import com.riders.thelab.core.permissions.PermissionManager
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import kotlinx.coroutines.launch
import timber.log.Timber

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
                            DownloaderContent(viewModel)
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

        // Init Broadcast receiver
        if (null == mDownloadReceiver) {
            mDownloadReceiver = DownloadReceiver()
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
            registerReceiver(
                mDownloadReceiver,
                IntentFilter().apply {
                    addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
                    addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED)
                    addAction(DownloadManager.ACTION_VIEW_DOWNLOADS)
                },
                RECEIVER_EXPORTED
            )
        }
    }

    override fun backPressed() {
        Timber.e("backPressed()")
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
    private fun checkPermissions() = PermissionManager
        .from(this@DownloadActivity)
        .request(Permission.Storage)
        .checkPermission {
            Timber.d("checkPermissions() | granted: $it")
            if (!it) {
                viewModel.updateCanDownload(false)
            } else {
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