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
import com.riders.thelab.core.common.bean.FileExtensions
import com.riders.thelab.core.common.utils.Resource
import okhttp3.ResponseBody
import okio.FileSystem
import okio.GzipSource
import okio.Path.Companion.toOkioPath
import okio.buffer
import okio.source
import org.xml.sax.InputSource
import timber.log.Timber
import java.io.*
import java.util.zip.GZIPInputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream

/**
 * Utils for I/O operations.
 */
object LabFileManager {


    private val DEFAULT_BUFFER_SIZE = 4096

    ////////////////////////////////////////////////////////
    // --- CREATE
    ////////////////////////////////////////////////////////
    fun createFolder(
        outputFolderPath: String,
        isExecutable: Boolean = true,
        isOwnerOnly: Boolean = true
    ): Resource<Pair<File?, String?>> = try {
        Timber.d("createFolder() | desired output folder path : $outputFolderPath")

        val file = File(outputFolderPath).also {
            Timber.d("createFolder() | output folder path : ${it.absolutePath}")
        }
        if (!file.exists()) {
            Timber.w("createFolder() | folder doesn't exist")
            val created = file.mkdirs()
            // Setting executable on a folder might not be needed for app's internal storage
            // and can fail if the underlying filesystem doesn't support it well or permissions are tight.
            // val isExecutablePermSet = file.setExecutable(isExecutable, isOwnerOnly)
            if (!created /*|| !isExecutablePermSet*/) {
                Timber.e("createFolder() | folder not created or permissions not set")
                Resource.Error(message = "folder not created or permissions not set")
            } else {
                Timber.i("createFolder() | folder created")
                Resource.Success(file to null)
            }
        } else {
            Timber.w("createFolder() | folder already exists")
            Resource.Success(file to "folder already exists")
        }
    } catch (exception: IOException) {
        exception.printStackTrace()
        Resource.Error(message = exception.message.toString(), throwable = exception)
    } catch (exception: SecurityException) {
        exception.printStackTrace()
        Resource.Error(message = exception.message.toString(), throwable = exception)
    }

    /*fun createFile(outputPath: String): Resource<Pair<File?, String?>> = try {
        val file = File(outputPath)
        if (false == file.parentFile?.exists()) {
            Timber.d("createFile() | parent folder doesn't exist")

            val folderResource = createFolder(file.parentFile!!.absolutePath)

            when (folderResource) {
                is Resource.Success -> {
                    Timber.d("createFile() | parent folder created")

                    if (!file.exists()) {
                        Timber.d("createFile() | file doesn't exist")
                        if (!file.createNewFile()) {
                            Timber.e("createFile() | file not created")
                            Resource.Error(message = "file not created")
                        } else {
                            Resource.Success(file to null)
                        }
                    } else {
                        Resource.Success(file to "file already exists")
                    }
                }

                is Resource.Error -> {
                    Timber.e("createFile() | Error caught with message : ${folderResource.message} (class : ${folderResource.throwable?.javaClass?.canonicalName})")
                    Resource.Error(
                        message = folderResource.message.toString(),
                        throwable = folderResource.throwable
                    )
                }

                is Resource.Loading -> Resource.Loading
            }
        } else {
            if (!file.exists()) {
                Timber.i("createFile() | file doesn't exist")
                if (!file.createNewFile()) {
                    Timber.e("createFile() | file not created")
                    Resource.Error(message = "file not created")
                } else {
                    Resource.Success(file to null)
                }
            } else {
                Resource.Success(file to "file already exists")
            }
        }
    } catch (exception: IOException) {
        exception.printStackTrace()
        Resource.Error(message = exception.message.toString(), throwable = exception)
    }*/

    fun createFile(outputPath: String): Resource<Pair<File?, String?>> = outputPath
        .substring(0, outputPath.lastIndexOf("/"))
        .also { Timber.d("createFile(outputPath = $outputPath) | output folder path : $it") }
        .let { folderPath ->
            when (val folderResource = createFolder(folderPath)) {
                is Resource.Success -> {
                    Timber.d("createFile() | parent folder created")

                    val parentDirFile = folderResource.data?.first
                    if (null == parentDirFile) {
                        Timber.e("createFile() | parent folder is null though success reported")
                        // Path to create the file from includes the filename itself
                        return@let createFileAsResource(File(outputPath))
                    }
                    // Construct file to be created within the parent directory
                    return@let createFileAsResource(
                        File(
                            parentDirFile,
                            outputPath.substring(outputPath.lastIndexOf("/") + 1)
                        )
                    )

                }

                is Resource.Error -> {
                    Timber.e("createFile() | Error caught with message : ${folderResource.message} (class : ${folderResource.throwable?.javaClass?.canonicalName})")
                    return@let Resource.Error(
                        message = folderResource.message.toString(),
                        throwable = folderResource.throwable
                    )
                }
            }
        }

    private fun createFileAsResource(
        file: File
    ): Resource<Pair<File?, String?>> = try {
        if (!file.exists()) {
            Timber.d("createFileAsResource() | file doesn't exist: ${file.absolutePath}")
            if (!file.createNewFile()) {
                Timber.e("createFileAsResource() | file not created: ${file.absolutePath}")
                Resource.Error(message = "file not created: ${file.name}")
            } else {
                Timber.i("createFileAsResource() | file created: ${file.absolutePath}")
                Resource.Success(file to null)
            }
        } else {
            Timber.w("createFileAsResource() | file already exists: ${file.absolutePath}")
            Resource.Success(file to "file already exists")
        }
    } catch (e: IOException) {
        Timber.e(e, "createFileAsResource() | IOException for file ${file.absolutePath}")
        Resource.Error(message = e.message ?: "IOException during file creation", throwable = e)
    }


