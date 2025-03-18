package com.riders.thelab.core.ui.data.local.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val DATASTORE_DARK_MODE_KEY = booleanPreferencesKey("DATASTORE_DARK_MODE")
    val DATASTORE_APP_THEME_NAME_KEY = stringPreferencesKey("DATASTORE_APP_THEME_NAME")
}