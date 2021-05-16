package com.riders.thelab.core.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.xml.sax.InputSource;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import okhttp3.ResponseBody;
import okhttp3.internal.io.FileSystem;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;
import timber.log.Timber;

/**
 * Utils for I/O operations.
 */
public class LabFileManager {

    private LabFileManager() {
    }

    /**
     * Reads file and returns a String.
     *
     * @param file the file to read
     * @return the string with file content or null
     */
    @Nullable
    public static String tryReadFile(@NonNull final File file) {
        try (final BufferedSource source = Okio.buffer(FileSystem.SYSTEM.source(file))) {
            return source.readUtf8();
        } catch (final IOException exception) {
            //ignored exception
            return null;
        }
    }

    /**
     * Reads InputStream and returns a String. It will close stream after usage.
     *
     * @param stream the stream to read
     * @return the string with file content or null
     */
    @Nullable
    public static String tryReadFile(@NonNull final InputStream stream) {
        try (final BufferedSource source = Okio.buffer(Okio.source(stream))) {
            return source.readUtf8();
        } catch (final IOException exception) {
            //ignored exception
            return null;
        }
    }

    /**
     * Reads file and returns a String.
     *
     * @param file the file to read
     * @return the string content
     */
    @NonNull
    public static String readFile(@NonNull final File file) throws IOException {
        try (final BufferedSource source = Okio.buffer(FileSystem.SYSTEM.source(file))) {
            return source.readUtf8();
        }
    }

    /**
     * Reads InputStream and returns a String. It will close stream after usage.
     *
     * @param stream the stream to read
     * @return the string content
     */
    @NonNull
    public static String readFile(@NonNull final InputStream stream) throws IOException {
        try (final BufferedSource source = Okio.buffer(Okio.source(stream))) {
            return source.readUtf8();
        }
    }

    /**
     * Ref : https://stackoverflow.com/questions/10469407/android-decompress-downloaded-xml-gz-file
     * https://stackoverflow.com/questions/216894/get-an-outputstream-into-a-string
     *
     * @param responseBody
     * @return
     */
    public static String unzipGzip(ResponseBody responseBody) {
        Timber.d("unzipGzip()");

        String json = null;

        try {
            Timber.d("Build stream objects with json file received ...");
            InputStream compressedInputStream = new GZIPInputStream(responseBody.byteStream());
            InputSource inputSource = new InputSource(compressedInputStream);
            InputStream inputStream = new BufferedInputStream(inputSource.getByteStream());

            Timber.d("Build buffer and string builder ...");
            StringBuilder sb = new StringBuilder();
            try (Source fileSource = Okio.source(inputStream);
                 BufferedSource bufferedSource = Okio.buffer(fileSource)) {

                while (true) {
                    String line = bufferedSource.readUtf8Line();
                    if (line == null) break;

                    sb.append(line);
                }

                Timber.d("File read, build json target variable ...");
                json = sb.toString();

                return json;
            } catch (Exception e) {
                Timber.e(e);
                e.printStackTrace();
            } finally {
                compressedInputStream.close();
                inputStream.close();
            }
        } catch (Exception e) {
            Timber.e(e);
            e.printStackTrace();
        }
        return json;
    }

}
