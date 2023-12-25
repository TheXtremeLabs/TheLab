package com.riders.thelab.core.data.preferences

import kotlinx.coroutines.flow.Flow

interface IPreferences {

    fun isNightMode(): Flow<Boolean>
    suspend fun toggleNightMode()
    fun isVibration(): Flow<Boolean>
    suspend fun toggleVibration()

    fun getEmailPref(): Flow<String>
    suspend fun saveEmailPref(email: String)
    fun getPasswordPref(): Flow<String>
    suspend fun savePasswordPref(password: String)
    fun isRememberCredentialsPref(): Flow<Boolean>
    suspend fun saveRememberCredentialsPref(isChecked: Boolean)
    fun getUserToken(): Flow<String>
    suspend fun saveTokenPref(token: String)
}