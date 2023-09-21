package com.riders.thelab.utils

import android.content.Context
import com.riders.thelab.core.data.local.model.app.App

object Constants {

    const val DATASTORE_THE_LAB_FILE_NAME = "THE_LAB_DATASTORE"

    const val EMULATOR_DEVICE_TAG = "sdk"

    const val ANDROID_RES_PATH = "android.resource://"
    const val SEPARATOR = "/"

    /////////////////////////////////////////////
    //
    // TheLab API
    //
    /////////////////////////////////////////////
    private const val HTTP = "http://"

    //    private const val IP_ADDRESS = "192.168.0.136"
//        private const val IP_ADDRESS = "192.168.0.48"
    private const val IP_ADDRESS = "192.168.1.99"

    //    private const val EMULATOR_IP_ADDRESS = "192.168.0.163"
    private const val EMULATOR_IP_ADDRESS = "192.168.0.48"

    private const val PORT = ":8101"

    val BASE_ENDPOINT_THE_LAB_URL: String = "https://the-lab-back.herokuapp.com"
    /*HTTP + if (LabDeviceManager.getModel().trim().lowercase()
            .contains("sdk")
    ) EMULATOR_IP_ADDRESS else IP_ADDRESS + PORT*/


    //REST client Base URL
    const val BASE_ENDPOINT_YOUTUBE = "https://raw.githubusercontent.com"
    const val BASE_ENDPOINT_SEARCH = "https://ajax.googleapis.com"
    const val BASE_ENDPOINT_GOOGLE_FIREBASE_API = " https://firebasestorage.googleapis.com/"
    const val BASE_ENDPOINT_GOOGLE_MAPS_API = "https://maps.googleapis.com/maps/api/"
    const val BASE_ENDPOINT_GOOGLE_PLACES = "https://maps.googleapis.com/maps/api/place/"
    const val BASE_ENDPOINT_WEATHER = "http://api.openweathermap.org"
    const val BASE_ENDPOINT_WEATHER_BULK_DOWNLOAD = "http://bulk.openweathermap.org/"
    const val WEATHER_BULK_DOWNLOAD_URL = "sample/city.list.json.gz"
    const val BASE_ENDPOINT_WEATHER_ICON = "http://openweathermap.org/img/wn/"
    const val BASE_ENDPOINT_WEATHER_FLAG = "http://openweathermap.org/images/flags/"
    const val WEATHER_ICON_SUFFIX = "@2x.png"
    const val WEATHER_FLAG_PNG_SUFFIX = ".png"
    const val WEATHER_COUNTRY_CODE_FRANCE = "FR"
    const val WEATHER_COUNTRY_CODE_GUADELOUPE = "GP"
    const val WEATHER_COUNTRY_CODE_MARTINIQUE = "MQ"
    const val WEATHER_COUNTRY_CODE_GUYANE = "GF"
    const val WEATHER_COUNTRY_CODE_REUNION = "RE"
    const val FIREBASE_DATABASE_NAME = "kat"

    // Palette
    const val PALETTE_URL = "http://i.ytimg.com/vi/aNHOfJCphwk/maxresdefault.jpg"

    // Activity Recognition
    const val BROADCAST_DETECTED_ACTIVITY = "activity_intent"
    const val DETECTION_INTERVAL_IN_MILLISECONDS = (30 * 1000).toLong()
    const val CONFIDENCE = 70

    const val SZ_SEPARATOR = "/"

    const val GPS_REQUEST = 5214

    const val NOTIFICATION_ID: Int = 45532
    const val NOTIFICATION_CHANNEL_ID: String = "45532"
    const val NOTIFICATION_MUSIC_ID: Int = 3432
    const val NOTIFICATION_MUSIC_CHANNEL_ID: String = "3432"

    // WebView
    const val WEB_URL: String = "WEB_URL"


    /**
     * Return app testing list
     */
    fun getActivityList(context: Context): List<App> {
        return ArrayList(AppBuilderUtils.buildActivities(context))
    }
}