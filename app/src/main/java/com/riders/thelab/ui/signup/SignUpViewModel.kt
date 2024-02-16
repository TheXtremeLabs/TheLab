package com.riders.thelab.ui.signup

import android.content.Context
import android.util.Patterns
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDestination
import com.riders.thelab.BuildConfig
import com.riders.thelab.core.common.utils.encodeToSha256
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.model.User
import com.riders.thelab.core.data.local.model.compose.UserState
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.utils.UIManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@Suppress("EmptyMethod")
@HiltViewModel
class SignUpViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val repository: IRepository
) : BaseViewModel() {
    /////////////////////////////////////
    // Composable states
    /////////////////////////////////////
    private var _userState: MutableStateFlow<UserState> = MutableStateFlow(UserState.NotSaved)
    val userState: StateFlow<UserState> = _userState
    var currentDestination: NavDestination? by mutableStateOf(null)
        private set

    var firstname: String by mutableStateOf("")
        private set
    var lastname: String by mutableStateOf("")
        private set
    var username: String by mutableStateOf("")
        private set
    var email: String by mutableStateOf("")
        private set
    var password: String by mutableStateOf("")
        private set
    var passwordConfirmation: String by mutableStateOf("")
        private set

    val userFormButtonEnabled: Boolean by derivedStateOf {
        firstname.isNotBlank() || lastname.isNotBlank() || username.isNotBlank() || email.isNotBlank() || password.isNotBlank() || passwordConfirmation.isNotBlank() && password == passwordConfirmation
    }

    var isSubmitSuccess: Boolean by mutableStateOf(false)
        private set

    // val passwordsHasError: Boolean by derivedStateOf { password != passwordConfirmation }
    @OptIn(ExperimentalCoroutinesApi::class)
    val emailHasError: StateFlow<Boolean> =
        snapshotFlow { email }
            .mapLatest {
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false
            )

    @OptIn(ExperimentalCoroutinesApi::class)
    val passwordsHasError: StateFlow<Boolean> =
        snapshotFlow { password to passwordConfirmation }
            .mapLatest {
                it.first != it.second
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = false
            )

    var shouldShowSaveOrErrorView: Boolean by mutableStateOf(false)
        private set
    var message: String by mutableStateOf("")
        private set
    var shouldShowExitDialogConfirmation: Boolean by mutableStateOf(false)
        private set

    private fun updateUserState(newState: UserState) {
        this._userState.value = newState
    }

    fun updateCurrentNavDestination(newDestination: NavDestination) {
        Timber.d("updateCurrentNavDestination() | newDestination: ${newDestination.route}")
        this.currentDestination = newDestination
    }

    fun updateFirstname(firstname: String) {
        this.firstname = firstname
    }

    fun updateLastname(lastname: String) {
        this.lastname = lastname
    }

    fun updateEmail(email: String) {
        this.email = email
    }

    fun updateUsername(username: String) {
        this.username = username
    }

    fun updatePassword(password: String) {
        this.password = password
    }

    fun updatePasswordConfirmation(passwordConfirmation: String) {
        this.passwordConfirmation = passwordConfirmation
    }

    fun updateShouldShowSaveOrErrorView(show: Boolean) {
        this.shouldShowSaveOrErrorView = show
    }

    fun updateMessage(message: String) {
        this.message = message
    }

    fun updateShouldShowExitDialogConfirmation(show: Boolean) {
        this.shouldShowExitDialogConfirmation = show
    }

    private fun updateIsSubmitSuccess(isSuccess: Boolean) {
        this.isSubmitSuccess = isSuccess
    }

    init {
        viewModelScope.launch {
            repository.isNightMode().collect {
                updateDarkMode(it)
            }
        }

        if (BuildConfig.DEBUG) {
            mockFields()
        }
    }

    /////////////////////////////////////
    // Coroutine
    /////////////////////////////////////
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            Timber.e("CoroutineExceptionHandler | Error caught with message: ${throwable.message}")
            UIManager.showToast(context, "Error while saving user to Database")
            updateIsSubmitSuccess(false)
        }


    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCleared() {
        super.onCleared()
        Timber.e("onCleared()")
    }

    /////////////////////////////////////
    //
    // CLASSES METHODS
    //
    /////////////////////////////////////
    // SignUp
    private fun mockFields() {
        Timber.d("mockFields()")

        updateFirstname("Jane")
        updateLastname("Jane")
        updateUsername("JaneDoe")
        updateEmail("jane.doe@test.com")
        updatePassword("test1234")
        updatePasswordConfirmation("test1234")
    }

    fun submitForm() {
        Timber.d("submitForm()")

        updateUserState(UserState.Saving)

        if (firstname.isBlank()) {
            Timber.e("firstname is blank")
            return
        }
        if (lastname.isBlank()) {
            Timber.e("lastname is blank")
            return
        }
        if (username.isBlank()) {
            Timber.e("username is blank")
            return
        }
        if (email.isBlank()) {
            Timber.e("email is blank")
            return
        }

        if (!isValidEmail()) {
            Timber.e("!isValidPassword()")
            return
        }
        if (!isValidPassword()) {
            Timber.e("!isValidPassword()")
            return
        }

        val user = User(
            firstname,
            lastname,
            username,
            email,
            password.encodeToSha256(),
            System.currentTimeMillis()
        )

        saveUser(user)
    }

    private fun isValidEmail() =
        email.trim().isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidPassword() = password.trim().isNotEmpty() && password.length >= 4

    private fun saveUser(user: User) {
        Timber.d("saveUser() | user: $user")
        Timber.d("attempt to insert user in database...")
        viewModelScope.launch(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
            // Simulate long-time running operation
            delay(1_500)

            val longIdResult = repository.insertUser(user)
            Timber.d("user save result: $longIdResult")

            repository.setUserLogged(longIdResult.toInt())

            withContext(Dispatchers.Main) {
                if (-1L == longIdResult) {
                    updateUserState(UserState.NotSaved)
                } else {
                    updateUserState(UserState.Saved(longIdResult))
                }
                updateIsSubmitSuccess(-1L != longIdResult)
            }
        }
    }
}