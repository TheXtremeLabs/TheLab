package com.riders.thelab.ui.login

import androidx.lifecycle.*
import com.riders.thelab.data.IRepository
import com.riders.thelab.data.remote.dto.ApiResponse
import com.riders.thelab.data.remote.dto.UserDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    private val login: MutableLiveData<ApiResponse> = MutableLiveData()
    private val loginError: MutableLiveData<ApiResponse> = MutableLiveData()
    private val dataStoreEmail = repository.getEmailPref().asLiveData()
    private val dataStorePassword = repository.getPasswordPref().asLiveData()
    private val dataStoreRememberCredentials = repository.isRememberCredentialsPref().asLiveData()

    // Backing property to avoid state updates from other classes
    private val _networkState: MutableStateFlow<NetworkState> =
        MutableStateFlow(NetworkState.Disconnected(true))

    // The UI collects from this StateFlow to get its state updates
    val networkState: StateFlow<NetworkState> = _networkState

    init {
        viewModelScope.launch(ioContext) {
            try {
                supervisorScope {
                    Timber.d("getApi()")
                    val response = repository.getApi()
                    Timber.d("$response")

                    _networkState.value = NetworkState.Available(true)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e(e.message)

                _networkState.value = NetworkState.Disconnected(true)
            }
        }
    }


    ///////////////
    //
    // Observers
    //
    ///////////////
    fun getLogin(): LiveData<ApiResponse> = login
    fun getLoginError(): LiveData<ApiResponse> = loginError
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

                val cs: CharSequence = "404".subSequence(0, 3)
                if (e.message?.contains(cs, true) == true) {
                    withContext(mainContext) {
                        loginError.value = ApiResponse("", 404, null)
                    }
                }
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