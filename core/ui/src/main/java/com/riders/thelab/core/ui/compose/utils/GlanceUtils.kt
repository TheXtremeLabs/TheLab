package com.riders.thelab.core.ui.compose.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.core.content.FileProvider
import androidx.glance.LocalContext
import coil.annotation.ExperimentalCoilApi
import coil.imageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import timber.log.Timber


@Composable
fun stringResource(@StringRes id: Int, vararg args: Any): String {
    return LocalContext.current.getString(id, args)
}


///////////////////////////////////////////////////////
// Get Image from cache
///////////////////////////////////////////////////////
@OptIn(ExperimentalCoilApi::class)
suspend fun String.getIconUri(context: Context): String {
    Timber.d("getIconUri() | icon url: $this")

    val request = ImageRequest.Builder(context)
        .data(this)
        .build()

    // Request the image to be loaded and throw error if it failed
    with(context.imageLoader) {
        diskCache?.remove(this@getIconUri)
        memoryCache?.remove(coil.memory.MemoryCache.Key(this@getIconUri))

        val result = execute(request)
        if (result is ErrorResult) {
            throw result.throwable
        }
    }

    // Get the path of the loaded image from DiskCache.
    val path = context.imageLoader.diskCache?.openSnapshot(this)?.use { snapshot ->
        Timber.i("diskCache?.openSnapshot(iconUrl) | snapshot")
        
        val imageFile = snapshot.data.toFile()
        Timber.i("diskCache?.openSnapshot(iconUrl) | snapshot | file path: ${imageFile.absolutePath}")

        // Use the FileProvider to create a content URI
        val contentUri = FileProvider.getUriForFile(
            context,
            "com.riders.thelab.fileprovider",
            imageFile
        )

        // Find the current launcher everytime to ensure it has read permissions
        val resolveInfo = context.packageManager
            .resolveActivity(
                Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_HOME) },
                PackageManager.MATCH_DEFAULT_ONLY
            )
        val launcherName = resolveInfo?.activityInfo?.packageName
        if (launcherName != null) {
            context.grantUriPermission(
                launcherName,
                contentUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
            )
        }

        Timber.i("return the path | ${contentUri.toString()}")
        // return the path
        contentUri.toString()
    }
    return requireNotNull(path) {
        "Couldn't find cached file"
    }
}