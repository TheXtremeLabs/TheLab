package com.riders.thelab.ui.login

import android.util.Patterns
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.riders.thelab.BuildConfig
import com.riders.thelab.core.common.utils.encodeToSha256
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.model.compose.LoginFieldsUIState
import com.riders.thelab.core.data.local.model.compose.LoginUiState
import com.riders.thelab.core.data.remote.dto.ApiResponse
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: IRepository
) : BaseViewModel() {
    val list = listOf("test", "mike", "chronopost", "john")

    //////////////////////////////////////////
    // DataStore
    //////////////////////////////////////////
    private val dataStoreEmail = repository.getEmailPref().asLiveData()
    private val dataStorePassword = repository.getPasswordPref().asLiveData()
    private val dataStoreRememberCredentials = repository.isRememberCredentialsPref().asLiveData()

    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    // Login States
    // User Login State
    private var _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.None)
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

    // Login field state
    private var _loginFieldUiState =
        MutableStateFlow<LoginFieldsUIState.Login>(LoginFieldsUIState.Login.Idle)
    val loginFieldUiState: StateFlow<LoginFieldsUIState.Login> = _loginFieldUiState

    // Password field state
    private var _passwordFieldUiState =
        MutableStateFlow<LoginFieldsUIState.Password>(LoginFieldsUIState.Password.Idle)
    val passwordFieldUiState: StateFlow<LoginFieldsUIState.Password> = _passwordFieldUiState

    // Backing property to avoid state updates from other classes
    private val _networkState: MutableStateFlow<NetworkState> =
        MutableStateFlow(NetworkState.Disconnected(true))

    // The UI collects from this StateFlow to get its state updates
    val networkState: StateFlow<NetworkState> = _networkState

    var login by mutableStateOf(if (BuildConfig.DEBUG) "jane.doe@test.com" else "")
        private set
    var password by mutableStateOf(if (BuildConfig.DEBUG) "test1234" else "")
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
        // !Patterns.EMAIL_ADDRESS.matcher(login).matches()
        login.isBlank()
    }

    var isRememberCredentials: Boolean by mutableStateOf(false)
        private set


    ////////////////////////////////////////
    // Composable methods
    ////////////////////////////////////////
    private fun updateLoginUiState(newState: LoginUiState) {
        _loginUiState.value = newState
    }

    fun updateLoginFieldUiState(newLoginFieldState: LoginFieldsUIState.Login) {
        _loginFieldUiState.value = newLoginFieldState
    }

    fun updatePasswordFieldUiState(newPasswordFieldState: LoginFieldsUIState.Password) {
        _passwordFieldUiState.value = newPasswordFieldState
    }

    private fun updateNetworkState(newState: NetworkState) {
        _networkState.value = newState
    }

    fun updateLogin(value: String) {
        login = value
    }

    fun updatePassword(value: String) {
        password = value
    }

    fun updateIsRememberCredentials(remember: Boolean) {
        this.isRememberCredentials = remember

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            repository.saveRememberCredentialsPref(
                remember
            )
        }
    }

    fun isUsernameAvailable(login: String): Boolean = list.contains(login)

    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e("coroutineExceptionHandler | ${throwable.message}")

            updateNetworkState(NetworkState.Disconnected(true))

            val cs404: CharSequence = "404".subSequence(0, 3)
            val cs503: CharSequence = "503".subSequence(0, 3)
            if (throwable.message?.contains(cs404, true) == true
                || throwable.message?.contains(cs503, true) == true
            ) {
                updateLoginUiState(LoginUiState.Error(ApiResponse("", 404, null)))
            } else {
                // Default error
                updateLoginUiState(
                    LoginUiState.Error(
                        ApiResponse(
                            "Error while logging user. Please verify your credentials",
                            404,
                            null
                        )
                    )
                )
            }
        }


    ///////////////
    //
    // OVERRIDE
    //
    ///////////////
    init {
        if (true == dataStoreRememberCredentials.value) {
            dataStoreEmail.value?.let { updateLogin(it) }
            dataStorePassword.value?.let { updatePassword(it) }
            dataStoreRememberCredentials.value?.let { updateIsRememberCredentials(it) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Timber.e("onCleared()")
    }


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

        updateLoginUiState(LoginUiState.Connecting)

        logUser(login, password)
    }


    private fun isValidLogin(): Boolean =
        (login.trim().isNotEmpty()
                && Patterns.EMAIL_ADDRESS.matcher(login).matches()) ||
                (login.trim().isNotEmpty() && login.length >= 2)

    private fun isValidPassword(): Boolean = password.trim().isNotEmpty() && password.length >= 4

    fun getApi() {
        Timber.d("getApi()")
        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            Timber.d("getApi()")
            val response = repository.getApi()
            Timber.d("$response")

            updateNetworkState(NetworkState.Available(true))
        }
    }

    private fun logUser(usernameOrEmail: String, password: String) {
        Timber.d("logUser() | username Or Email: $usernameOrEmail, password:$password")

        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {

            // Simulate long-time running operation
            delay(1_500)

            repository.logUser(usernameOrEmail, password.encodeToSha256())?.let {
                Timber.d("user found: $it")
                if (isRememberCredentials) {
                    saveUserDataInDataStore(usernameOrEmail, password)
                }
                updateLoginUiState(LoginUiState.UserSuccess(it))
            } ?: run {
                Timber.e("user object is null. Unable to get user, please make sure to enter a valid username or email with a valid password.")
                updateLoginUiState(
                    LoginUiState.UserError("user object is null. Unable to get user, please make sure to enter a valid username or email with a valid password.")
                )
            }
        }
    }

    private fun saveUserDataInDataStore(email: String, password: String) =
        runCatching {
            Timber.d("saveUserDataInDataStore() | runCatching")
            viewModelScope.launch(Dispatchers.IO) {
                repository.saveEmailPref(email)
                repository.savePasswordPref(password)
            }
        }
            .onFailure {
                Timber.e("runCatching | onFailure | error caught with message: ${it.message}")
            }
}

fun String.isLetterOrDigits(): Boolean = this.matches("^[a-zA-Z0-9]*$".toRegex())