package com.riders.thelab.ui.login

import androidx.lifecycle.*
import com.riders.thelab.data.IRepository
import com.riders.thelab.data.remote.dto.ApiResponse
import com.riders.thelab.data.remote.dto.UserDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    private val login: MutableLiveData<ApiResponse> = MutableLiveData()
    private val dataStoreEmail = repository.getEmailPref().asLiveData()
    private val dataStorePassword = repository.getPasswordPref().asLiveData()
    private val dataStoreRememberCredentials = repository.isRememberCredentialsPref().asLiveData()


    ///////////////
    //
    // Observers
    //
    ///////////////
    fun getLogin(): LiveData<ApiResponse> = login
    fun getDataStoreEmail() = dataStoreEmail
    fun getDataStorePassword() = dataStorePassword
    fun getDataStoreRememberCredentials() = dataStoreRememberCredentials


    ///////////////
    //
    // Functions
    //
    ///////////////
    fun makeCallLogin(email: String, password: String) {
        Timber.d("makeCallLogin() - with $email and $password")

        val encodedPassword: String =
            LoginUtils.encodedHashedPassword(
                LoginUtils.convertToSHA1(password)!!
            )!!

        val user = UserDto(email, password, encodedPassword)

        viewModelScope.launch(ioContext) {
            try {
                supervisorScope {
                    val response = repository.login(user)
                    Timber.d("$response")

                    withContext(mainContext) {
                        login.value = response
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e(e.message)
            }
        }
    }

    fun saveUserDataInDataStore(email: String, password: String, isChecked: Boolean) {
        try {
            viewModelScope.launch(ioContext) {
                repository.saveEmailPref(email)
                repository.savePasswordPref(password)
                repository.saveRememberCredentialsPref(isChecked)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    companion object {
        val ioContext = Dispatchers.IO + Job()
        val mainContext = Dispatchers.Main + Job()
    }
}