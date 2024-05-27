package com.riders.thelab.ui.settings

import android.os.Bundle
import com.riders.thelab.R
import com.riders.thelab.core.ui.compose.base.BaseAppCompatActivity

class SettingsActivity : BaseAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, SettingsFragment())
            .commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        backPressed()
        return true
    }

    override fun backPressed() {
        if(supportFragmentManager.popBackStackImmediate()) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }
}