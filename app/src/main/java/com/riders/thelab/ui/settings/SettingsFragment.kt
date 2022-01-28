package com.riders.thelab.ui.settings

import android.hardware.Camera
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.riders.thelab.R
import com.riders.thelab.ui.googlemlkit.camera.CameraSource
import java.util.HashMap

/** Configures App settings.  */
class SettingsFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(bundle: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.the_lab_preferences, rootKey)

    }

}