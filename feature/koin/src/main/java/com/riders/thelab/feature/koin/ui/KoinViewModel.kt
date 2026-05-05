package com.riders.thelab.feature.koin.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.data.utils.Resource
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.core.ui.data.local.IUiRepository
import com.riders.thelab.feature.koin.data.IRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
class KoinViewModel(
    private val repository: IRepository,
    uiRepository: IUiRepository
) : BaseViewModel(uiRepository), DefaultLifecycleObserver {

    var htmlContent: String by mutableStateOf("")

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        fetchGoogle()
    }


    fun fetchGoogle() {
        Timber.d("fetchGoogle()")

        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repository.fetchGoogle()) {
                is Resource.ErrorWithType -> {
                    Timber.d("fetchGoogle() | Error: ${result.error}")
                }

                is Resource.Success -> {
                    Timber.d("fetchGoogle() | Response:\nSnippet: ${result.data.take(250)}...\nData length: ${result.data.length}")
                    htmlContent = result.data
                }

                else -> {
                    Timber.d("fetchGoogle() | Unknown error")
                }
            }
        }
    }
}