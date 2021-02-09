package com.riders.thelab.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.riders.thelab.data.local.model.weather.CityModel;

import java.util.List;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

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


    /* Method to fetch contacts stored locally */
    @Query("SELECT * FROM city")
    Single<List<CityModel>> getCities();


    @Query("DELETE FROM city")
    void deleteAll();
}
