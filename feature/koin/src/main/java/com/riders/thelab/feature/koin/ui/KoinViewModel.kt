package com.riders.thelab.feature.koin.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.riders.thelab.core.data.utils.Resource
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import com.riders.thelab.feature.koin.data.RepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import timber.log.Timber

@KoinViewModel
class KoinViewModel(private val repository: RepositoryImpl) : BaseViewModel(), DefaultLifecycleObserver {

    var htmlContent: String by mutableStateOf("")

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        fetchGoogle()
    }


    fun fetchGoogle() {
        Timber.d("fetchGoogle()")

        viewModelScope.launch(Dispatchers.IO) {
            when (val result = repository.fetchGoogle()) {
                is Resource.Error -> {
                    Timber.d("fetchGoogle() | Error: ${result.error}")
                }

                is Resource.Success -> {
                    Timber.d("fetchGoogle() | Response:\nSnippet: ${result.data.take(250)}...\nData length: ${result.data.length}")
                    htmlContent = result.data
                }
            }
        }
    }
}