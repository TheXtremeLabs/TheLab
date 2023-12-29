package com.riders.thelab.feature.download

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.ui.compose.utils.findActivity
import com.riders.thelab.core.ui.utils.UIManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class DownloadReceiver : BroadcastReceiver() {
    @Inject
    lateinit var repository: IRepository

    // The receiver only will be triggered if was registered from your application using
    private lateinit var mDownloadManager: DownloadManager


    override fun onReceive(context: Context?, intent: Intent?) {
        if (null == context) {
            Timber.e("Context is null here. Leave method")
            return
        }

        Timber.d("onReceive() | intent: ${intent?.action}")

        // mDownloadManager = context.getSystemService(DownloadManager::class.java)
        mDownloadManager = repository.getDownloadManager(context)

        if (intent?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {

            //Fetching the download id received with the broadcast
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            val query = id.let { DownloadManager.Query().setFilterById(it) } ?: -1

            // Checking if the received broadcast is for our enqueued download by matching download id
            if (-1L == id) {
                Timber.e("Error while downloading file")
            } else {
                Timber.d("Downloaded file with id: $id completed.")

                runCatching {
                    (context.findActivity() as DownloadActivity).runOnUiThread {
                        (context.findActivity() as DownloadActivity)
                            .downloadFinishedForId(id)

                        UIManager.showToast(context, "onReceive() | Download Completed")
                    }
                }
                    .onFailure { Timber.e("runCatching | onFailure | error caught with message: ${it.message}") }
                    .onSuccess { }
            }
        }
    }
}