package com.riders.thelab.ui.download

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riders.thelab.data.IRepository
import com.riders.thelab.data.local.model.Download
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    private val downloadStatus: MutableLiveData<Download> = MutableLiveData()
    private val percentData: MutableLiveData<Int> = MutableLiveData()

    fun getDownloadStatus(): LiveData<Download> {
        return downloadStatus
    }

    fun getPercentData(): LiveData<Int> {
        return percentData
    }


    suspend fun downloadFile() {
        Timber.d("downloadFile()")
        repository.getBulkDownload()
            .collect { download ->

                withContext(Dispatchers.Main) {
                    when (download) {
                        is Download.Started -> {
                            downloadStatus.value = download
                        }

                        is Download.Progress -> {
                            // update ui with progress
                            Timber.d("percent : ${download.percent}%")

                            percentData.value = download.percent
                        }

                        is Download.Done -> {
                            downloadStatus.value = download
                        }

                        is Download.Error -> {
                            downloadStatus.value = download
                        }

                        is Download.Finished -> {
                            // update ui with file
                            Timber.d("download status : ${download.file.toString()}")
                        }
                    }
                }
            }
    }
}