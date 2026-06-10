package com.riders.thelab.core.common.network


sealed class DownloadState {
    data class Downloading(val progress: Int) : DownloadState()

    data object Finished : DownloadState()
    data class Failed(
        val errorMessage: String? = null,
        val throwable: Throwable? = null
    ) : DownloadState()

    data object Idle : DownloadState()
}