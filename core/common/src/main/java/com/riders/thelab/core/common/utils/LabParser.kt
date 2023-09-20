package com.riders.thelab.core.common.utils

import android.content.Context
import kotlinx.serialization.json.Json
import timber.log.Timber

object LabParser {
    inline fun <reified T> parseJsonFile(
        context: Context,
        filename: String
    ): T? = runCatching {
        Timber.d("parseJsonFile() | filename: $filename")

        val json = context.assets.open(filename).bufferedReader().use { it.readText() }
        val mRObject: T = Json.decodeFromString(json)

        if (null == mRObject) {
            Timber.e("List is null. Return emptyList")
            return null
        }

        return mRObject
    }
        .onFailure {
            Timber.e("runCatching - onFailure() | Error caught: ${it.message}")
        }
        .onSuccess {
            Timber.d("runCatching - onSuccess() | app list fetched successfully")
        }
        .getOrThrow<T>()

    inline fun <reified T> parseJsonFile(jsonContent: String): T? = runCatching {
        Timber.d("parseJsonFile() | content length: ${jsonContent.length}")

        val mRObject: T = Json.decodeFromString(jsonContent)

        if (null == mRObject) {
            Timber.e("List is null. Return emptyList")
            return null
        }

        return mRObject
    }
        .onFailure {
            Timber.e("runCatching - onFailure() | Error caught: ${it.message}")
        }
        .onSuccess {
            Timber.d("runCatching - onSuccess() | app list fetched successfully")
        }
        .getOrThrow<T>()


    // TODO : try to generify class
    /*
    public <T> T parseJsonFileWithMoshi(Class<T> targetType, Class<P> targetObject, String jsonToParse) {
        try {
            Timber.d("Build Moshi adapter and build object...");
            // Step 2 convert to class object
            Moshi moshi =
                    new Moshi.Builder()
                            .add(new CitiesEventJsonAdapter())
                            .build();

            Type type = Types.newParameterizedType(targetType, targetObject);
            JsonAdapter<T> jsonAdapter = moshi.adapter(type);

            return (T) jsonAdapter.fromJson(jsonToParse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> List<P> parseJsonFileListWithMoshi(Class<T> targetType, Class<P> targetObject, String jsonToParse) {
        try {
            Timber.d("Build Moshi adapter and build object...");
            // Step 2 convert to class object
            Moshi moshi =
                    new Moshi.Builder()
                            .add(new CitiesEventJsonAdapter())
                            .build();

            Type type = Types.newParameterizedType(targetType, targetObject);
            JsonAdapter<P> jsonAdapter = moshi.adapter(type);

            return (List<P>) jsonAdapter.fromJson(jsonToParse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/
}