package com.riders.thelab.feature.kat.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class KatProfileViewModel : ViewModel() {


    /////////////////////////
    // Composable States
    /////////////////////////
    var profileCurrentUsername: String by mutableStateOf("")
        private set
    var profileUsername: String by mutableStateOf("")
        private set
    var profilePhoneNumber: String by mutableStateOf("")
        private set


    fun updateCurrentProfileUsername(currentUsername: String) {
        this.profileCurrentUsername = currentUsername
    }

    fun updateProfileUsername(newUsername: String) {
        this.profileUsername = newUsername
    }

    fun updateProfilePhone(newPhone: String) {
        this.profilePhoneNumber = newPhone
    }

}