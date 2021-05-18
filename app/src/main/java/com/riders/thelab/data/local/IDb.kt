package com.riders.thelab.data.local

import android.database.Cursor
import com.riders.thelab.data.local.model.Contact
import com.riders.thelab.data.local.model.weather.CityModel
import com.riders.thelab.data.local.model.weather.WeatherData
import com.riders.thelab.data.remote.dto.weather.City
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

interface IDb {
    /////////////////////////////////////
    //
    // CONTACTS
    //
    /////////////////////////////////////
    fun insertContact(contact: Contact)
    fun insertContactRX(contact: Contact): Maybe<Long>
    fun insertAllContacts(contactDetails: List<Contact>)
    fun getContacts(): List<Contact>
    fun getAllContacts(): Single<List<Contact>>
    fun clearData()

    /////////////////////////////////////
    //
    // WEATHER
    //
    /////////////////////////////////////
    fun insertWeatherData(isWeatherData: WeatherData): Maybe<Long>
    fun saveCity(city: CityModel): Maybe<Long>
    fun saveCities(dtoCities: List<City>): Maybe<List<Long>>
    fun getWeatherData(): Single<WeatherData>
    fun getCities(): Single<List<CityModel>>
    fun getCitiesCursor(query: String): Cursor
    fun deleteAll()
}