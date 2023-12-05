package com.riders.thelab.core.ui.compose.base

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    var isDarkMode: Boolean by mutableStateOf(true)
        private set
    var isVibration: Boolean by mutableStateOf(true)
        private set

    fun toggleDarkMode() {
        isDarkMode = !isDarkMode
    }

    fun updateDarkMode(darkMode: Boolean) {
        this.isDarkMode = darkMode
    }

    fun toggleVibration() {
        isVibration = !isVibration
    }

}