    /**
     * Writes the response body to disk.
     *
     * @param responseBody The response body to write to disk.
     * @param path The path to write the response body to.
     * @param downloadUrl The URL of the download.
     * @param startNanos The start time of the download in nanoseconds.
     * @param onSuccess A callback that is invoked when the download is successful.
     * @param onError A callback that is invoked when an error occurs during the download.
     */
    /*fun writeResponseBodyToDisk(
        responseBody: Response<ResponseBody>,
        path: String,
        downloadUrl: String,
        startNanos: Long,
        onSuccess: (String?) -> Unit,
        onError: (String?, Throwable?) -> Unit
    ) {
        val stopNanos = System.nanoTime()

        if (200 != responseBody.code()) {
            val e = Throwable(
                "Error code:" + responseBody.code() +
                        " -- Error body" + responseBody.message()
            )
            LogUtils.generateFunctionalLog(
                "BIGQUERY_APPNAME_DOWNLOAD",
                "GET DOWNLOAD BY URL",
                SamApkLogUtils().samApkLogData(downloadUrl),
                LogState.KO.name,
                e.toString(),
                TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos)
            )
            onError(path, e)
            return
        }


        try {
//            val apkDir = File(getAppFilePath(path))
            val apkDir = File(path)
            if (!apkDir.exists()) {
                apkDir.parentFile?.let {
                    if (!it.exists()) {
                        it.mkdirs()
                    }
                    // We need to make it executable to be able to install APKs
                    it.setExecutable(true, false)
                }
            }

            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(DEFAULT_BUFFER_SIZE) // Use defined constant
                var fileSizeDownloaded: Long = 0

                responseBody.body()?.byteStream()?.use {
                    inputStream = it
                    outputStream = FileOutputStream(apkDir)
                    while (true) {
                        val read = inputStream.read(fileReader)
                        if (read == -1) {
                            break
                        }
                        outputStream.write(fileReader, 0, read)
                        fileSizeDownloaded += read.toLong()
                    }
                    outputStream.flush()
                } ?: run {
                    Timber.e("writeResponseBodyToDisk() | response body byte stream is null")
                }

                //setLoggerInfo download ok with succes
                val infosLogger = InfosLogger()
                infosLogger.BIGQUERY_APPNAME_DOWNLOAD = apkDir.name // Use .name for safety

                if (apkDir.length() != 0L) {
                    LogUtils.generateFunctionalLog(
                        "BIGQUERY_APPNAME_DOWNLOAD",
                        " GET DOWNLOAD BY URL",
                        SamApkLogUtils().samApkLogData(downloadUrl),
                        LogState.OK.name,
                        infosLogger
                    )
                    onSuccess.invoke(path)
                    return
                } else {
                    LogUtils.generateFunctionalLog(
                        "BIGQUERY_APPNAME_DOWNLOAD",
                        "GET DOWNLOAD BY URL",
                        SamApkLogUtils().samApkLogData(downloadUrl),
                        LogState.KO.name,
                        "File is empty",
                        TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos)
                    )
                    onError(path, null)
                    return
                }
            } catch (exception: IOException) {
                Timber.e("writeResponseBodyToDisk() | Error caught with message ${exception.message} (class : ${exception.javaClass.canonicalName})")

                LogUtils.generateFunctionalLog(
                    "BIGQUERY_APPNAME_DOWNLOAD",
                    "GET DOWNLOAD BY URL",
                    SamApkLogUtils().samApkLogData(downloadUrl),
                    LogState.KO.name,
                    exception.message ?: "Unknown IO Exception", // Handle null message
                    TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos)
                )
                onError(path, exception)
                return
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (exception: IOException) {
            LogUtils.generateFunctionalLog(
                "BIGQUERY_APPNAME_DOWNLOAD",
                "GET DOWNLOAD BY URL",
                SamApkLogUtils().samApkLogData(downloadUrl),
                LogState.KO.name,
                exception.message ?: "Unknown IO Exception", // Handle null message
                TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos)
            )
            onError(path, exception)
            return
        }
    }*/

    fun zipFiles(files: List<File>, outputPath: String): Resource<File> = runCatching {
        Timber.d("zipFiles() |\nfiles to zip : ${files.joinToString(",") { it.name }}\noutputPath : $outputPath")

        var outputFile: File? = null

        try {
            outputFile = File(outputPath)
            outputFile.parentFile?.let { parentDir ->
                if (!parentDir.exists()) {
                    if (!parentDir.mkdirs()) {
                        Timber.w("zipFiles() | Could not create parent directory: ${parentDir.absolutePath}")
                        // Consider if this failure should be a hard error. If parentDir is essential and couldn't be created,
                        // an immediate error return might be more appropriate.
                        // For now, aligns with previous logic which didn't hard-fail here.
                    }
                    parentDir.setExecutable(true) // Consider implications, generally not needed for app-private dirs
                }
            }
            Timber.i("zipFiles() | output folder : ${outputFile.parentFile?.absolutePath}")
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return@runCatching Resource.Error(
                message = "Unable to prepare output file/directory structure: ${ioException.message}", // Enhanced message
                throwable = ioException
            )
        } catch (securityException: SecurityException) {
            securityException.printStackTrace()
            return@runCatching Resource.Error(
                message = "Security exception while preparing output file/directory: ${securityException.message}", // Enhanced message
                throwable = securityException
            )
        }

        // Defensive check: Ensure outputFile was initialized.
        // If the File(outputPath) constructor itself threw an unexpected error not caught above,
        // or if somehow parentFile logic failed to initialize it (though less likely).
        if (outputFile == null) {
            Timber.e("zipFiles() | Output file reference is null, cannot proceed.")
            return@runCatching Resource.Error(
                message = "Output file reference could not be initialized.",
                throwable = IllegalStateException("Output file reference was not initialized prior to zipping.")
            )
        }

        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)

