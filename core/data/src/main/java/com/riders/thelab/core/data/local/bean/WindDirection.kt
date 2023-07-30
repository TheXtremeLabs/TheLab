package com.riders.thelab.core.data.local.bean

import com.riders.thelab.R

enum class WindDirection(
    val shortName: String,
    val fullName: String,
    val degree: Double,
    val icon: Int
) {

    NORTH("N", "Northerly", 337.5, R.drawable.ic_wind_north),
    NORTH_WEST("NNW", "North Westerly", 292.5, R.drawable.ic_wind_north_west),
    WEST("W", "Westerly", 247.5, R.drawable.ic_wind_west),
    SOUTH_WEST("SSW", "South Westerly", 202.5, R.drawable.ic_wind_south_west),
    SOUTH("S", "Southerly", 157.5, R.drawable.ic_wind_south),
    SOUTH_EAST("SSE", "South Easterly", 122.5, R.drawable.ic_wind_south_east),
    EAST("E", "Easterly", 67.5, R.drawable.ic_wind_east),
    NORTH_EAST("NNE", "North Easterly", 22.5, R.drawable.ic_wind_north_east);

    companion object {
        fun getWindDirectionToTextualDescription(degree: Int): WindDirection {
            if (degree > 337.5) return NORTH
            if (degree > 292.5) return NORTH_WEST
            if (degree > 247.5) return WEST
            if (degree > 202.5) return SOUTH_WEST
            if (degree > 157.5) return SOUTH
            if (degree > 122.5) return SOUTH_EAST
            if (degree > 67.5) return EAST
            return if (degree > 22.5) {
                NORTH_EAST
            } else NORTH
        }
    }
}