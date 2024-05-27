package com.riders.thelab.core.data.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {

    // APP
    val DATASTORE_FIRST_TIME_LAUNCHED_KEY = booleanPreferencesKey("DATASTORE_FIRST_TIME_LAUNCHED_KEY")
    val DATASTORE_DARK_MODE_KEY = booleanPreferencesKey("DATASTORE_DARK_MODE_KEY")
    val DATASTORE_VIBRATION_KEY = booleanPreferencesKey("DATASTORE_VIBRATION_KEY")
    val DATASTORE_ACTIVITIES_SPLASH_SCREEN_KEY =
        booleanPreferencesKey("DATASTORE_ACTIVITIES_SPLASH_SCREEN_KEY")

    // USER
    val DATASTORE_EMAIL_KEY = stringPreferencesKey("DATASTORE_EMAIL_KEY")
    val DATASTORE_PASSWORD_KEY = stringPreferencesKey("DATASTORE_PASSWORD_KEY")
    val DATASTORE_REMEMBER_CREDENTIALS_KEY =
        booleanPreferencesKey("DATASTORE_REMEMBER_CREDENTIALS_KEY")
    val DATASTORE_TOKEN_KEY = stringPreferencesKey("DATASTORE_TOKEN_KEY")
}