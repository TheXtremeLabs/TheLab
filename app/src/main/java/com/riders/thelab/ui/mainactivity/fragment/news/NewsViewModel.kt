package com.riders.thelab.ui.mainactivity.fragment.news

import android.content.Context
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

    fun fetchRecentApps(context: Context, recentAppsNames: Array<String>) {
        val recentAppList: MutableList<App> = ArrayList()

        val constants = Constants(context)

        // Setup last 3 features added
        for (element in constants.getActivityList()) {
            for (item in recentAppsNames) {
                if (element.appTitle.contains(item))
                    (recentAppList as ArrayList<App>).add(element)
            }
        }

        recentApps.value = recentAppList
    }
}