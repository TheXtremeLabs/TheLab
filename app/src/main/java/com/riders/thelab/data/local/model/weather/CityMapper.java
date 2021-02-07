package com.riders.thelab.data.local.model.weather;

import com.riders.thelab.data.remote.dto.weather.City;

import java.util.ArrayList;
import java.util.List;

public class CityMapper {

    public static List<CityModel> getCityList(List<City> dtoCities) {
        List<CityModel> listToReturn = new ArrayList<>();
        for (City dtoItem : dtoCities) {
            listToReturn.add(new CityModel(dtoItem));
        }
        return listToReturn;
    }
}
