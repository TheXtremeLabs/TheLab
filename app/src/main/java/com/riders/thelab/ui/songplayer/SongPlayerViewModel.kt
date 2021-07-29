package com.riders.thelab.ui.songplayer

import android.content.Context
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riders.thelab.core.storage.FileManager
import com.riders.thelab.data.local.model.music.SongModel
import com.riders.thelab.utils.Constants.Companion.SZ_SEPARATOR
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SongPlayerViewModel @Inject constructor() : ViewModel() {

    private val fileList: MutableLiveData<List<SongModel>> = MutableLiveData()

    fun getFiles(): LiveData<List<SongModel>> {
        return fileList
    }

    fun retrieveSongFiles(context: Context) {
        try {
            FileManager.getSdCardPaths(context, true)?.forEach { volumePath ->
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

        val fileList = mutableListOf<SongModel>()

        // Create file with path (can be either a folder or a file)
        val f = File(path)
        // Get list if it's a directory
        val files = f.listFiles() ?: return null

        for (inFile in files) {
            if (inFile.isDirectory && inFile.name == "Music") {
                val musicPath = path + SZ_SEPARATOR + inFile.name
                Timber.e("musicPathname : $musicPath")

                val musicDirectory = File(musicPath)

                val musicFiles = musicDirectory.listFiles() ?: return null
                for (musicFile in musicFiles) {
                    if (musicFile.isDirectory && musicFile.name == "Prod") {
                        val prodPath =
                            musicPath + SZ_SEPARATOR + musicFile.name
                        Timber.e("prodPathname : $prodPath")

                        val prodDirectory = File(prodPath)

                        val prodFiles = prodDirectory.listFiles() ?: return null
                        for (prodFile in prodFiles) {
                            if (prodFile.isDirectory) {
                                // is directory
                                Timber.e("$prodFile is directory")
                            } else if (prodFile.isFile) {
                                Timber.d("$prodFile is A file")

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    // LogManager.logSongObject(prodPath, prodFile.name)
                                }
                                fileList.add(
                                    SongModel(
                                        parseSongName(prodFile.name)
                                        /*if (prodFile.name.endsWith(".mp3", true) || prodFile.name.endsWith(".m4a", true))
                                            prodFile.name.split(".mp3")[0] else ""*/,
                                        prodPath + SZ_SEPARATOR + prodFile.name,
                                        "",
                                        false
                                    )
                                )
                            }
                        }

                    }
                }
            }
        }

        return fileList
    }

    private fun parseSongName(fileName: String): String {

        if (fileName.endsWith(".mp3", true)) {
            return fileName.split(".mp3")[0]
        } else if (fileName.endsWith(".m4a", true)) {
            return fileName.split(".m4a")[0]
        } else {
            return ""
        }
    }
}