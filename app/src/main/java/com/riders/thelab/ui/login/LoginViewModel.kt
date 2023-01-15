package com.riders.thelab.ui.login


import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.riders.thelab.data.IRepository
import com.riders.thelab.data.local.model.compose.LoginUiState
import com.riders.thelab.data.remote.dto.ApiResponse
import com.riders.thelab.data.remote.dto.UserDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    //////////////////////////////////////////
    // DataStore
    //////////////////////////////////////////
    private val dataStoreEmail = repository.getEmailPref().asLiveData()
    private val dataStorePassword = repository.getPasswordPref().asLiveData()
    private val dataStoreRememberCredentials = repository.isRememberCredentialsPref().asLiveData()

    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    var login = mutableStateOf("")
    var password = mutableStateOf("")
    fun updateLogin(value: String) {
        login.value = value
    }

    fun updatePassword(value: String) {
        password.value = value
    }

    private var _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.None)
    val loginUiState = _loginUiState

    // Backing property to avoid state updates from other classes
    private val _networkState: MutableStateFlow<NetworkState> =
        MutableStateFlow(NetworkState.Disconnected(true))

    // The UI collects from this StateFlow to get its state updates
    val networkState: StateFlow<NetworkState> = _networkState

    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { coroutineContext, throwable ->
            throwable.printStackTrace()
            Timber.e(throwable.message)

            _networkState.value = NetworkState.Disconnected(true)

            val cs404: CharSequence = "404".subSequence(0, 3)
            val cs503: CharSequence = "503".subSequence(0, 3)
            if (throwable.message?.contains(cs404, true) == true
                || throwable.message?.contains(cs503, true) == true
            ) {
                _loginUiState.value = LoginUiState.Error(ApiResponse("", 404, null))
            }
        }

    init {
        viewModelScope.launch(IO + SupervisorJob() + coroutineExceptionHandler) {
            //try {
              //  supervisorScope {
                    Timber.d("getApi()")
                    val response = repository.getApi()
                    Timber.d("$response")

                    _networkState.value = NetworkState.Available(true)
                }
            /*} catch (e: Exception) {
                e.printStackTrace()
                Timber.e(e.message)

                _networkState.value = NetworkState.Disconnected(true)
            }
        }*/
    }


    ///////////////
    //
    // Observers
    //
    ///////////////
    fun getDataStoreEmail() = dataStoreEmail
    fun getDataStorePassword() = dataStorePassword
    fun getDataStoreRememberCredentials() = dataStoreRememberCredentials


    ///////////////
    //
    // Functions
    //
    ///////////////
    fun login() {
        Timber.d("login()")
        if (!isValidLogin()) {
            Timber.e("!isValidLogin()")
            return
        }
        if (!isValidPassword()) {
            Timber.e("!isValidPassword()")
            return
        }

        _loginUiState.value = LoginUiState.Loading

        makeCallLogin(login.value, password.value)
    }


    fun isValidLogin() =
        login.value.trim().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(login.value).matches()

    fun isValidPassword() = password.value.trim().isNotEmpty() && password.value.length >= 4

    fun makeCallLogin(email: String, password: String) {
        Timber.d("makeCallLogin() - with $email and $password")

        val encodedPassword: String =
            LoginUtils.encodedHashedPassword(
                LoginUtils.convertToSHA1(password)!!
            )!!

        val user = UserDto(email, password, encodedPassword)

        viewModelScope.launch(IO + SupervisorJob() + coroutineExceptionHandler) {
            delay(2500L)

            /*try {
                supervisorScope {*/
                    /*val response = repository.login(user)
                    Timber.d("$response")

                    _loginUiState.value = LoginUiState.Success(response)*/

                    //TODO : Due to Heroku back-end free services ending,
                    // Use of the database to store and log users

                    // Force response
                    _loginUiState.value = LoginUiState.Error(ApiResponse("", 404, null))
               // }
            /*} catch (e: Exception) {
                e.printStackTrace()
                Timber.e(e.message)*/

                /*val cs404: CharSequence = "404".subSequence(0, 3)
                val cs503: CharSequence = "503".subSequence(0, 3)
                if (e.message?.contains(cs404, true) == true
                    || e.message?.contains(cs503, true) == true
                ) {
                    _loginUiState.value = LoginUiState.Error(ApiResponse("", 404, null))
                }*/
            //}
        }
    }

    fun saveUserDataInDataStore(email: String, password: String, isChecked: Boolean) {
        try {
            viewModelScope.launch(IO) {
                repository.saveEmailPref(email)
                repository.savePasswordPref(password)
                repository.saveRememberCredentialsPref(isChecked)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}