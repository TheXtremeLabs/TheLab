package com.riders.thelab.feature.weather.utils

object
Constants {

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

    const val BASE_ENDPOINT_THE_LAB_URL: String = "https://the-lab-back.herokuapp.com"
    /*HTTP + if (LabDeviceManager.getModel().trim().lowercase()
            .contains("sdk")
    ) EMULATOR_IP_ADDRESS else IP_ADDRESS + PORT*/


    //REST client Base URL
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
}