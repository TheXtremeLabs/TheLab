package com.riders.thelab.ui.login


import android.util.Patterns
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.riders.thelab.data.IRepository
import com.riders.thelab.data.local.model.compose.LoginUiState
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

    private val loginldata: MutableLiveData<ApiResponse> = MutableLiveData()
    private val loginError: MutableLiveData<ApiResponse> = MutableLiveData()

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
    fun getLogin(): LiveData<ApiResponse> = loginldata
    fun getLoginError(): LiveData<ApiResponse> = loginError
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

        viewModelScope.launch {
            delay(2500L)
            makeCallLogin(login.value, password.value)
        }
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

        viewModelScope.launch(ioContext) {
            try {
                supervisorScope {
                    val response = repository.login(user)
                    Timber.d("$response")

                    _loginUiState.value = LoginUiState.Success(response)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Timber.e(e.message)

                val cs404: CharSequence = "404".subSequence(0, 3)
                val cs503: CharSequence = "503".subSequence(0, 3)
                if (e.message?.contains(cs404, true) == true
                    || e.message?.contains(cs503, true) == true) {
                    _loginUiState.value = LoginUiState.Error(ApiResponse("", 404, null))
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