package com.riders.thelab.data.local.model.weather

import android.annotation.SuppressLint
import com.riders.thelab.core.utils.LabCompatibilityManager
import com.riders.thelab.data.remote.dto.weather.City
import java.util.*

class CityMapper() {

    companion object {

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
}
