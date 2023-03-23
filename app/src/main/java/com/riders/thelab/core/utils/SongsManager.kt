package com.riders.thelab.core.utils

import android.os.Environment
import java.io.File
import java.io.FilenameFilter

/**
 * Created by Michaël on 26/02/2018.
 */

class SongsManager {

    // SDCard Path
    private val MEDIA_PATH: String = Environment.getExternalStorageDirectory().toString()

    //final String MEDIA_PATH = System.getenv("SECONDARY_STORAGE");
    private val songsList: ArrayList<HashMap<String, String>> = ArrayList()

    /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     * */
    fun getPlayList(): ArrayList<HashMap<String, String>> {

        try {
            val home = File(MEDIA_PATH)

            /*
            Map<String, File> externalLocations = ExternalStorage.getAllStorageLocations();
            File sdCard = externalLocations.get(ExternalStorage.SD_CARD);
            File externalSdCard = externalLocations.get(ExternalStorage.EXTERNAL_SD_CARD);
            */

            if (home.listFiles(FileExtensionFilter())!!.isNotEmpty()) {
                for (file: File in home.listFiles(FileExtensionFilter())!!) {
                    val song: HashMap<String, String> = HashMap()

                    song["songTitle"] = file.name.substring(0, (file.name.length - 4))
                    song["songPath"] = file.path

                    // Adding each song to SongList
                    songsList.add(song)
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        // return songs list array
        return songsList
    }

    fun getPlayList(directoryPath: String): ArrayList<HashMap<String, String>> {

        try {
            val home = File(directoryPath)

            /*
            Map<String, File> externalLocations = ExternalStorage.getAllStorageLocations();
            File sdCard = externalLocations.get(ExternalStorage.SD_CARD);
            File externalSdCard = externalLocations.get(ExternalStorage.EXTERNAL_SD_CARD);
            */

            if (home.listFiles(FileExtensionFilter())!!.isNotEmpty()) {
                for (file: File in home.listFiles(FileExtensionFilter())!!) {
                    val song: HashMap<String, String> = HashMap()

                    song["songTitle"] = file.name.substring(0, (file.name.length - 4))
                    song["songPath"] = file.path

                    // Adding each song to SongList
                    songsList.add(song)
                }
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

        // return songs list array
        return songsList
    }

    /**
     * Class to filter files which are having .mp3 extension
     * */
    class FileExtensionFilter : FilenameFilter {
        override fun accept(dir: File?, name: String?): Boolean {
            return (
                    name?.endsWith(".mp3", true) == true
                            || name?.endsWith(".m4a", true) == true)
        }
    }
}