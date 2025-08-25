package com.riders.thelab.feature.home

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.riders.thelab.core.data.local.model.app.App
import com.riders.thelab.core.data.local.model.app.LocalApp
import com.riders.thelab.core.ui.data.local.UiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import java.lang.ref.WeakReference
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    val uiRepository: UiRepository
) : ViewModel(), DefaultLifecycleObserver {

    private var mWeakReference: WeakReference<HomeActivity>? = null

    private var _appList: MutableStateFlow<List<LocalApp>> = MutableStateFlow(emptyList())
    val appList = _appList

    fun updateLocalApps(localApps: List<LocalApp>) {
        this._appList.update { localApps }
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)

        mWeakReference?.get()?.let { activity ->
            activity
                .getActivityList(activity.isTv)
                .map { it as LocalApp }
                .sortedByDescending { it.localDate }
                .let { appList -> updateLocalApps(appList) }
        } ?: run {
            Timber.e("onStart() | Unable to get weak reference activity. Object is null")
        }
    }

    fun initWeakReference(activity: HomeActivity) {
        if (null == mWeakReference) {
            mWeakReference = WeakReference(activity)
        }
    }

    /**
     * @return the list of activities developed in TheLab App
     */
    private fun Context.getActivityList(isForTv: Boolean): List<App> = AppBuilderUtils
        .getInstance(this)
        .run {
            if (isForTv) {
                getTVActivities(this@getActivityList)
            } else {
                getMobileActivities(this@getActivityList)
            }
        }
}