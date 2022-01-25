package com.riders.thelab.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riders.thelab.data.IRepository
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

    private val login: MutableLiveData<LoginResponse> = MutableLiveData()

    ///////////////
    //
    // Observers
    //
    ///////////////
    fun getLogin(): LiveData<LoginResponse> = login


    ///////////////
    //
    // Functions
    //
    ///////////////
    fun makeCallLogin(user: UserDto) {
        Timber.d("makeCallLogin()")
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
    fun saveUserDataInDataStore(){

    }

    companion object {
        val ioContext = Dispatchers.IO + Job()
        val mainContext = Dispatchers.Main + Job()
    }
}