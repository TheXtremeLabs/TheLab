package com.riders.thelab.core.data.local.model.weather

import android.annotation.SuppressLint
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.data.remote.dto.weather.City

object CityMapper {

    @SuppressLint("NewApi")
    fun getCityList(dtoCities: List<City?>): List<CityModel> {
        val listToReturn: MutableList<CityModel> = ArrayList()
        if (!LabCompatibilityManager.isNougat()) {
            // Cannot use streams on list
            // Use common method
            for (dtoItem in dtoCities) {
                dtoItem?.let { listToReturn.add(CityModel(it)) }
            }
        } else {
            // Use stream method
            dtoCities
                .stream()
                .forEach { dtoCity: City? ->
                    dtoCity
                        ?.let { CityModel(it) }
                        ?.let { listToReturn.add(it) }
                }
        }
        return listToReturn
    }
}
