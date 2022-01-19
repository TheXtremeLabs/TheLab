package com.riders.thelab.ui.songplayer

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riders.thelab.core.storage.LabFileManager
import com.riders.thelab.data.local.model.music.SongModel
import com.riders.thelab.utils.Constants.Companion.SZ_SEPARATOR
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SongPlayerViewModel @Inject constructor() : ViewModel() {
    companion object {
        const val MUSIC_PLACEHOLDER = "Music"
        const val PROD_PLACEHOLDER = "Prod"

    }

    private val fileList: MutableLiveData<List<SongModel>> = MutableLiveData()

    fun getFiles(): LiveData<List<SongModel>> {
        return fileList
    }

    fun retrieveSongFiles(context: Context) {
        Timber.d("retrieveSongFiles()")
        try {
            LabFileManager.getSdCardPaths(context, true)?.forEach { volumePath ->
                Timber.e("volumePath:$volumePath")

                if (volumePath.contains("0000")) {
                    fileList.value = getFilesWithPath(volumePath)
                }

            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    private fun getFilesWithPath(path: String): List<SongModel>? {
        Timber.d("getFilesWithPath()")

        val fileList = mutableListOf<SongModel>()

        try {

            // Create file with path (can be either a folder or a file)
            val f = File(path)
            // Get list if it's a directory
            val files = f.listFiles() ?: return null

            // Root Music directory path
            val musicPath =
                files.find { inFile -> inFile.isDirectory && inFile.name == MUSIC_PLACEHOLDER }
                    .toString()
                    ?: ""
            Timber.e("musicPathname : $musicPath")
            if (musicPath.isBlank())
                return null

            val musicDirectory = File(musicPath)
            val musicFiles = musicDirectory.listFiles() ?: return null

            // Root Prod directory path
            val prodPath =
                musicFiles.find { musicFile -> musicFile.isDirectory && musicFile.name == PROD_PLACEHOLDER }
                    .toString()
                    ?: ""
            Timber.e("prodPathname : $prodPath")
            if (prodPath.isBlank())
                return null

            val prodDirectory = File(prodPath)
            val prodFiles = prodDirectory.listFiles() ?: return null

            fileList.addAll(prodFiles.map { prodFile ->
                SongModel(
                    parseSongName(prodFile.name),
                    prodPath + SZ_SEPARATOR + prodFile.name,
                    "",
                    false
                )
            })
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        return fileList
    }

    private fun parseSongName(fileName: String): String {
        return when {
            fileName.endsWith(".mp3", true) -> {
                fileName.split(".mp3")[0]
            }
            fileName.endsWith(".m4a", true) -> {
                fileName.split(".m4a")[0]
            }
            else -> {
                ""
            }
        }
    }
}