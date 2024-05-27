package com.riders.thelab.core.data.preferences

import kotlinx.coroutines.flow.Flow

interface IPreferences {

    fun isFirstTimeLaunched(): Flow<Boolean>
    suspend fun saveFirstTimeLaunched(firstTimeLaunched: Boolean)

    fun isNightMode(): Flow<Boolean>
    suspend fun saveNightMode(isNightMode: Boolean)
    suspend fun toggleNightMode()
    fun isVibration(): Flow<Boolean>
    suspend fun saveVibration(isVibration: Boolean)
    suspend fun toggleVibration()
    fun isActivitiesSplashScreenEnabled(): Flow<Boolean>
    suspend fun saveActivitiesSplashScreenEnabled(isActivitiesSplashScreenEnabled: Boolean)
    suspend fun toggleActivitiesSplashScreenEnabled()

    fun getEmailPref(): Flow<String>
    suspend fun saveEmailPref(email: String)
    fun getPasswordPref(): Flow<String>
    suspend fun savePasswordPref(password: String)
    fun isRememberCredentialsPref(): Flow<Boolean>
    suspend fun saveRememberCredentialsPref(isChecked: Boolean)
    fun getUserToken(): Flow<String>
    suspend fun saveTokenPref(token: String)
}