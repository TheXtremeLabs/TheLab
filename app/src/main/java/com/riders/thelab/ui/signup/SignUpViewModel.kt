package com.riders.thelab.ui.signup

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDestination
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.remote.dto.ApiResponse
import com.riders.thelab.core.data.remote.dto.UserDto
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: IRepository
) : BaseViewModel() {
    /////////////////////////////////////
    // Composable states
    /////////////////////////////////////
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
        firstname.isNotBlank() || lastname.isNotBlank() || username.isNotBlank() || email.isNotBlank() || password.isNotBlank() || passwordConfirmation.isNotBlank()
    }
    var shouldShowExitDialogConfirmation: Boolean by mutableStateOf(false)
        private set

    fun updateCurrentNavDestination(newDestination: NavDestination) {
        Timber.d("updateCurrentNavDestination() | newDestination: ${newDestination.route}")
        this.currentDestination = newDestination
    }

    fun updateShouldShowExitDialogConfirmation(show: Boolean) {
        this.shouldShowExitDialogConfirmation = show
    }

    // Display 3 items
    val pagerCount = 3

    val shouldUpdateProgressBar1: Boolean by derivedStateOf { true }
    val shouldUpdateProgressBar2: Boolean by derivedStateOf { true }
    val shouldUpdateProgressBar3: Boolean by derivedStateOf { true }

    private val shouldShowHideLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val shouldEnableDisableUI: MutableLiveData<Boolean> = MutableLiveData()

    private val saveUserSuccessful: MutableLiveData<ApiResponse> = MutableLiveData()
    private val saveUserError: MutableLiveData<ApiResponse> = MutableLiveData()

    private var currentUser: UserDto? = null


    init {
        viewModelScope.launch {
            repository.isNightMode().collect {
                updateDarkMode(it)
            }
        }
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
    // Form
    fun setFormUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String
    ) {
        Timber.d("setFormUser()")
        this.currentUser = UserDto(firstName, lastName, email, password)
    }

    // SignUp
    fun saveUser() {
        Timber.d("saveUser()")

        if (null == currentUser) {
            Timber.e("currentUser == null")
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    supervisorScope {
                        Timber.d("repository.saveUser() - $currentUser")
                        val saveResponse = repository.saveUser(currentUser!!)
                        Timber.d("$saveResponse")

                        // Simulate long-time running operation
                        delay(3_000)

                        withContext(Dispatchers.Main) {
                            if (401 == saveResponse.code) {
                                saveUserError.value = saveResponse
                            }
                            if (201 == saveResponse.code) {
                                saveUserSuccessful.value = saveResponse
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Timber.e(e.message)
                }
            }
        }
    }
}