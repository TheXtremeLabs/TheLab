package com.riders.thelab.feature.download

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.common.storage.FileManager
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.model.compose.Download
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.net.UnknownHostException

@SuppressLint("StaticFieldLeak")
class DownloadViewModel(
    @ApplicationContext private val context: Context,
    private val repository: IRepository
) : BaseViewModel() {

    private var errorCount: Int = 0

    /////////////////////////////////////////
    // Composable states
    /////////////////////////////////////////
    private var _downloadList: MutableStateFlow<List<Download>> = MutableStateFlow(emptyList())
    val downloadList: StateFlow<List<Download>> = _downloadList.asStateFlow()

    var canDownload: Boolean by mutableStateOf(false)
        private set
    var isDownloadStarted: Boolean by mutableStateOf(false)
        private set

    private var message: String by mutableStateOf("")

    private var eventsList by mutableStateOf(emptyList<String>())

    private var downloadIds: List<Long> by mutableStateOf(emptyList())

    fun updateIsDownloadStarted(isStarted: Boolean) {
        this.isDownloadStarted = isStarted
    }

    fun updateMessage(message: String) {
        this.message = message
        updateEventList(message)
    }

    private fun updateEventList(newEvent: String) {
        this.eventsList += newEvent
    }

    private fun updateDownloadList(filename: String) {
        Timber.d("updateDownloadList() | filename: $filename")

        val list = listOf(
            Download(
                id = if (_downloadList.value.isEmpty()) 0 else _downloadList.value.size,
                filename = filename
            )
        )

        this._downloadList.value += list
    }

    private fun updateListWithElementAtIndex(newElement: Download) {
        Timber.d("updateListWithElementAtIndex() | newElement: $newElement")
        val list: MutableList<Download> = _downloadList.value.toMutableList()
        val index = list.indexOf(list.find { it.downloadRefId == newElement.downloadRefId })
        list.removeAt(index)
        list.add(index, newElement)

        this._downloadList.value += list
    }

    private fun updateListWithElementAtIndex(index: Int, newElement: Download) {
        Timber.d("updateListWithElementAtIndex() | index: $index, newElement: $newElement")
        val list: MutableList<Download> = _downloadList.value.toMutableList()
        list.apply {
            removeAt(index)
            add(index, newElement)
        }

        this._downloadList.value += list
    }

    fun updateCanDownload(canDownload: Boolean) {
        Timber.d("updateCanDownload() | canDownload: $canDownload")
        this.canDownload = canDownload
    }

    private fun appendDownloadID(downloadId: Long) {
        Timber.d("appendDownloadID() | id: $downloadId")
        this.downloadIds += downloadId
    }

    private fun updateProgressAtIndex(index: Int, progress: Int) {
        Timber.e("updateProgressAtIndex() | index: $index, progress: $progress")
        updateListWithElementAtIndex(this._downloadList.value[index].copy(progress = progress))
    }


    private fun updateDownloadRefId(reference: Long) = if (_downloadList.value.isEmpty()) {
        Timber.e("updateDownloadRefId() | list is empty")
    } else {
        val index = _downloadList.value.indexOf(_downloadList.value.last())

        updateListWithElementAtIndex(
            index,
            this._downloadList.value[index].copy(downloadRefId = reference)
        )
        Timber.e("updateDownloadRefId() | item updated: ${_downloadList.value[index]}")
    }

    fun updateFinishedDownloadItem(reference: Long) {
        Timber.d("updateFinishedDownloadItem() | reference: $reference")

        if (_downloadList.value.isEmpty()) {
            Timber.e("updateFinishedDownloadItem() | list is empty")
        } else {
            _downloadList.value.find { reference == it.downloadRefId }?.let {
                val index = _downloadList.value.indexOf(it)
                updateListWithElementAtIndex(
                    index,
                    this._downloadList.value[index].copy(progress = 100, isComplete = true)
                )
                Timber.d("updateFinishedDownloadItem() | matched reference: ${it.downloadRefId}")
            }
        }
    }

    ///////////////////////////////
    // Coroutine
    ///////////////////////////////
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e("Coroutine Exception caught with message: ${throwable.message} (${throwable.javaClass})")

            if (throwable is UnknownHostException
                && (throwable.message?.contains("c11.it-local", true) == true ||
                        throwable.message?.contains("bad gateway", true) == true)
                && _downloadList.value.isNotEmpty()
            ) {
                updateListWithElementAtIndex(
                    0,
                    this._downloadList.value[0].copy(
                        isComplete = true,
                        isError = true to "${throwable.message}"
                    )
                )
            }

            errorCount += 1

            if (1 == errorCount) {
                getSpeedTest()
            } else if (2 == errorCount) {
                getIsoFile()
            }
        }


    ///////////////////////////////
    //
    // OVERRIDE
    //
    ///////////////////////////////
    override fun onCleared() {
        super.onCleared()

        Timber.e("onCleared()")
        updateCanDownload(false)
        updateIsDownloadStarted(false)

        repository.cancelDownloads(downloadIds)
    }

    ///////////////////////////////
    //
    // CLASS METHODS
    //
    ///////////////////////////////
    private fun getSpeedTest() {
        Timber.d("getSpeedTest()")

        val speedTestUrl = Constants.URL_SPEED_TEST
        val filename = speedTestUrl.getFilenameFromUrl()
        Timber.d("filename: $filename")

        viewModelScope.launch {
            if (isActive && isDownloadStarted) {
                withContext(Dispatchers.Main) {
                    updateDownloadList(filename = filename)
                }

                delay(1_500)
                launchDownload(speedTestUrl) {
                    getIsoFile()
                }
            }
        }
    }

    private fun getIsoFile() {
        Timber.d("getIsoFile()")

        val isoUrl = Constants.URL_ISO_TEST
        val filename = isoUrl.getFilenameFromUrl()
        Timber.d("filename: $filename")

        viewModelScope.launch {
            if (isActive && isDownloadStarted) {
                withContext(Dispatchers.Main) {
                    updateDownloadList(filename = filename)
                }

                delay(1_500)
                launchDownload(isoUrl) {}
            }
        }
    }

    @SuppressLint("Range")
    fun launchDownload(url: String, filename: String? = null, onFinishedDownload: () -> Unit) {
        Timber.d("launchDownload() | url: $url")

        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            val fileName = filename ?: url.getFilenameFromUrl()
            Timber.d("fileName: $fileName")

            val file: File = FileManager.getInstance(context).createFileInDownloads(fileName)

            // enqueue puts the download request in the queue.
            val reference: Long = repository.downloadFile(url)

            appendDownloadID(reference)
            updateDownloadRefId(reference)

            var finishDownload = false
            var progress: Int

            while (!finishDownload) {
                val cursor: Cursor = repository
                    .getDownloadManager(context)
                    .query(DownloadManager.Query().setFilterById(reference))

                if (cursor.moveToFirst()) {
                    val status =
                        cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))

                    when (status) {
                        DownloadManager.STATUS_FAILED -> {
                            Timber.e("DownloadManager.STATUS_FAILED")

                            _downloadList.value.find { reference == it.downloadRefId }?.let {
                                val index = _downloadList.value.indexOf(it)

                                updateListWithElementAtIndex(
                                    index, _downloadList.value[index].copy(
                                        isComplete = true,
                                        isError = true to "Download failed"
                                    )
                                )

                                Timber.e("item: $this")
                            }

                            finishDownload = true
                        }

                        DownloadManager.STATUS_PAUSED -> {
                            Timber.e("DownloadManager.STATUS_PAUSED")
                        }

                        DownloadManager.STATUS_PENDING -> {
                            Timber.d("DownloadManager.STATUS_PENDING")
                        }

                        DownloadManager.STATUS_RUNNING -> {
                            // Timber.d("DownloadManager.STATUS_RUNNING")
                            val total =
                                cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                            if (total >= 0) {
                                val downloaded =
                                    cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                                progress = (downloaded * 100L / total).toInt()
                                Timber.d("downloaded: $progress %")

                                val progressFloat = (progress / 100).toFloat()

                                _downloadList.value.find { reference == it.downloadRefId }?.let {
                                    val index = _downloadList.value.indexOf(it)
                                    updateProgressAtIndex(index, progress)
                                } ?: run { Timber.e("download item not found with id: $reference") }

                                // if you use downloadmanger in async task, here you can use like this to display progress.
                                // Don't forget to do the division in long to get more digits rather than double.
                                //  publishProgress((int) ((downloaded * 100L) / total));
                            }
                        }

                        DownloadManager.STATUS_SUCCESSFUL -> {
                            Timber.d("DownloadManager.STATUS_SUCCESSFUL")

                            progress = 100
                            // if you use aysnc task
                            // publishProgress(100);
                            finishDownload = true

                            _downloadList.value.find { reference == it.downloadRefId }?.let {
                                val index = _downloadList.value.indexOf(it)

                                updateListWithElementAtIndex(
                                    index,
                                    _downloadList.value[index].copy(
                                        progress = progress,
                                        isComplete = true
                                    )
                                )

                                Timber.d("item: $this")
                            }

                            delay(500L)
                            onFinishedDownload()
                        }
                    }
                }
            }
        }
    }
}