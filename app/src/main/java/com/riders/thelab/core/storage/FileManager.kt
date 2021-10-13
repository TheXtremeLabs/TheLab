package com.riders.thelab.core.storage

import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.os.storage.StorageVolume
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.os.EnvironmentCompat
import com.riders.thelab.R
import timber.log.Timber
import java.io.File


class FileManager private constructor() {
    companion object {

        /**
         * returns a list of all available sd cards paths, or null if not found.
         *
         * @param includePrimaryExternalStorage set to true if you wish to also include the path of the primary external storage
         */
        fun getSdCardPaths(
            context: Context,
            includePrimaryExternalStorage: Boolean
        ): List<String>? {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val storageManager =
                    context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
                val storageVolumes = storageManager.storageVolumes
                if (!storageVolumes.isNullOrEmpty()) {
                    val primaryVolume = storageManager.primaryStorageVolume
                    val result = ArrayList<String>(storageVolumes.size)
                    for (storageVolume in storageVolumes) {
                        val volumePath = getVolumePath(storageVolume) ?: continue
                        if (storageVolume.uuid == primaryVolume.uuid || storageVolume.isPrimary) {
                            if (includePrimaryExternalStorage)
                                result.add(volumePath)
                            continue
                        }
                        result.add(volumePath)
                    }
                    return if (result.isEmpty()) null else result
                }
            }
            val externalCacheDirs = ContextCompat.getExternalCacheDirs(context)
            if (externalCacheDirs.isEmpty())
                return null
            if (externalCacheDirs.size == 1) {
                if (externalCacheDirs[0] == null)
                    return null
                val storageState = EnvironmentCompat.getStorageState(externalCacheDirs[0])
                if (Environment.MEDIA_MOUNTED != storageState)
                    return null
                if (!includePrimaryExternalStorage && Environment.isExternalStorageEmulated())
                    return null
            }
            val result = ArrayList<String>()
            if (externalCacheDirs[0] != null && (includePrimaryExternalStorage || externalCacheDirs.size == 1))
                result.add(getRootOfInnerSdCardFolder(context, externalCacheDirs[0]))
            for (i in 1 until externalCacheDirs.size) {
                val file = externalCacheDirs[i] ?: continue
                val storageState = EnvironmentCompat.getStorageState(file)
                if (Environment.MEDIA_MOUNTED == storageState)
                    result.add(getRootOfInnerSdCardFolder(context, externalCacheDirs[i]))
            }
            return if (result.isEmpty()) null else result
        }

        fun getRootOfInnerSdCardFolder(context: Context, inputFile: File): String {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val storageManager =
                    context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
                storageManager.getStorageVolume(inputFile)?.let {
                    val result = getVolumePath(it)
                    if (result != null)
                        return result
                }
            }
            var file: File = inputFile
            val totalSpace = file.totalSpace
            while (true) {
                val parentFile = file.parentFile
                if (parentFile == null || parentFile.totalSpace != totalSpace || !parentFile.canRead())
                    return file.absolutePath
                file = parentFile
            }
        }

        @RequiresApi(Build.VERSION_CODES.N)
        fun getVolumePath(storageVolume: StorageVolume): String? {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
                return storageVolume.directory?.absolutePath
            try {
                val storageVolumeClazz = StorageVolume::class.java
                val getPath = storageVolumeClazz.getMethod("getPath")
                return getPath.invoke(storageVolume) as String
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        fun getOutputDirectory(context: Context): File {
            Timber.e("getOutputDirectory()")
            /*val mediaDir = externalMediaDirs.firstOrNull()?.let {
                File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
            }*/

            val mediaDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).let {
                File(it, context.resources.getString(R.string.app_name)).apply { mkdirs() }
            }

            return if (mediaDir != null && mediaDir.exists())
                mediaDir else context.filesDir
        }

    }

    /*fun getMetaDataImage() {
        val mmr = MediaMetadataRetriever()
         mmr.setDataSource(songsList.get(songIndex).get("songPath"))

        // convert the byte array to a bitmap
        val data = mmr.embeddedPicture

        // convert the byte array to a bitmap
        if (data != null) {
            val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            coverart.setImageBitmap(bitmap) //associated cover art in bitmap
        } else {
            coverart.setImageResource(R.drawable.fallback_cover) //any default cover resourse folder
        }

        coverart.setAdjustViewBounds(true)
        coverart.setLayoutParams(LinearLayout.LayoutParams(500, 500))
    }*/
}
