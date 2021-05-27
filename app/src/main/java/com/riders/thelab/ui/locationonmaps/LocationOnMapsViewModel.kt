package com.riders.thelab.ui.locationonmaps

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

class LocationOnMapsViewModel : ViewModel() {

    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }
}