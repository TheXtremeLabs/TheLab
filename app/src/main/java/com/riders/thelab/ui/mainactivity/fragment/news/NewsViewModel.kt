package com.riders.thelab.ui.mainactivity.fragment.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riders.thelab.data.local.model.app.App
import com.riders.thelab.utils.Constants
import java.util.*

class NewsViewModel : ViewModel() {

    var recentApps: MutableLiveData<List<App>> = MutableLiveData()

    fun getRecentApps(): LiveData<List<App>> {
        return recentApps
    }

    fun fetchRecentApps(recentAppsNames: Array<String>) {
        val recentAppList: List<App>? = null

        // Setup last 3 features added
        for (element in Constants.getInstance().getActivityList()) {
            for (item in recentAppsNames) {
                if (element.appTitle?.contains(item) == true)
                    (recentAppList as ArrayList<App>).add(element)
            }
        }

        recentApps.value = recentAppList
    }
}