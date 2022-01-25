package com.riders.thelab.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riders.thelab.data.IRepository
import com.riders.thelab.data.remote.dto.ApiResponse
import com.riders.thelab.data.remote.dto.LoginResponse
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

    ///////////////
    //
    // Observers
    //
    ///////////////
    fun getLogin(): LiveData<ApiResponse> = login


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

        val user = UserDto(email ,password, encodedPassword)

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

    // TODO : save user data in Datastore in order to log faster
    fun saveUserDataInDataStore() {

    }

    companion object {
        val ioContext = Dispatchers.IO + Job()
        val mainContext = Dispatchers.Main + Job()
    }
}