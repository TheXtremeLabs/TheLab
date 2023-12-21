package com.riders.thelab.feature.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.local.model.User
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: IRepository) : BaseViewModel() {

    //////////////////////////////////////////
    // Variables
    //////////////////////////////////////////
    val themeOptions: List<String> = listOf("Light", "Dark", "Use System")

    //////////////////////////////////////////
    // Compose states
    //////////////////////////////////////////
    var user: User? by mutableStateOf(null)
        private set

    private fun updateUser(user: User) {
        this.user = user
    }

    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e("coroutineExceptionHandler | ${throwable.message}")
        }

    init {
        viewModelScope.launch {
            repository.isNightMode().collect {
                updateDarkMode(it)
            }

            repository.isVibration().collect {
                updateVibration(it)
            }
        }

        getLoggedUser()
    }


    //////////////////////////////////////////
    //
    // CLASS METHODS
    //
    //////////////////////////////////////////
    fun updateDarkModeDatastore() {
        viewModelScope.launch {
            repository.toggleNightMode()
        }
    }

    fun updateVibrationDatastore() {
        viewModelScope.launch {
            repository.toggleNightMode()
        }
    }

    private fun getLoggedUser() {
        Timber.d("getLoggedUser()")

        val user: User? =
            runBlocking(Dispatchers.IO + SupervisorJob() + coroutineExceptionHandler) {
                repository.getUsersSync().firstOrNull { it.logged }
            }
        user?.let { updateUser(it) }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            repository.getUsersSync().firstOrNull { it.logged }?.let {
                repository.logoutUser(it._id.toInt())
            }
        }
    }
}