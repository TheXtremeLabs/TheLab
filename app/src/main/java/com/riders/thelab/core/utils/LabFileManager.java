package com.riders.thelab.core.utils;

import org.xml.sax.InputSource;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;
import timber.log.Timber;

public class LabFileManager {

    private LabFileManager() {
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
