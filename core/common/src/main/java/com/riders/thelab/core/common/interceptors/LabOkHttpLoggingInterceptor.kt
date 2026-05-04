package com.riders.thelab.core.common.interceptors

import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

class LabOkHttpLoggingInterceptor(val isBulkDownload: Boolean = false) {

    fun provideOkHttpLogger(): HttpLoggingInterceptor = HttpLoggingInterceptor { message: String ->
        Timber.tag("OkHttp").d(message)
    }
        .setLevel(if (isBulkDownload) HttpLoggingInterceptor.Level.HEADERS else HttpLoggingInterceptor.Level.BODY)
}