package com.riders.thelab.data.local.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.riders.thelab.data.local.model.weather.CityModel;
import com.riders.thelab.data.local.model.weather.WeatherData;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface WeatherDao {

    @Insert(onConflict = REPLACE)
    Maybe<Long> insertRX(CityModel city);

    @Insert(onConflict = REPLACE)
    Maybe<List<Long>> insertAllRX(List<CityModel> cities);

    /* Method to insert contacts fetched from api to room */
    @Insert(onConflict = REPLACE)
    void insert(CityModel city);

    @Insert(onConflict = REPLACE)
    Maybe<Long> insert(WeatherData isWeatherData);


    /* Method to fetch contacts stored locally */
    @Query("SELECT * FROM weather_data")
    Single<WeatherData> getWeatherData();

    @Query("SELECT * FROM city")
    Single<List<CityModel>> getCities();

    @Query("SELECT * FROM city WHERE name LIKE :cityText")
    LiveData<List<CityModel>> getCities(String cityText);

    @Query("SELECT * FROM city WHERE name LIKE :cityText")
    Cursor getCitiesCursor(String cityText);


    @Query("DELETE FROM city")
    void deleteAll();
}
