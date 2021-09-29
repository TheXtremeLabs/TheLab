package com.riders.thelab.core.okhttp

import okhttp3.*
import okio.Buffer
import okio.BufferedSink
import okio.GzipSink
import okio.buffer
import timber.log.Timber
import java.io.IOException
import java.util.*

class LabInterceptors {

    class LoggingRequestInterceptors : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {

            //Intercepting the request to log it
            try {
                val buffer = Buffer()
                chain.request().body?.writeTo(buffer)
                Timber.d("Request sent : %s", buffer.readUtf8())
            } catch (ignored: IOException) {
                Timber.e(ignored)
            }
            return chain.proceed(chain.request())
        }
    }

    class LoggingResponseInterceptors : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request: Request = chain.request()

            //If we're downloading a file, we dont want to intercept the response
            if (request.body!!.contentType()!!.subtype == "vnd.android.package-archive") return chain.proceed(
                request
            )
            val response: Response = chain.proceed(request)
            val jsonResponse = response.body!!.string()
            Timber.d("Received response : %s", jsonResponse)

            // Re-create the response before returning it because body can be read only once
            return response
                .newBuilder()
                .body(
                    ResponseBody.create(
                        response.body?.contentType(),
                        jsonResponse
                    )
                )
                .build()
        }
    }

    /**
     * https://stackoverflow.com/questions/51901333/okhttp-3-how-to-decompress-gzip-deflate-response-manually-using-java-android
     *
     *
     * This interceptor compresses the HTTP request body. Many webservers can't handle this!
     */
    class GzipRequestInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest: Request = chain.request()
            if (originalRequest.body == null
                || originalRequest.header("Content-Encoding") != null
            ) {
                return chain.proceed(originalRequest)
            }
            val compressedRequest = originalRequest.newBuilder()
                .header("Content-Encoding", "gzip")
                .method(originalRequest.method, gzip(originalRequest.body))
                .build()
            return chain.proceed(compressedRequest)
        }

        private fun gzip(body: RequestBody?): RequestBody {
            return object : RequestBody() {
                override fun contentType(): MediaType? {
                    return body!!.contentType()
                }

                override fun contentLength(): Long {
                    return -1 // We don't know the compressed length in advance!
                }

                @Throws(IOException::class)
                override fun writeTo(sink: BufferedSink) {
                    val gzipSink = GzipSink(sink).buffer()
                    body!!.writeTo(gzipSink)
                    gzipSink.close()
                }
            }
        }
    }
}