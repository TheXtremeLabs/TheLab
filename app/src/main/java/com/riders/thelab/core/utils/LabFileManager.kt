package com.riders.thelab.core.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.annotation.AnyRes
import androidx.annotation.NonNull
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
class LabFileManager private constructor() {
    companion object {

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
    }
}