package com.riders.thelab.core.common.storage

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import android.os.storage.StorageVolume
import androidx.annotation.AnyRes
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.os.EnvironmentCompat
import com.riders.thelab.core.common.R
import okhttp3.ResponseBody
import okhttp3.internal.io.FileSystem
import okio.buffer
import okio.source
import org.xml.sax.InputSource
import timber.log.Timber
import java.io.BufferedInputStream
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.zip.GZIPInputStream

/**
 * Utils for I/O operations.
 */
object LabFileManager {

    /**
     * Reads file and returns a String.
     *
     * @param file the file to read
     * @return the string with file content or null
     */
    fun tryReadFile(file: File): String? {
        try {
            FileSystem.SYSTEM.source(file).buffer().use {
                return it.readUtf8()
            }
        } catch (exception: IOException) {
            //ignored exception
            return null
        }
    }

    /**
     * Reads InputStream and returns a String. It will close stream after usage.
     *
     * @param stream the stream to read
     * @return the string with file content or null
     */
    fun tryReadFile(stream: InputStream): String? {
        try {
            stream.source().buffer().use { source -> return source.readUtf8() }
        } catch (exception: IOException) {
            //ignored exception
            return null
        }
    }

    /**
     * Reads file and returns a String.
     *
     * @param file the file to read
     * @return the string content
     */
    @Throws(IOException::class)
    fun readFile(file: File): String {
        FileSystem.SYSTEM.source(file).buffer().use { source -> return source.readUtf8() }
    }

    /**
     * Reads InputStream and returns a String. It will close stream after usage.
     *
     * @param stream the stream to read
     * @return the string content
     */
    @Throws(IOException::class)
    fun readFile(stream: InputStream): String {
        stream.source().buffer().use { source -> return source.readUtf8() }
    }

    /**
     * Ref : https://stackoverflow.com/questions/10469407/android-decompress-downloaded-xml-gz-file
     * https://stackoverflow.com/questions/216894/get-an-outputstream-into-a-string
     *
     * @param responseBody
     * @return
     */
    fun unzipGzip(responseBody: ResponseBody): String? {
        Timber.d("unzipGzip()")
        var json: String? = null
        try {
            Timber.d("Build stream objects with json file received ...")
            val compressedInputStream: InputStream = GZIPInputStream(responseBody.byteStream())
            val inputSource = InputSource(compressedInputStream)
            val inputStream: InputStream = BufferedInputStream(inputSource.byteStream)
            Timber.d("Build buffer and string builder ...")
            val sb = StringBuilder()
            try {
                inputStream.source().use { fileSource ->
                    fileSource.buffer().use { bufferedSource ->
                        while (true) {
                            val line = bufferedSource.readUtf8Line() ?: break
                            sb.append(line)
                        }
                        Timber.d("File read, build json target variable ...")
                        json = sb.toString()
                        return json
                    }
                }
            } catch (e: Exception) {
                Timber.e(e)
                e.printStackTrace()
            } finally {
                compressedInputStream.close()
                inputStream.close()
            }
        } catch (e: Exception) {
            Timber.e(e)
            e.printStackTrace()
        }
        return json
    }


    fun getDrawableURI(@NonNull context: Context, @AnyRes drawableId: Int): String {
        val imageUri: Uri = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE +
                    "://" + context.resources.getResourcePackageName(drawableId)
                    + '/' + context.resources.getResourceTypeName(drawableId)
                    + '/' + context.resources.getResourceEntryName(drawableId)
        )

        return imageUri.toString()
    }


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
            val storageVolumes: List<StorageVolume>? = storageManager.storageVolumes

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

    private fun getRootOfInnerSdCardFolder(context: Context, inputFile: File): String {
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

    private fun getFileFromAssets(context: Context, filename: String): InputStream? = context
        .assets
        .runCatching {
            open(filename)
        }
        .onFailure {
            it.printStackTrace()
            Timber.e("Failed to open asset file. Message : ${it.message}")
        }
        .getOrNull()

    fun getFileInputStreamFromAssets(context: Context, filename: String): InputStream? =
        try {
            context.assets.open(filename)
        } catch (exception: Exception) {
            exception.printStackTrace()
            null
        }

    fun getJsonFileContentFromAssets(context: Context, filename: String): String? =
        getFileFromAssets(context, filename)?.let { stream ->
            stream.bufferedReader().use { it.readText() }
        }
}