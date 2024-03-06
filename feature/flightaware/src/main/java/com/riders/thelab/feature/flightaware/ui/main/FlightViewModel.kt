package com.riders.thelab.feature.flightaware.ui.main

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.riders.thelab.core.ui.compose.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FlightViewModel @Inject constructor() : BaseViewModel(), DefaultLifecycleObserver {


    ///////////////////////////////
    //
    // OVERRIDE
    //
    ///////////////////////////////
    override fun onCleared() {
        super.onCleared()
        Timber.e("onCleared()")
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        Timber.d("onCreate()")
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        Timber.d("onStart()")
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        Timber.e("onPause()")
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        Timber.d("onResume()")
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        Timber.e("onStop()")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onStop(owner)
        Timber.e("onDestroy()")

    }
}