package com.riders.thelab.core.parser;

import com.riders.thelab.data.local.model.CitiesEventJsonAdapter;
import com.riders.thelab.data.remote.dto.weather.City;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.lang.reflect.Type;
import java.util.List;

import timber.log.Timber;

public class LabParser {

    private static LabParser mInstance;

    private LabParser() {
    }

    public static LabParser getInstance() {
        if (null == mInstance) {
            mInstance = new LabParser();

        }
        return mInstance;
    }


    public List<City> parseJsonFileListWithMoshi(String jsonToParse) {
        try {
            Timber.d("Build Moshi adapter and build object...");
            // Step 2 convert to class object
            Moshi moshi =
                    new Moshi.Builder()
                            .add(new CitiesEventJsonAdapter())
                            .build();

            Type type = Types.newParameterizedType(List.class, City.class);
            JsonAdapter<List<City>> jsonAdapter = moshi.adapter(type);
            return jsonAdapter.fromJson(jsonToParse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
