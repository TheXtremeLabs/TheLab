package com.riders.thelab.data.local.model.weather;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(tableName = "city")
@Setter
@Getter
@ToString
public class City {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    public int id;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "state")
    public String state;
    @ColumnInfo(name = "country")
    public String country;
    @ColumnInfo(name = "longitude")
    public double longitude;
    @ColumnInfo(name = "latitude")
    public double latitude;

    public City() {
    }

    @Ignore
    public City(com.riders.thelab.data.remote.dto.weather.City dtoCity) {
        // TODO create mapper
        setId(dtoCity.getId());
        setName(dtoCity.getName());
        setState(dtoCity.getState());
        setCountry(dtoCity.getCountry());
        setLatitude(dtoCity.getCoordinates().getLatitude());
        setLongitude(dtoCity.getCoordinates().getLongitude());
    }
}
