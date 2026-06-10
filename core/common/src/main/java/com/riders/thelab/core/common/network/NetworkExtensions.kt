package com.riders.thelab.core.common.network

import android.content.Context
import com.riders.thelab.core.common.storage.FileManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import okio.use
import timber.log.Timber
import java.io.File

fun ResponseBody.saveFile(destinationPath: String) {
    val destinationFile = File(destinationPath).also {
        Timber.d("saveFile() | Attempt to save file in ${it.absolutePath}")
    }

    byteStream().use { inputStream ->
        destinationFile.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    }
}

fun ResponseBody.saveFileAsFlow(context: Context, destinationPath: String): Flow<DownloadState> {
    return flow {
        emit(DownloadState.Downloading(0))
        val destinationFile = File(destinationPath).also {
            Timber.d("saveFileAsFlow() | Attempt to save file in ${it.absolutePath}")
        }

        FileManager.getInstance(context).apply {
            getOrCreateFolder(destinationFile.parent.toString())
            getOrCreateFile(destinationPath)
        }

        try {
            byteStream().use { inputStream ->
                destinationFile.outputStream().use { outputStream ->
                    val totalBytes = contentLength()
                    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                    var progressBytes = 0L
                    var bytes = inputStream.read(buffer)
                    while (bytes >= 0) {
                        outputStream.write(buffer, 0, bytes)
                        progressBytes += bytes
                        bytes = inputStream.read(buffer)
                        emit(DownloadState.Downloading(((progressBytes * 100) / totalBytes).toInt()))
                    }
                }
            }
            emit(DownloadState.Finished)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(DownloadState.Failed(e.message, e))
        }
    }
        .flowOn(Dispatchers.IO)
        .distinctUntilChanged()
}