        Timber.i("zipFiles() | Start zip process for ${outputFile.absolutePath}")

        try {
            // It's crucial that FileOutputStream is inside this try, as it can throw FileNotFoundException
            // (e.g., if outputPath is a directory, or non-writable).
            ZipOutputStream(FileOutputStream(outputFile)).use { zos ->
                if (files.isEmpty()) {
                    Timber.w("zipFiles() | Input file list is empty. Creating an empty zip file at: ${outputFile.absolutePath}")
                    // No need to do anything else, ZipOutputStream will create an empty zip when closed.
                } else {
                    files.forEach { fileToZip ->
                        // Check file existence and type
                        if (!fileToZip.exists() || !fileToZip.isFile) {
                            val errorMessage =
                                "Input file not found or is not a regular file: ${fileToZip.absolutePath}"
                            Timber.e("zipFiles() | $errorMessage -- Aborting and returning Resource.Error.")
                            // This ensures the entire runCatching block completes with this specific error
                            return@runCatching Resource.Error(
                                message = errorMessage,
                                throwable = FileNotFoundException(errorMessage) // Test expects FileNotFoundException
                            )
                        }

                        FileInputStream(fileToZip).use { fis ->
                            val entry = ZipEntry(fileToZip.name)
                            zos.putNextEntry(entry)

                            var length: Int
                            while (fis.read(buffer).also { length = it } > 0) {
                                zos.write(buffer, 0, length)
                            }
                            zos.closeEntry()
                            Timber.d("zipFiles() | Successfully added to zip: ${fileToZip.name}")
                        }
                    }
                }
            }
        } catch (e: FileNotFoundException) {
            // This can be thrown by new FileOutputStream(outputFile) or new FileInputStream(fileToZip)
            // if the check above was somehow bypassed or if outputFile path is invalid (e.g. a directory).
            Timber.e(
                e,
                "zipFiles() | FileNotFoundException during zipping stream setup or file read: ${e.message}"
            )
            return@runCatching Resource.Error(
                "File operation error (likely output path or specific input file issue): ${e.message}",
                e
            )
        } catch (e: IOException) {
            Timber.e(
                e,
                "zipFiles() | IOException during zipping process for ${outputFile.absolutePath}"
            )
            // Consider deleting outputFile if it's partially created and an error occurs.
            // outputFile.delete()
            return@runCatching Resource.Error("IOException during zipping: ${e.message}", e)
        } catch (e: SecurityException) {
            Timber.e(
                e,
                "zipFiles() | SecurityException during zipping process for ${outputFile.absolutePath}"
            )
            // outputFile.delete()
            return@runCatching Resource.Error("SecurityException during zipping: ${e.message}", e)
        }

