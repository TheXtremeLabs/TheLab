package com.riders.thelab.core.common.bean

import android.os.Build
import androidx.annotation.RequiresApi
import timber.log.Timber

enum class AndroidReleases(val osVersionName: String) {

    M("Marshmallow"),
    N("Nougat"),
    N_("Nougat++"),
    O("Oreo"),
    O_("Oreo++"),
    P("Pie"),
    Q("Android 10"),
    R("Red Velvet Cake"),
    S("Snow Cone"),
    Sv2("Snow Cone v2"),
    T("Tiramisu"),
    UPSIDE_DOWN_CAKE("Upside Down Cake");

    companion object {

        /**
         * Gets the version name from version code. Note! Needs to be updated
         * when new versions arrive, or will return a single letter. Like Android 8.0 - Oreo
         * yields "O" as a version name.
         *
         * @return version name of device's OS name
         */
        @RequiresApi(api = Build.VERSION_CODES.O)
        fun getOsVersionName(name: String): String = entries.run {
            Timber.d("getOsVersionName() | name: $name")
            this.first { it.name == name }.osVersionName ?: "UNKNOWN"
        }
    }
}