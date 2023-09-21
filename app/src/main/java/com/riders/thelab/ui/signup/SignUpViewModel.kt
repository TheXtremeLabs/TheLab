package com.riders.thelab.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.data.remote.dto.ApiResponse
import com.riders.thelab.core.data.remote.dto.UserDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    private val shouldShowHideLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val shouldEnableDisableUI: MutableLiveData<Boolean> = MutableLiveData()

    private val saveUserSuccessful: MutableLiveData<ApiResponse> = MutableLiveData()
    private val saveUserError: MutableLiveData<ApiResponse> = MutableLiveData()

    private var currentUser: UserDto? = null

    /////////////////////////////////////
    //
    // OBSERVERS
    //
    /////////////////////////////////////
    fun getShowHideLoading(): LiveData<Boolean> = shouldShowHideLoading
    fun getEnabledDisableUI(): LiveData<Boolean> = shouldEnableDisableUI
    fun getSaveUserSuccessful(): LiveData<ApiResponse> = saveUserSuccessful
    fun getSaveUserError(): LiveData<ApiResponse> = saveUserError


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
            viewModelScope.launch(ioContext) {
                try {
                    supervisorScope {
                        Timber.d("repository.saveUser() - $currentUser")
                        val saveResponse = repository.saveUser(currentUser!!)
                        Timber.d("$saveResponse")

                        // Simulate long-time running operation
                        delay(3000)

                        withContext(mainContext) {
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

    companion object {
        val ioContext = Dispatchers.IO + Job()
        val mainContext = Dispatchers.Main + Job()
    }
}