package com.riders.thelab.core.common.storage

import android.os.Environment
import java.io.File
import java.util.Scanner

/**
 * Created by Michaël on 01/03/2018.
 */
object ExternalStorage {

    private const val SD_CARD = "sdCard"
    private const val EXTERNAL_SD_CARD = "externalSdCard"

    /**
     * @return True if the external storage is available. False otherwise.
     */
    /*fun isAvailable(): Boolean {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state || Environment.MEDIA_MOUNTED_READ_ONLY == state
        ) {
            return true
        }
        return false
    }*/
    fun isAvailable(): Boolean = when (Environment.getExternalStorageState()) {
        Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY -> true
        else -> false
    }


    fun getSdCardPath(): String = Environment.getExternalStorageDirectory().path + "/"


    /**
     * @return True if the external storage is writable. False otherwise.
     */
    fun isWritable(): Boolean =
        Environment.getExternalStorageState().run { Environment.MEDIA_MOUNTED == this }

    /**
     * @return A map of all storage locations available
     */
    fun getAllStorageLocations(): Map<String, File> {
        val map: MutableMap<String, File> = mutableMapOf()

        val mMounts: ArrayList<String> = mutableListOf<String>() as ArrayList<String>
        val mVold: ArrayList<String> = mutableListOf<String>() as ArrayList<String>
        mMounts.add("/mnt/sdcard")
        mVold.add("/mnt/sdcard")

        try {
            val mountFile = File("/proc/mounts")
            if (mountFile.exists()) {
                val scanner = Scanner(mountFile)
                while (scanner.hasNext()) {
                    val line: String = scanner.nextLine()
                    if (line.startsWith("/dev/block/vold/")) {
                        val lineElements: List<String> = line.split(" ")
                        val element: String = lineElements[1]

                        // don't add the default mount path
                        // it's already in the list.
                        if (element != "/mnt/sdcard")
                            mMounts.add(element)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            val voldFile = File("/system/etc/vold.fstab")
            if (voldFile.exists()) {
                val scanner = Scanner(voldFile)
                while (scanner.hasNext()) {
                    val line: String = scanner.nextLine()
                    if (line.startsWith("dev_mount")) {
                        val lineElements: List<String> = line.split(" ")
                        var element: String = lineElements[2]

                        if (element.contains(":"))
                            element = element.substring(0, element.indexOf(":"))
                        if (element != "/mnt/sdcard")
                            mVold.add(element)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        for (i in 0 until mMounts.size) {
            val mount = mMounts[i]
            if (!mVold.contains(mount))
                mMounts.removeAt(i - 1)
        }
        mVold.clear()

        val mountHash: ArrayList<String> = mutableListOf<String>() as ArrayList<String>

        for (mount: String in mMounts) {
            val root = File(mount)
            if (root.exists() && root.isDirectory && root.canWrite()) {

                val list: Array<out File>? = root.listFiles()
                var hash = "["

                if (list != null) {
                    for (f: File in list) {
                        hash += "${f.name.hashCode()} : ${f.length()}, "
                    }
                }
                hash += "]"
                if (!mountHash.contains(hash)) {
                    var key: String = SD_CARD + "_" + map.size
                    if (map.isEmpty()) {
                        key = SD_CARD
                    } else if (map.size == 1) {
                        key = EXTERNAL_SD_CARD
                    }
                    mountHash.add(hash)
                    map[key] = root
                }
            }
        }

        mMounts.clear()

        if (map.isEmpty()) {
            map[SD_CARD] = Environment.getExternalStorageDirectory()
        }
        return map
    }
}

