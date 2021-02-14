package com.riders.thelab.core.okhttp;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;
import timber.log.Timber;


public class LabInterceptors {
    public static @NotNull Interceptor provideLoggingInterceptor() {
        return new HttpLoggingInterceptor(
                s -> Timber
                        .tag("OKHttp")
                        .d(s))
                .setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    public static class LoggingRequestInterceptors implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            //Intercepting the request to log it
            try {
                final RequestBody copy = chain.request().body();
                final Buffer buffer = new Buffer();

                if (copy != null)
                    copy.writeTo(buffer);

                Timber.d("Request sent : %s", buffer.readUtf8());
            } catch (final IOException ignored) {
            }

            return chain.proceed(chain.request());
        }

    }

    public static class LoggingResponseInterceptors implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();

            //If we're downloading a file, we dont want to intercept the response
            if (request.body().contentType().subtype().equals("vnd.android.package-archive"))
                return chain.proceed(request);

            okhttp3.Response response = chain.proceed(request);
            String jsonResponse = response.body().string();

            Timber.d("Received response : %s", jsonResponse);

            // Re-create the response before returning it because body can be read only once
            return response
                    .newBuilder()
                    .body(
                            ResponseBody
                                    .create(
                                            Objects.requireNonNull(response.body()).contentType(),
                                            jsonResponse))
                    .build();
        }
    }

    /**
     * https://stackoverflow.com/questions/51901333/okhttp-3-how-to-decompress-gzip-deflate-response-manually-using-java-android
     * <p>
     * This interceptor compresses the HTTP request body. Many webservers can't handle this!
     */
    public static class GzipRequestInterceptor
            implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request originalRequest = chain.request();
            if (originalRequest.body() == null
                    || originalRequest.header("Content-Encoding") != null) {
                return chain.proceed(originalRequest);
            }

            Request compressedRequest = originalRequest.newBuilder()
                    .header("Content-Encoding", "gzip")
                    .method(originalRequest.method(), gzip(originalRequest.body()))
                    .build();
            return chain.proceed(compressedRequest);
        }

        private RequestBody gzip(final RequestBody body) {
            return new RequestBody() {
                @Override
                public MediaType contentType() {
                    return body.contentType();
                }

                @Override
                public long contentLength() {
                    return -1; // We don't know the compressed length in advance!
                }

                @Override
                public void writeTo(BufferedSink sink) throws IOException {
                    BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                    body.writeTo(gzipSink);
                    gzipSink.close();
                }
            };
        }
    }

}
