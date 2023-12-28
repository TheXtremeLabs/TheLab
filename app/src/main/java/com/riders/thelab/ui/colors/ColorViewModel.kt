package com.riders.thelab.ui.colors

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.utils.LabColorsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ColorViewModel @Inject constructor(private val repository: IRepository) : BaseViewModel() {

    var randomColor: Int by mutableStateOf(LabColorsManager.getRandomColor())
        private set

    fun updateRandomColor() {
        this.randomColor = LabColorsManager.getRandomColor(this.randomColor)

    }

    init {
        viewModelScope.launch {
            repository.isNightMode().collect {
                updateDarkMode(it)
            }
        }
    }
}