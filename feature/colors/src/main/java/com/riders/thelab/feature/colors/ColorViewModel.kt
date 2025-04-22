package com.riders.thelab.feature.colors

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.data.local.IUiRepository
import com.riders.thelab.core.ui.utils.LabColorsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Suppress("EmptyMethod")
@HiltViewModel
class ColorViewModel @Inject constructor(val uiRepository: IUiRepository) : BaseViewModel() {

    var randomColor: Int by mutableIntStateOf(LabColorsManager.getRandomColor())
        private set

    fun updateRandomColor() {
        this.randomColor = LabColorsManager.getRandomColor(this.randomColor)
    }
}