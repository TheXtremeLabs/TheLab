package com.riders.thelab.core.common.extensions

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Parcelable
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import java.io.Serializable


@SuppressLint("NewApi")
inline fun <reified T : Parcelable> Intent.getParcelableArrayExtras(
    key: String
): Array<out Parcelable>? = if (!LabCompatibilityManager.isTiramisu()) {
    @Suppress("DEPRECATION")
    this.getParcelableArrayExtra(key)
} else {
    @SuppressLint("NewApi")
    this.getParcelableArrayExtra<T>(
        key,
        T::class.java
    )
}

@SuppressLint("NewApi")
inline fun <reified T> Intent.getParcelableExtras(
    key: String
): T? = if (!LabCompatibilityManager.isTiramisu()) {
    @Suppress("DEPRECATION")
    this.getParcelableExtra(key)
} else {
    @SuppressLint("NewApi")
    this.getParcelableExtra<T>(
        key,
        T::class.java
    )
}

@SuppressLint("NewApi")
inline fun <reified T : Serializable> Intent.getSerializableExtra(
    key: String
): T? = if (!LabCompatibilityManager.isTiramisu()) {
    @Suppress("DEPRECATION")
    this.getSerializableExtra(key)
} else {
    @SuppressLint("NewApi")
    this.getSerializableExtra(
        key,
        T::class.java
    )
} as T?