package com.riders.thelab.ui.mainactivity

import android.view.View
import com.riders.thelab.data.local.model.app.App

interface MainActivityAppClickListener {

    fun onAppItemClickListener(cardView: View, item: App)

    fun onAppItemCLickListener(view: View, item: App, position: Int)
}