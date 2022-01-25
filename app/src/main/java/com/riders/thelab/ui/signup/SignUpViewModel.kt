package com.riders.thelab.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riders.thelab.data.IRepository
import com.riders.thelab.data.remote.dto.UserDto
import com.riders.thelab.data.remote.dto.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: IRepository
) : ViewModel() {

    private val shouldShowHideLoading: MutableLiveData<Boolean> = MutableLiveData()
    private val shouldEnableDisableUI: MutableLiveData<Boolean> = MutableLiveData()
    private val shouldShowSuccessCreditCard: MutableLiveData<Boolean> = MutableLiveData()
    private val shouldShowFailedCreditCard: MutableLiveData<Boolean> = MutableLiveData()

    private val saveUserSuccessful: MutableLiveData<UserResponse> = MutableLiveData()
    private val saveUserError: MutableLiveData<UserResponse> = MutableLiveData()

    private lateinit var currentUser: UserDto

    /////////////////////////////////////
    //
    // OBSERVERS
    //
    /////////////////////////////////////
    fun getShowHideLoading(): LiveData<Boolean> = shouldShowHideLoading
    fun getEnabledDisableUI(): LiveData<Boolean> = shouldEnableDisableUI
    fun getSuccessCreditCard(): LiveData<Boolean> = shouldShowSuccessCreditCard
    fun getFailedCreditCard(): LiveData<Boolean> = shouldShowFailedCreditCard
    fun getSaveUserSuccessful(): LiveData<UserResponse> = saveUserSuccessful
    fun getSaveUserError(): LiveData<UserResponse> = saveUserError


    /////////////////////////////////////
    //
    // CLASSES METHODS
    //
    /////////////////////////////////////
    // Form
    fun setFormUser(
        gender: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        phoneNumber: String,
        dateOfBirth: String
    ) {
        currentUser =
            UserDto(gender, firstName, lastName, email, password, phoneNumber, dateOfBirth)
    }

    // Plan
    fun setUserPremium(isPremium: Boolean) {
        Timber.d("setUserPremium()")
        currentUser.isPremium = isPremium
    }

    // Premium
    fun checkCardValidity() {
        Timber.d("checkCardValidity()")

        shouldShowHideLoading.value = true
        shouldEnableDisableUI.value = false

        viewModelScope.launch {

            delay(TimeUnit.SECONDS.toMillis(2))

            shouldShowHideLoading.value = false
            shouldEnableDisableUI.value = true

            // TODO : set on error to the view
            shouldShowSuccessCreditCard.value = true
        }
    }

    // SignUp
    fun saveUser() {
        Timber.d("saveUser()")

        // TODO : Remove when done
        currentUser.isCustomer = true
        currentUser.isProvider = false

        viewModelScope.launch(ioContext) {
            try {
                supervisorScope {
                    val saveResponse = repository.saveUser(currentUser)
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


    companion object {
        val ioContext = Dispatchers.IO + Job()
        val mainContext = Dispatchers.Main + Job()
    }
}