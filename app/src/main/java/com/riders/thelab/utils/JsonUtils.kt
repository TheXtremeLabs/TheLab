package com.riders.thelab.utils

import android.content.Context
import com.riders.thelab.data.local.model.app.App
import com.riders.thelab.data.local.model.app.LocalApp
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import timber.log.Timber

object JsonUtils {

    fun parseJsonFile(context: Context, filename: String): List<App> = runCatching {
        Timber.d("parseJsonFile() | filename: $filename")

        val json = context.assets.open(filename).bufferedReader().use { it.readText() }
        val mLocalAppList: List<LocalApp> = Json.decodeFromString(json)

        if (null == mLocalAppList) {
            Timber.e("List is null. Return emptyList")
            return arrayListOf<App>()
        }

        return mLocalAppList.map { localApp ->
            LocalApp(
                localApp.id,
                localApp.title!!,
                localApp.description!!,
                LocalApp.getDrawableByName(localApp.icon!!),
                localApp.activity,
                localApp.date!!
            )
        }
    }
        .onFailure {
            Timber.e("runCatching - onFailure() | Error caught: ${it.message}")
        }
        .onSuccess {
            Timber.d("runCatching - onSuccess() | app list fetched successfully")
        }
        .getOrThrow<List<App>>()
}