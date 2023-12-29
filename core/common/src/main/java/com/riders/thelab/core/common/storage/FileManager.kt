package com.riders.thelab.core.common.storage

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class FileManager private constructor(private val context: Context) {

    private val dataPath =
        if (!LabCompatibilityManager.isAndroid10()) "${Environment.getExternalStorageDirectory()}/SAM_SHARE/DISTRI" else Environment.getDataDirectory().absolutePath
    private val mDistriDataFolder =
        if (!LabCompatibilityManager.isAndroid10()) "$dataPath/" else "$dataPath/data/${context.packageName}/files/"

    fun getDataDirectoryPath(): String =
        if (!LabCompatibilityManager.isAndroid10()) "${Environment.getExternalStorageDirectory()}/SAM_SHARE/${context.packageName}" else "${Environment.getDataDirectory().absolutePath}/data/${context.packageName}/files"

    fun createRootFolder(): Boolean = try {
        val file = File("${getDataDirectoryPath()}/empty")

        if (!file.exists()) {
            Timber.e("File does NOT exist, then create it")
            file.parentFile?.mkdirs()
            file.createNewFile()
        }

        if (file.exists()) {
            Timber.d("file created: ${file.absolutePath}")
        }
        true
    } catch (exception: Exception) {
        exception.printStackTrace()
        Timber.e("Exception caught: ${exception.message}")
        when (exception) {
            is FileAlreadyExistsException -> true
            is IOException -> false
            else -> false
        }
    }

    /**
     * Creates a screenshot folder for numeric stamp
     * Either in SAM_SHARE/DISTRI (below Android 8) or in app's data files folder (Android 9 or above)
     *
     * @return true if created, false if not
     */
    fun createDistriScreenshotFolder(): Boolean = try {
        val file = File("${getDataDirectoryPath()}/empty")

        if (!file.exists()) {
            Timber.e("File does NOT exist, then create it")
            file.parentFile?.mkdirs()
            file.createNewFile()
        }

        if (file.exists()) {
            Timber.d("file created: ${file.absolutePath}")
        }
        true
    } catch (exception: Exception) {
        exception.printStackTrace()
        Timber.e("Exception caught: ${exception.message}")
        when (exception) {
            is FileAlreadyExistsException -> true
            is IOException -> false
            else -> false
        }
    }


    /**
     * Creates a file using the data passed in parameters such as app ID, filename and the data
     */
    fun createFile(fileName: String, data: ByteArray) {
        Timber.d("createFile() | fileName: $fileName, data: $data")

        var fo: OutputStream? = null

        try {
            val file = File(getDataDirectoryPath(), fileName)

            if (!file.exists()) {
                Timber.e("File does NOT exist, then create it")
                file.parentFile?.mkdirs()
                file.createNewFile()
            }

            fo = FileOutputStream(file)
            fo.write(data)
            Timber.d("file created: $file")

            file.absolutePath
        } catch (exception: Exception) {
            exception.printStackTrace()
            Timber.e(exception.message)
        } finally {
            fo?.flush()
            fo?.close()
        }
    }

    /**
     * Creates a file using the data passed in parameters such as app ID, filename and the data
     */
    fun createFile(fileName: String): File {
        Timber.d("createFile() | fileName: $fileName")

        return try {
            val file = File(getDataDirectoryPath(), fileName)

            if (!file.exists()) {
                Timber.e("File does NOT exist, then create it")
                file.parentFile?.mkdirs()
                file.createNewFile()
                Timber.d("file created: ${file.absolutePath}")
                file
            } else {
                Timber.d("File ${file.absolutePath} already exists, then return it")
                file
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            Timber.e(exception.message)
            File("")
        }
    }

    /**
     * Creates a file using the data passed in parameters such as app ID, filename and the data
     */
    fun createFileInDownloads(fileName: String): File {
        Timber.d("createFile() | fileName: $fileName")

        return try {
            val file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                fileName
            )

            if (!file.exists()) {
                Timber.e("File does NOT exist, then create it")
                file.parentFile?.mkdirs()
                file.createNewFile()
                Timber.d("file created: ${file.absolutePath}")
                file
            } else {
                Timber.d("File ${file.absolutePath} already exists, then return it")
                file.parentFile?.mkdirs()
                val duplicateFile = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    fileName + "_1"
                )
                duplicateFile.createNewFile()
                Timber.d("file created: ${duplicateFile.absolutePath}")
                duplicateFile
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            Timber.e(exception.message)
            File("")
        }
    }


    /**
     * Creates a file using the data passed in parameters such as app ID, filename and the data
     */
    fun createFile(fileName: String, bitmap: Bitmap) {
        Timber.d("createFile() | fileName: $fileName, bitmap: $bitmap")
        val quality = 85
        var fo: OutputStream? = null

        try {
            val file = File(getDataDirectoryPath(), fileName)
            fo = FileOutputStream(file)
            fo.use {
                // Finally writing the bitmap to the output stream that we opened
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, it)
                fo.flush()
                fo.close()
                Timber.d("Captured View and saved to Gallery")
            }

            Timber.d("file created: $file")

            file.absolutePath
        } catch (exception: Exception) {
            exception.printStackTrace()
            Timber.e(exception.message)
        } finally {
            fo?.flush()
            fo?.close()
        }
    }


    companion object {
        @SuppressLint("StaticFieldLeak")
        private var mInstance: FileManager? = null

        fun getInstance(context: Context): FileManager {
            if (null == mInstance) {
                mInstance = FileManager(context)
            }
            return mInstance as FileManager
        }
    }
}