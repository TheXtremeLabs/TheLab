package com.riders.thelab.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.riders.thelab.data.local.model.weather.City;

import java.util.List;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface WeatherDao {

    @Insert(onConflict = REPLACE)
    Maybe<Long> insertRX(City city);

    /* Method to insert contacts fetched from api to room */
    @Insert(onConflict = REPLACE)
    void insert(City city);

    @Insert(onConflict = REPLACE)
    Maybe<List<Long>> insertAllRX(List<City> cities);

    /* Method to fetch contacts stored locally */
    @Query("SELECT * FROM city")
    Single<List<City>> getCities();


    @Query("DELETE FROM city")
    void deleteAll();
}
