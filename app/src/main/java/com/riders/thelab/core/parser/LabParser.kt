package com.riders.thelab.core.parser

import com.riders.thelab.data.local.model.weather.CitiesEventJsonAdapter
import com.riders.thelab.data.remote.dto.weather.City
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.lang.reflect.Type

object LabParser {

    fun parseJsonFileListWithKotlinSerialization(jsonToParse: String): List<City> {
        return Json.decodeFromString<List<City>>(jsonToParse)
    }

    fun parseJsonFileListWithMoshi(jsonToParse: String): List<City>? {
        return try {
            Timber.d("Build Moshi adapter and build object...")
            // Step 2 convert to class object
            val moshi = Moshi.Builder()
                .add(CitiesEventJsonAdapter())
                .build()
            val type: Type = Types.newParameterizedType(MutableList::class.java, City::class.java)
            val jsonAdapter: JsonAdapter<List<City>> = moshi.adapter(type)
            jsonAdapter.fromJson(jsonToParse)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


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