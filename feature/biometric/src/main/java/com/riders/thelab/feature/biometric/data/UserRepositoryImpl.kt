package com.riders.thelab.feature.biometric.data

import com.riders.thelab.feature.biometric.data.biometric.KeyValueStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import timber.log.Timber
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    //injected only to perform mock authentication
    private val keyValueStorage: KeyValueStorage
) : IUser {

    private val _isUserLoggedIn: MutableStateFlow<Boolean> by lazy {
        MutableStateFlow(false)
    }

    override val isUserLoggedIn: StateFlow<Boolean> by lazy {
        _isUserLoggedIn.asStateFlow()
    }

    override suspend fun login(username: String, password: String) {
        Timber.d("do login")
        _isUserLoggedIn.value = true
    }

    override suspend fun loginWithToken(token: String) {
        val fakeToken = keyValueStorage.getValue(BiometricRepositoryImpl.MOCK_TOKEN_KEY)
        _isUserLoggedIn.value = fakeToken == token
    }

    override suspend fun logout() {
        _isUserLoggedIn.value = false
    }
}