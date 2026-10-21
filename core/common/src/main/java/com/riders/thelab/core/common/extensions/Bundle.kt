package com.riders.thelab.core.common.extensions

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import java.io.Serializable
import kotlin.jvm.java

@SuppressLint("NewApi")
inline fun <reified T : Parcelable> Bundle.getParcelableArrayExtra(
    key: String
): Array<out Parcelable>? = if (!LabCompatibilityManager.isTiramisu()) {
    @Suppress("DEPRECATION")
    this.getParcelableArray(key)
} else {
    @SuppressLint("NewApi")
    this.getParcelableArray<T>(
        key,
        T::class.java
    )
}

@SuppressLint("NewApi")
inline fun <reified T : Parcelable> Bundle.getParcelableExtra(
    key: String
) = if (!LabCompatibilityManager.isTiramisu()) {
    @Suppress("DEPRECATION")
    getParcelable(key)
} else {
    @SuppressLint("NewApi")
    getParcelable(
        key,
        T::class.java
    )
}


@SuppressLint("NewApi")
inline fun <reified T : Serializable> Bundle.getSerializableExtra(
    key: String
) = if (!LabCompatibilityManager.isTiramisu()) {
    @Suppress("DEPRECATION")
    getSerializable(key)
} else {
    @SuppressLint("NewApi")
    getSerializable(
        key,
        T::class.java
    )
} as T?