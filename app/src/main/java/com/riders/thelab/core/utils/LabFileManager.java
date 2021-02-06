package com.riders.thelab.core.utils;

import org.xml.sax.InputSource;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

import okhttp3.ResponseBody;
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
            InputStream compressedInputStream = new GZIPInputStream(responseBody.byteStream());
            InputSource inputSource = new InputSource(compressedInputStream);
            InputStream inputStream = new BufferedInputStream(inputSource.getByteStream());

            ByteArrayOutputStream baos = new ByteArrayOutputStream(inputStream.available());

            Timber.d("Create destination file optional... in our case : no");


            // TODO : unzip file and sav it somewhere
            /*File outputFile = new File("cities");
            outputFile.getParentFile().mkdirs();
            OutputStream outputStream = new FileOutputStream(outputFile.getAbsoluteFile());

            byte[] buffer = new byte[4096];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(json, 0, length);
            }
*/
            String tmp = new String(baos.toByteArray(), StandardCharsets.UTF_8);

            Timber.d("String tmp value : %s", tmp);
            Timber.d("String tmp value : %s", tmp);

            /*outputStream.flush();
            outputStream.close();*/
            inputStream.close();

            json = tmp;
            Timber.d("String json value : %s", json);

            return json;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

}
