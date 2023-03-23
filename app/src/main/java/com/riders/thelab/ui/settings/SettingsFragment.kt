package com.riders.thelab.ui.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.riders.thelab.R
import timber.log.Timber

/** Configures App settings.  */
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(bundle: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.the_lab_preferences, rootKey)
        setupPreferences()
    }

    private fun setupPreferences() {
        val darkModeSettings: SwitchPreferenceCompat? =
            findPreference(getString(R.string.pref_key_dark_mode))
        darkModeSettings?.setOnPreferenceChangeListener { preference, newValue ->
            Timber.e("$preference + $newValue")
            true
        }

        val wipeDataSettings: Preference? =
            findPreference(getString(R.string.pref_key_wipe_data))
        wipeDataSettings?.setOnPreferenceClickListener {
            Timber.e("$it clicked")
            Timber.e("Should ask user for delete all preferences")
            true
        }

    }
}