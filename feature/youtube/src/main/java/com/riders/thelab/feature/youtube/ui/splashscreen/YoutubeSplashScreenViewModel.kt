package com.riders.thelab.feature.youtube.ui.splashscreen

import com.riders.thelab.core.data.IRepository
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@HiltViewModel
class YoutubeSplashScreenViewModel @Inject constructor(
    private val repository: IRepository
) : BaseViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + Job()

    //////////////////////////////////////////
    // Coroutines
    //////////////////////////////////////////
    private val coroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            Timber.e("coroutineExceptionHandler | ${throwable.message}")
        }

    ////////////////////////////////////////
    //
    // OVERRIDE
    //
    ////////////////////////////////////////
    init {
        runBlocking(coroutineContext + coroutineExceptionHandler) {
            repository.isActivitiesSplashScreenEnabled().first()
        }.also {
            Timber.d("init | isActivitiesSplashScreenEnabled() | is enabled value: $it")
            updateActivitiesSplashEnabled(it)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Timber.e("onCleared()")
    }
}