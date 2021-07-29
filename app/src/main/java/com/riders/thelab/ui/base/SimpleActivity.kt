package com.riders.thelab.ui.base

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class SimpleActivity : AppCompatActivity() {

    fun setupToolBar(
        toolbar: MaterialToolbar,
        title: String?,
        subTitle: String?,
        showButtonHome: Boolean
    ) {
        // Set up the layout_toolbar.
        setSupportActionBar(toolbar)
        val ab = supportActionBar
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(showButtonHome)
            ab.setDisplayShowHomeEnabled(showButtonHome)
            ab.title = title
            if (subTitle != null) ab.subtitle = subTitle
        }
        toolbar.setNavigationOnClickListener { view: View? -> onBackPressed() }
    }
}