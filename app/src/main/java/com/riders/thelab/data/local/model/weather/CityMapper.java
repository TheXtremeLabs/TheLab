package com.riders.thelab.data.local.model.weather;

import android.annotation.SuppressLint;

import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.data.remote.dto.weather.City;

import java.util.ArrayList;
import java.util.List;

public class CityMapper {

    @SuppressLint("NewApi")
    public static List<CityModel> getCityList(List<City> dtoCities) {
        List<CityModel> listToReturn = new ArrayList<>();

        if (!LabCompatibilityManager.isNougat()) {
            // Cannot use streams on list
            // Use common method

            for (City dtoItem : dtoCities) {
                listToReturn.add(new CityModel(dtoItem));
            }

        } else {
            // Use stream method
            dtoCities
                    .stream()
                    .forEach(dtoCity -> listToReturn.add(new CityModel(dtoCity)));
        }

        return listToReturn;
    }
}