        // If we successfully processed all files (or an empty list), this is a success.
        Timber.i("zipFiles() | Zip process finished successfully for ${outputFile.absolutePath}")
        Resource.Success(outputFile)
    }
        .onFailure { exception -> // Catches exceptions from runCatching block if not handled by inner try-catches
            // Or if an exception occurs outside the try-catch within runCatching (e.g. in Timber calls if misconfigured)
            exception.printStackTrace()
            Timber.e(
                exception,
                "zipFiles() | Uncaught error in runCatching block: ${exception.message}"
            )
        }
        .getOrElse { exception -> // Fallback if runCatching failed and .onFailure was triggered
            Resource.Error(
                message = "Failed to zip files due to an unexpected error: ${exception.message}", // More generic message
                throwable = exception
            )
        }

    /**
     * Unpacks a ZIP archive to a specified output path.
     * This method attempts to protect against Zip Slip vulnerability.
     *
     * @param archivePath The path to the ZIP archive file.
     * @param outputPath The path to the directory where the archive contents will be extracted.
     * @return `true` if the archive was successfully unpacked, `false` otherwise.
     */
    fun unpackZipFile(archivePath: String, outputPath: String): Boolean {
        Timber.d("unpackZipFile() | Archive: '$archivePath', Output directory: '$outputPath'")

        // Check if archive path matches zip extension (case-insensitive)
        if (!archivePath.endsWith(FileExtensions.ZIP.extensionValue, ignoreCase = true)) {
            Timber.e("unpackZipFile() | Archive path '$archivePath' doesn't have a .zip extension.")
            return false
        }

        val outputDir = File(outputPath)
        try {
            if (!outputDir.exists()) {
                if (!outputDir.mkdirs()) {
                    Timber.e("unpackZipFile() | Failed to create base output directory: '${outputDir.absolutePath}'")
                    return false
                }
            } else if (!outputDir.isDirectory) {
                Timber.e("unpackZipFile() | Output path '${outputDir.absolutePath}' exists but is not a directory.")
                return false
            }
        } catch (e: SecurityException) {
            Timber.e(
                e,
                "unpackZipFile() | SecurityException while setting up output directory: '${outputDir.absolutePath}'"
            )
            return false
        }

        val canonicalOutputDirPath: String
        try {
            canonicalOutputDirPath = outputDir.canonicalPath
        } catch (e: IOException) {
            Timber.e(
                e,
                "unpackZipFile() | Could not get canonical path for output directory: '${outputDir.absolutePath}'"
            )
            return false
        }

        try {
            ZipInputStream(BufferedInputStream(FileInputStream(archivePath))).use { zis ->
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                var zipEntry: ZipEntry?

                while (zis.nextEntry.also { zipEntry = it } != null) {
                    val currentEntry = zipEntry!! // Safe due to loop condition
                    val entryName = currentEntry.name
                    val destinationFile = File(outputDir, entryName)

                    // Security check: Path Traversal (Zip Slip)
                    val canonicalDestinationPath = destinationFile.canonicalPath
                    if (!canonicalDestinationPath.startsWith(canonicalOutputDirPath + File.separator) && canonicalDestinationPath != canonicalOutputDirPath) {
                        Timber.e("unpackZipFile() | Path traversal attempt for entry: '$entryName'. Entry resolves to '$canonicalDestinationPath', which is outside of '$canonicalOutputDirPath'.")
                        zis.closeEntry() // Close entry before returning
                        return false // Critical security failure
                    }

                    if (currentEntry.isDirectory) {
                        if (!destinationFile.mkdirs() && !destinationFile.isDirectory) {
                            Timber.w("unpackZipFile() | Could not create directory for entry: '$entryName' at '${destinationFile.absolutePath}', or it's not a directory.")
                            // This might not be a fatal error if no files are in this specific directory.
                        }
                    } else { // Is a file
                        // Ensure parent directory exists for the file
                        destinationFile.parentFile?.let { parentDir ->
                            if (!parentDir.exists()) {
                                if (!parentDir.mkdirs() && !parentDir.isDirectory) {
                                    Timber.e("unpackZipFile() | Failed to create parent directory: '${parentDir.absolutePath}' for file '$entryName'.")
                                    zis.closeEntry()
                                    return false // If parent dir can't be made, file can't be written.
                                }
                            } else if (!parentDir.isDirectory) {
                                Timber.e("unpackZipFile() | Parent path '${parentDir.absolutePath}' for file '$entryName' exists but is not a directory.")
                                zis.closeEntry()
                                return false
                            }
                        }

                        try {
                            FileOutputStream(destinationFile).use { fos ->
                                var len: Int
                                while (zis.read(buffer).also { len = it } > 0) {
                                    fos.write(buffer, 0, len)
                                }
                            }
                        } catch (e: IOException) {
                            Timber.e(
                                e,
                                "unpackZipFile() | IOException while writing file: '${destinationFile.absolutePath}' for entry '$entryName'."
                            )
                            zis.closeEntry()
                            // Consider deleting partially written file if required: destinationFile.delete()
                            return false // Error during file write
                        }
                    }
                    zis.closeEntry() // Close current entry before processing the next one
                }
            }
        } catch (e: FileNotFoundException) {
            Timber.e(e, "unpackZipFile() | Archive file not found: '$archivePath'")
            return false
        } catch (e: IOException) {
            Timber.e(
                e,
                "unpackZipFile() | IOException during zip processing for archive: '$archivePath'"
            )
            return false
        } catch (e: SecurityException) {
            Timber.e(
                e,
                "unpackZipFile() | SecurityException during zip processing for archive: '$archivePath'"
            )
            return false
        }

        Timber.d("unpackZipFile() | Successfully unpacked archive '$archivePath' to '${outputDir.absolutePath}'")
        return true
    }

    /**
     * Unzips a zip file recursively.
     *
     * This function extracts the contents of a zip file to a directory with the same name as the zip file (without the .zip extension).
     * It handles nested zip files by recursively calling itself.
     *
     * @param zipName The name of the zip file to unzip.
     *
     * @throws IOException If there is an error opening or reading the zip file.
     */
    @Throws(IOException::class)
    fun unzipRecursively(zipName: String) {
        Timber.d("unzipRecursively() | Starting for: $zipName")
        val srcFile = File(zipName)
        if (!srcFile.exists() || !srcFile.isFile) {
            Timber.e("unzipRecursively() | Source zip file does not exist or is not a file: $zipName")
            throw FileNotFoundException("Source zip file not found: $zipName")
        }

        // Create a directory with the same name to which the contents will be extracted
        val zipPath = zipName.substring(0, zipName.length - 4) // Assumes .zip extension
        val outputDir = File(zipPath)

        if (!outputDir.exists()) {
            if (!outputDir.mkdirs()) {
                Timber.e("unzipRecursively() | Could not create output directory: $zipPath")
                throw IOException("Could not create output directory: $zipPath")
            }
        } else if (!outputDir.isDirectory) {
            Timber.e("unzipRecursively() | Output path exists but is not a directory: $zipPath")
            throw IOException("Output path exists but is not a directory: $zipPath")
        }
        Timber.d("unzipRecursively() | Output directory: ${outputDir.absolutePath}")


        val canonicalOutputDirPath = outputDir.canonicalPath

        ZipFile(srcFile).use { zipFile ->
            val entries = zipFile.entries()
            while (entries.hasMoreElements()) {
                val entry: ZipEntry = entries.nextElement()
                val destinationFile = File(outputDir, entry.name)

                // Security check: Path Traversal (Zip Slip)
                val canonicalDestinationPath = destinationFile.canonicalPath
                if (!canonicalDestinationPath.startsWith(canonicalOutputDirPath + File.separator) && canonicalDestinationPath != canonicalOutputDirPath) {
                    Timber.e("unzipRecursively() | Path traversal attempt for entry: '${entry.name}'. Entry resolves to '$canonicalDestinationPath', which is outside of '$canonicalOutputDirPath'.")
                    // Decide on error handling: throw exception or skip entry
                    throw SecurityException("Path traversal attempt for entry: ${entry.name}")
                }


                if (entry.isDirectory) {
                    if (!destinationFile.mkdirs() && !destinationFile.isDirectory) {
                        Timber.w("unzipRecursively() | Could not create directory for entry: '${entry.name}' at '${destinationFile.absolutePath}'")
                    }
                } else {
                    Timber.d("unzipRecursively() | Extracting file: ${destinationFile.absolutePath}")
                    destinationFile.parentFile?.let { parentDir ->
                        if (!parentDir.exists()) {
                            if (!parentDir.mkdirs() && !parentDir.isDirectory) {
                                throw IOException("Could not create parent directory ${parentDir.absolutePath} for entry ${entry.name}")
                            }
                        }
                    }

                    BufferedInputStream(zipFile.getInputStream(entry)).use { bis ->
                        BufferedOutputStream(
                            FileOutputStream(destinationFile),
                            DEFAULT_BUFFER_SIZE
                        ).use { bos -> // Use constant
                            val buffer = ByteArray(DEFAULT_BUFFER_SIZE) // Use constant
                            var b: Int
                            while ((bis.read(buffer, 0, DEFAULT_BUFFER_SIZE)
                                    .also { b = it }) != -1
                            ) {
                                bos.write(buffer, 0, b)
                            }
                        }
                    }
                    // Recursively unzip if this entry is also a zip file
                    if (entry.name.endsWith(FileExtensions.ZIP.extensionValue, ignoreCase = true)) {
                        Timber.d("unzipRecursively() | Found nested zip: ${destinationFile.absolutePath}, calling unzipRecursively.")
                        unzipRecursively(destinationFile.absolutePath)
                    }
                }
            }
        }
        Timber.d("unzipRecursively() | Finished for: $zipName")
    }

    ////////////////////////////////////////////////////////
    // --- GET
    ////////////////////////////////////////////////////////
    /**
     * Returns the absolute path to a file or directory within the application's shared directory.
     *
     * This function constructs the full path by appending the given relative [path]
     * to the base path of the application's shared directory, as determined by [getSamSharePath].
     *
     * For example, if `getSamSharePath()` returns "/storage/emulated/0/SAM_SHARE/" and the input `path`
     * is "my_file.txt", this function will return "/storage/emulated/0/SAM_SHARE/my_file.txt".
     *
     * The function also logs the generated path using Timber for debugging purposes.
     *
     * @param path The relative path to the file or directory within the application's shared directory.
     * This should not start with a slash.
     * @return The absolute path to the specified file or directory.
     */
    /*fun getAppFilePath(path: String): String = run { getSamSharePath() + path }
        .also { Timber.d("getAppFilePath() | path :$it") }*/

    fun getDownloadFolderAsString(): String = Environment
        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        .absolutePath


    fun getFile(parentDirectory: String, fileName: String, extension: String): File =
        File(parentDirectory, "$fileName.$extension")

    /**
     * This function returns the path to the SAM_SHARE directory.
     * If external storage is mounted, it returns the path to the SAM_SHARE directory on external storage.
     * Otherwise, it returns the path to the SAM_SHARE directory on the download cache directory.
     *
     * @return The path to the SAM_SHARE directory.
     */
    /*fun getSamSharePath(): String = run {
        // For Android 10 (API 29) and above, app-specific storage is preferred for new files.
        // However, SAM_SHARE sounds like a shared location, which has restrictions.
        // This logic might need review based on actual requirements for SAM_SHARE.
        if (LabCompatibilityManager.isAndroid10()) { // API 29+
            // Accessing filesDir is always specific to the app.
            // If SAM_SHARE is truly external/shared, this path is internal.
            context.filesDir.absolutePath + File.separator + APK_DIRECTORY + File.separator
        } else { // Below API 29
            // Deprecated methods for external storage access
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                Environment.getExternalStorageDirectory().absolutePath + // Deprecated
                        File.separator +
                        APK_DIRECTORY +
                        File.separator
            } else {
                Environment.getDownloadCacheDirectory().absolutePath + // Deprecated
                        File.separator +
                        APK_DIRECTORY +
                        File.separator
            }
        }
    }.also { Timber.d("getSamSharePath() | path :$it") }*/

    /**
     * Retrieves an [InputStream] for a given asset file.
     *
     * This function attempts to open an asset file located in the application's assets folder.
     * The full filename is constructed by concatenating [assetName] and [extension] with a dot.
     * For example, if `assetName` is "my_file" and `extension` is "txt", it will try to open "my_file.txt".
     *
     * It uses a `runCatching` block to handle potential `IOException` if the asset is not found
     * or cannot be opened.
     * - On failure, it logs an error message with Timber, including the exception details.
     * - On success, it logs a debug message with Timber.
     *
     * @param assetName The name of the asset file (without the extension).
     * @param extension The extension of the asset file (without the leading dot).
     * @return An [InputStream] for the asset if found and successfully opened, otherwise `null`.
     */
    fun getAsset(context: Context, assetName: String, extension: String): InputStream? =
        context.assets.runCatching {
            open("$assetName.$extension")
        }
            .onFailure { exception -> Timber.e("getAsset() | onFailure | Error caught with message : ${exception.message} (class : ${exception.javaClass.canonicalName})") }
            .onSuccess { Timber.d("getAsset() | onSuccess") }
            .getOrNull()

    ////////////////////////////////////////////////////////
    // --- UPDATE
    ////////////////////////////////////////////////////////
    fun splitFile(file: File, chunkSizeMB: Int, outputDir: File) {
        if (!file.exists() || !file.isFile) {
            Timber.e("splitFile() | Source file does not exist or is not a file: ${file.absolutePath}")
            return
        }
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            Timber.e("splitFile() | Could not create output directory: ${outputDir.absolutePath}")
            return
        }
        if (!outputDir.isDirectory) {
            Timber.e("splitFile() | Output path is not a directory: ${outputDir.absolutePath}")
            return
        }


        val chunkSize =
            chunkSizeMB * 1024 * 1024L // Use Long for chunk size to avoid overflow with large MB values
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE) // Use constant
        var partCounter = 1

        Timber.d("splitFile() | Splitting file ${file.absolutePath} into chunks of $chunkSizeMB MB")

        try {
            BufferedInputStream(FileInputStream(file)).use { bis ->
                val fileName = file.nameWithoutExtension // Get name without original extension
                val fileExtension = file.extension

                while (true) {
                    val newFileName = if (fileExtension.isNotEmpty()) {
                        "$fileName.part$partCounter.$fileExtension"
                    } else {
                        "$fileName.part$partCounter"
                    }
                    val newFile = File(outputDir, newFileName)
                    partCounter++

                    BufferedOutputStream(FileOutputStream(newFile)).use { bos ->
                        var bytesWrittenThisPart: Long = 0
                        var bytesReadFromInput: Int

                        while (bytesWrittenThisPart < chunkSize) {
                            bytesReadFromInput = bis.read(buffer)
                            if (bytesReadFromInput == -1) break // End of source file

                            val bytesToWrite =
                                if (bytesWrittenThisPart + bytesReadFromInput > chunkSize) {
                                    (chunkSize - bytesWrittenThisPart).toInt()
                                } else {
                                    bytesReadFromInput
                                }
                            bos.write(buffer, 0, bytesToWrite)
                            bytesWrittenThisPart += bytesToWrite
                            if (bytesToWrite < bytesReadFromInput) {
                                // This case should not happen if logic is correct,
                                // as bis.read() won't return more than buffer.size()
                                // and bytesToWrite handles the chunk limit.
                                // However, if it means we only wrote a partial buffer to hit chunk size,
                                // we need to ensure the main loop reading from 'bis' continues correctly.
                                // The current logic seems to handle this by breaking the inner while
                                // and the outer while checks bis.available().
                                // For splitting, it's more common to read up to buffer size, write,
                                // and count towards chunk, then break from writing to this part.
                                // The provided logic seems complex here. Simpler: read, write to current part,
                                // if current part size > chunk, close part, open new.
                                // Let's stick to a slightly modified version of original for now.
                            }
                        }
                    }
                    if (bis.available() == 0) break // No more data in input stream
                }
            }
        } catch (e: IOException) {
            Timber.e(e, "splitFile() | Error during splitting file ${file.absolutePath}")
        }
    }


    /**
     * Copies a file from a source location to a destination location.
     *
     * @param src The source file to copy.
     * @param dest The destination file.
     * @return True if the file was copied successfully, false otherwise.
     */
    fun copyFile(src: File, dest: File): Boolean {
        if (!src.exists() || !src.isFile) {
            Timber.e("copyFile() | Source file does not exist or is not a file: ${src.absolutePath}")
            return false
        }
        dest.parentFile?.let {
            if (!it.exists() && !it.mkdirs()) {
                Timber.e("copyFile() | Could not create destination directory: ${it.absolutePath}")
                return false
            }
        }

        try {
            FileInputStream(src).use { `in` ->
                FileOutputStream(dest).use { out ->
                    val buf = ByteArray(DEFAULT_BUFFER_SIZE) // Use constant
                    var len: Int
                    while ((`in`.read(buf).also { len = it }) > 0) {
                        out.write(buf, 0, len)
                    }
                }
                return true
            }
        } catch (e: FileNotFoundException) {
            Timber.e(
                e,
                "copyFile() | File not found during copy. Src: ${src.absolutePath}, Dest: ${dest.absolutePath}"
            )
        } catch (e: IOException) {
            Timber.e(
                e,
                "copyFile() | IOException during copy. Src: ${src.absolutePath}, Dest: ${dest.absolutePath}"
            )
        }
        return false
    }

    /**
     * Copies a large file from a source to a destination.
     *
     * This function uses FileChannel for efficient copying of large files.
     *
     * @param src The source file to copy.
     * @param dst The destination file.
     * @throws IOException If an I/O error occurs during the copy operation.
     */
    @Throws(IOException::class)
    fun copyBigFile(src: File, dst: File) {
        if (!src.exists() || !src.isFile) {
            throw FileNotFoundException("Source file does not exist or is not a file: ${src.absolutePath}")
        }
        dst.parentFile?.let {
            if (!it.exists() && !it.mkdirs()) {
                throw IOException("Could not create destination directory: ${it.absolutePath}")
            }
        }
        FileInputStream(src).channel.use { sourceChannel ->
            FileOutputStream(dst).channel.use { destChannel ->
                sourceChannel.transferTo(0, sourceChannel.size(), destChannel)
            }
        }
    }

    /**
     * Copies a file from the app's assets folder to a specified destination.
     *
     * This function iterates through the files in the root of the assets folder.
     * If a filename in the assets matches a part of the `srcFile` string,
     * it attempts to copy that asset file to the `destFile` path.
     *
     * **Note:** The current implementation has a TODO to make the asset file matching
     * more dynamic. Currently, it checks if `srcFile` *contains* the asset filename.
     *
     * Errors during file listing or copying are logged using Timber.
     * Input and output streams are closed in a finally block to ensure resource release.
     *
     * @param assetPathInRoot The specific path/filename of the asset in the root assets folder (e.g., "myConfig.xml").
     * @param destFile The absolute path to where the asset file should be copied.
     */
    private fun copyAssetFromRoot(
        context: Context,
        assetPathInRoot: String,
        destFile: String
    ): Boolean =
        runCatching {
            Timber.d("copyAssetFromRoot() | Attempting to copy asset '$assetPathInRoot' to '$destFile'")
            val outFile = File(destFile)
            outFile.parentFile?.let {
                if (!it.exists() && !it.mkdirs()) {
                    Timber.e("copyAssetFromRoot() | Could not create destination directory: ${it.absolutePath}")
                    return@runCatching false
                }
            }

            context.assets.open(assetPathInRoot).use { inputStream ->
                FileOutputStream(outFile).use { outputStream ->
                    copyFile(inputStream, outputStream) // Uses the private helper
                }
            }
            Timber.i("copyAssetFromRoot() | Successfully copied asset '$assetPathInRoot' to '$destFile'")
            true
        }
            .onFailure { exception ->
                Timber.e(
                    exception,
                    "copyAssetFromRoot() | Failed to copy asset '$assetPathInRoot' to '$destFile'"
                )
            }
            .getOrDefault(false)


    fun copyAssetsToApplicationDirectory(
        context: Context,
        filename: String = "EMDKConfig.xml"
    ): File? =
        context.runCatching {
            val destinationFile = File(this.filesDir, filename)
            Timber.d("copyAssetsToApplicationDirectory() | Attempting to copy asset '$filename' to '${destinationFile.absolutePath}'")

            this.assets.open(filename).use { inputStream ->
                FileOutputStream(destinationFile).use { outputStream ->
                    copyFile(inputStream, outputStream) // Uses the private helper
                }
            }
            Timber.i("copyAssetsToApplicationDirectory() | Successfully copied asset '$filename' to '${destinationFile.absolutePath}'")
            destinationFile
        }
            .onFailure { exception ->
                Timber.e(
                    exception,
                    "copyAssetsToApplicationDirectory() | Failed to copy asset '$filename'"
                )
            }
            .getOrNull()

    /**
     * Copies the content of an InputStream to an OutputStream.
     *
     * @param in The InputStream to read from.
     * @param out The OutputStream to write to.
     * @throws IOException If an I/O error occurs during the copy process.
     */
    @Throws(IOException::class)
    private fun copyFile(`in`: InputStream, out: OutputStream) {
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE) // Use constant
        var read: Int
        while ((`in`.read(buffer).also { read = it }) != -1) {
            out.write(buffer, 0, read)
        }
    }

    /**
     * Copies a file from the assets folder to the specified destination and then unzips it.
     *
     * @param assetFileName The name of the file in the root of the assets folder (e.g. "archive.zip").
     * @param destDir The destination directory [File] data where the file will be copied and unzipped.
     *                The asset will be copied into this directory, then unzipped into this same directory.
     * @param onSuccess A callback function that is invoked when the operation is successful.
     *                  It receives the absolute path of the destination directory as a [String].
     * @param onError A callback function that is invoked when an error occurs.
     *                It receives an error message as a [String] (nullable) and the [Throwable] (nullable) that caused the error.
     */
    fun copyThenUnzip(
        context: Context,
        assetFileName: String, // Changed from src to be more specific
        destDir: File,      // Changed to destDir for clarity
        onSuccess: (String?) -> Unit,
        onError: (String?, Throwable?) -> Unit
    ) = runCatching {
        Timber.d("copyThenUnzip() | Asset: '$assetFileName', Destination Directory: '${destDir.absolutePath}'")

        if (!destDir.exists() && !destDir.mkdirs()) {
            throw IOException("Could not create destination directory: ${destDir.absolutePath}")
        }
        if (!destDir.isDirectory) {
            throw IOException("Destination path is not a directory: ${destDir.absolutePath}")
        }

        val copiedAssetFile = File(destDir, assetFileName)

        if (!copyAssetFromRoot(context, assetFileName, copiedAssetFile.absolutePath)) {
            throw IOException("Failed to copy asset '$assetFileName' to '${copiedAssetFile.absolutePath}'")
        }

        // Now unzip the copied asset file into the destDir
        // The previous `unzipRecursively` might be too aggressive if it creates a subdir with the zip name.
        // Using the robust `unpackZipFile` which unpacks into the specified output path.
        if (!unpackZipFile(copiedAssetFile.absolutePath, destDir.absolutePath)) {
            throw IOException("Failed to unzip '${copiedAssetFile.absolutePath}' into '${destDir.absolutePath}'")
        }

        // Optionally delete the copied zip file after successful extraction
        // copiedAssetFile.delete()
        // Timber.d("copyThenUnzip() | Deleted intermediate zip file: ${copiedAssetFile.absolutePath}")

        onSuccess(destDir.absolutePath)
    }
        .onFailure { exception ->
            exception.printStackTrace()
            Timber.e("copyThenUnzip() | onFailure | Error caught with message ${exception.message} (class : ${exception.javaClass.canonicalName})")
            onError(exception.message, exception)
        }
        .onSuccess {
            Timber.d("copyThenUnzip() | onSuccess for asset '$assetFileName'")
        }


    ////////////////////////////////////////////////////////
    // --- DELETE
    ////////////////////////////////////////////////////////
    /**
     * Deletes the specified file.
     *
     * This function first checks if the file exists. If it does, it attempts to delete it.
     * It logs the absolute path of the file to be deleted using Timber.
     * It also prints a message to the console indicating whether the file was successfully
     * deleted or not.
     *
     * @param fileToDelete The [File] object representing the file to be deleted.
     */
    fun deleteFile(fileToDelete: File) {
        Timber.d("deleteFile() | fileToDelete : ${fileToDelete.absolutePath}") // Changed to debug
        if (!fileToDelete.exists()) {
            Timber.w("deleteFile() | File not found, cannot delete: ${fileToDelete.absolutePath}") // Changed to warn
            return
        }

        if (!fileToDelete.delete()) {
            Timber.e("deleteFile() | Unable to delete file : ${fileToDelete.absolutePath}")
            return
        }

        Timber.i("deleteFile() | File deleted : ${fileToDelete.absolutePath}")
    }

    /**
     * Deletes all APK files from the SAM_SHARE/APK directory.
     * It first checks if the directory exists and is a directory.
     * Then, it iterates over all files in the directory and deletes them.
     * If an error occurs during the process, it logs the error.
     */
    /*fun deleteAllApkFile() {
        try {
            val dirPath = getSamSharePath() // This path includes APK_DIRECTORY
            val dir = File(dirPath)
            if (dir.exists() && dir.isDirectory) {
                val children: Array<File>? = dir.listFiles() // listFiles is safer
                if (children != null) {
                    for (child in children) {
                        if (child.isFile && child.name.endsWith(".apk", ignoreCase = true)) {
                            Timber.d("deleteAllApkFile() | Deleting APK: ${child.absolutePath}")
                            if (!child.delete()) {
                                Timber.w("deleteAllApkFile() | Failed to delete APK: ${child.absolutePath}")
                            }
                        }
                    }
                } else {
                    Timber.w("deleteAllApkFile() | No files found in directory or I/O error: ${dir.absolutePath}")
                }
            } else {
                Timber.w("deleteAllApkFile() | Directory does not exist or is not a directory: ${dir.absolutePath}")
            }
        } catch (exception: Exception) { // Catch more specific exceptions if possible
            exception.printStackTrace()
            Timber.e("deleteAllApkFile() | Error caught with message ${exception.message} (class : ${exception.javaClass.canonicalName})")
        }
    }*/


    /*companion object {
        // This path seems to point to a specific file, not a directory for profiles.
        // Also, Environment.getExternalStorageDirectory() is deprecated and has issues with scoped storage.
        // This needs careful review based on where EMDKConfig.xml is actually expected/managed.
        val emdkConfigFilePathAsString: String
            get() { // Made it a getter to log access, consider context for path construction
                val path =
                    Environment.getExternalStorageDirectory().absolutePath + "/SAM_SHARE/PROFILES/" + "EMDKConfig.xml"
                Timber.d("Accessing emdkConfigFilePathAsString: $path")
                return path
            }


        const val APK_DIRECTORY: String = "SAM_SHARE/APK" // This is a relative path segment
    }*/

    /**
     * Reads file and returns a String.
     *
     * @param file the file to read
     * @return the string with file content or null
     */
    fun tryReadFile(file: File): String? {
        try {
            FileSystem.SYSTEM.source(file.toOkioPath()).buffer().use {
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
        FileSystem.SYSTEM.source(file.toOkioPath()).buffer()
            .use { source -> return source.readUtf8() }
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
    fun unzipGzip(responseBody: ResponseBody): String? = runCatching {
        Timber.i("unzipGzip(responseBody: ResponseBody) | Build stream objects with json file received ...")

        var json: String?
        val compressedInputStream: InputStream = GZIPInputStream(responseBody.byteStream())
        val inputSource = InputSource(compressedInputStream)
        val inputStream: InputStream = BufferedInputStream(inputSource.byteStream)

        // Build buffer and StringBuilder
        val sb = StringBuilder()

        inputStream.source().use { fileSource ->
            fileSource.buffer().use { bufferedSource ->
                while (true) {
                    val line = bufferedSource.readUtf8Line() ?: break
                    sb.append(line)
                }

                json = sb.toString()
            }
        }

        compressedInputStream.close()
        inputStream.close()

        json
    }
        .onFailure {
            it.printStackTrace()
            Timber.e("unzipGzip() | onFailure | error caught with message: ${it.message} (class: ${it.javaClass.canonicalName})")
        }
        .onSuccess {
            Timber.d("unzipGzip() | onSuccess | is success: $it")
        }
        .getOrNull()

    /**
     * Ref : https://stackoverflow.com/questions/10469407/android-decompress-downloaded-xml-gz-file
     * https://stackoverflow.com/questions/216894/get-an-outputstream-into-a-string
     *
     * @param responseBody
     * @return
     */
    fun unzipGzip(responseBodyStream: InputStream): String? = runCatching {
        Timber.i("unzipGzip(responseBodyStream: InputStream) | Build stream objects with json file received ...")

        var json: String?
        val compressedInputStream: InputStream = GZIPInputStream(responseBodyStream)
        val inputSource = InputSource(compressedInputStream)
        val inputStream: InputStream = BufferedInputStream(inputSource.byteStream)

        // Build buffer and StringBuilder
        val sb = StringBuilder()

        inputStream.source().use { fileSource ->
            fileSource.buffer().use { bufferedSource ->
                while (true) {
                    val line = bufferedSource.readUtf8Line() ?: break
                    sb.append(line)
                }

                json = sb.toString()
            }
        }

        compressedInputStream.close()
        inputStream.close()

        json
    }
        .onFailure {
            it.printStackTrace()
            Timber.e("unzipGzip() | onFailure | error caught with message: ${it.message} (class: ${it.javaClass.canonicalName})")
        }
        .onSuccess {
            Timber.d("unzipGzip() | onSuccess | is success: $it")
        }
        .getOrNull()

    fun unzipGzip(gzip: GzipSource): String? = runCatching {
        Timber.i("unzipGzip(gzip: GzipSource) | Build stream objects with json file received ...")

        var json: String?

        // Build buffer and StringBuilder
        val sb = StringBuilder()

        gzip.use { fileSource ->
            fileSource.buffer().use { bufferedSource ->
                while (true) {
                    val line = bufferedSource.readUtf8Line() ?: break
                    sb.append(line)
                }

                json = sb.toString()
            }
        }

        json
    }
        .onFailure {
            it.printStackTrace()
            Timber.e("unzipGzip() | onFailure | error caught with message: ${it.message} (class: ${it.javaClass.canonicalName})")
        }
        .onSuccess {
            Timber.d("unzipGzip() | onSuccess | is success: $it")
        }
        .getOrNull()

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

        return if (mediaDir.exists())
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