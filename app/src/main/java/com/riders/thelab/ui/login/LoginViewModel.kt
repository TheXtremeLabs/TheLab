package com.riders.thelab.ui.login

import android.util.Patterns
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.riders.thelab.BuildConfig
import com.riders.thelab.data.IRepository
import com.riders.thelab.data.local.model.compose.LoginUiState
import com.riders.thelab.data.remote.dto.ApiResponse
import com.riders.thelab.data.remote.dto.UserDto
import com.riders.thelab.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: IRepository
) : BaseViewModel() {
    val list = listOf<String>("test", "mike", "chronopost", "john")

    //////////////////////////////////////////
    // DataStore
    //////////////////////////////////////////
    private val dataStoreEmail = repository.getEmailPref().asLiveData()
    private val dataStorePassword = repository.getPasswordPref().asLiveData()
    private val dataStoreRememberCredentials = repository.isRememberCredentialsPref().asLiveData()

    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    var login by mutableStateOf(if (BuildConfig.DEBUG) "test@test.fr" else "")
        private set
    var password by mutableStateOf(if (BuildConfig.DEBUG) "test12356" else "")
        private set

    @OptIn(ExperimentalCoroutinesApi::class)
    val loginHasError: StateFlow<Boolean> =
        snapshotFlow { login }
            .mapLatest {
                // isUsernameAvailable(it)
                list.contains(login)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false
            )

    val loginHasLocalError by derivedStateOf {
        // synchronous call
        !Patterns.EMAIL_ADDRESS.matcher(login).matches()
    }

    private var _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.None)
    val loginUiState = _loginUiState

    // Backing property to avoid state updates from other classes
    private val _networkState: MutableStateFlow<NetworkState> =
        MutableStateFlow(NetworkState.Disconnected(true))

    // The UI collects from this StateFlow to get its state updates
    val networkState: StateFlow<NetworkState> = _networkState


    ////////////////////////////////////////
    // Composable methods
    ////////////////////////////////////////
    fun updateLogin(value: String) {
        login = value
    }

    fun updatePassword(value: String) {
        password = value
    }

    fun isUsernameAvailable(login: String): Boolean = list.contains(login)

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

    /**
     * logging in user. Will make http post request with name, email
     * as parameters
     */
    fun login() {
        Timber.d("login()")

        if (loginHasError.value) {
            Timber.e("loginHasError.value")
            return
        }
        if (loginHasLocalError) {
            Timber.e("loginHasLocalError")
            return
        }

        if (!isValidLogin()) {
            Timber.e("!isValidLogin()")
            return
        }
        if (!isValidPassword()) {
            Timber.e("!isValidPassword()")
            return
        }

        _loginUiState.value = LoginUiState.Loading

        makeCallLogin(login, password)
    }


    fun isValidLogin() =
        login.trim().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(login).matches()

    fun isValidPassword() = password.trim().isNotEmpty() && password.length >= 4

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

fun String.isLetterOrDigits(): Boolean = this.matches("^[a-zA-Z0-9]*$".toRegex())