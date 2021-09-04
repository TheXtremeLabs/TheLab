package com.riders.thelab.data.remote.api

import com.riders.thelab.utils.Constants


import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming

interface WeatherBulkApiService {

    @Streaming // allows streaming data directly to fs without holding all contents in ram
    @GET(Constants.WEATHER_BULK_DOWNLOAD_URL)
    suspend fun getCitiesGZipFile(): ResponseBody
}