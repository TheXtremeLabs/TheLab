package com.riders.thelab.feature.settings

import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: IRepository) : BaseViewModel() {
    init {
        viewModelScope.launch {
            repository.isNightMode().collect {
                updateDarkMode(it)
            }
        }
    }

    fun updateDarkModeDatastore(isDarkMode: Boolean) {
        viewModelScope.launch {
            repository.toggleNightMode()
        }
    }
}