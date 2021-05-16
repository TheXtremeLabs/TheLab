package com.riders.thelab.data.local.bean;

import com.riders.thelab.R;

public enum WindDirection {

    NORTH("N", "Northerly", 337.5, R.drawable.ic_wind_north),
    NORTH_WEST("NNW", "North Westerly", 292.5, R.drawable.ic_wind_north_west),
    WEST("W", "Westerly", 247.5, R.drawable.ic_wind_west),
    SOUTH_WEST("SSW", "South Westerly", 202.5, R.drawable.ic_wind_south_west),
    SOUTH("S", "Southerly", 157.5, R.drawable.ic_wind_south),
    SOUTH_EAST("SSE", "South Easterly", 122.5, R.drawable.ic_wind_south_east),
    EAST("E", "Easterly", 67.5, R.drawable.ic_wind_east),
    NORTH_EAST("NNE", "North Easterly", 22.5, R.drawable.ic_wind_north_east);

    private String shortName;
    private String name;
    private double degree;
    private int icon;

    WindDirection(String shortName, String name, double degree, int icon) {
        this.shortName = shortName;
        this.name = name;
        this.degree = degree;
        this.icon = icon;
    }

    public static WindDirection getWindDirectionToTextualDescription(double degree) {
        if (degree > 337.5) return NORTH;
        if (degree > 292.5) return NORTH_WEST;
        if (degree > 247.5) return WEST;
        if (degree > 202.5) return SOUTH_WEST;
        if (degree > 157.5) return SOUTH;
        if (degree > 122.5) return SOUTH_EAST;
        if (degree > 67.5) return EAST;
        if (degree > 22.5) {
            return NORTH_EAST;
        }
        return NORTH;
    }

    public String getShortName() {
        return shortName;
    }

    public String getName() {
        return name;
    }

    public double getDegree() {
        return degree;
    }

    public int getIcon() {
        return icon;
    }
}
