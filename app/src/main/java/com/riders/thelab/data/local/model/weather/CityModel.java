package com.riders.thelab.data.local.model.weather;

import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.riders.thelab.data.remote.dto.weather.City;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(tableName = "city")
@Setter
@Getter
@ToString
public class CityModel {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "_id")
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

    public CityModel() {
    }

    @Ignore
    public CityModel(City dtoCity) {
        // TODO create mapper
        setId(dtoCity.getId());
        setName(dtoCity.getName());
        setState(dtoCity.getState());
        setCountry(dtoCity.getCountry());
        setLatitude(dtoCity.getCoordinates().getLatitude());
        setLongitude(dtoCity.getCoordinates().getLongitude());
    }

    @Ignore
    public CityModel(Cursor cursor) {
        setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        setState(cursor.getString(cursor.getColumnIndexOrThrow("state")));
        setCountry(cursor.getString(cursor.getColumnIndexOrThrow("country")));
        setLatitude(cursor.getDouble(cursor.getColumnIndexOrThrow("latitude")));
        setLongitude(cursor.getDouble(cursor.getColumnIndexOrThrow("longitude")));
    }
}
