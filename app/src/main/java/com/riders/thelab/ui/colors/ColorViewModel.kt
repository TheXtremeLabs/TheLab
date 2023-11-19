package com.riders.thelab.ui.colors

import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ColorViewModel @Inject constructor(private val repository: IRepository) : BaseViewModel() {
    init {
        viewModelScope.launch {
            repository.isNightMode().collect {
                updateDarkMode(it)
            }
        }
    }
}