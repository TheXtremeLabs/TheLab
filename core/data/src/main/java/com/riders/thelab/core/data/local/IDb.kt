package com.riders.thelab.core.data.local

import android.database.Cursor
import com.riders.thelab.core.data.local.model.Contact
import com.riders.thelab.core.data.local.model.weather.CityModel
import com.riders.thelab.core.data.local.model.weather.WeatherData
import com.riders.thelab.core.data.remote.dto.weather.City


interface IDb {
    /////////////////////////////////////
    //
    // CONTACTS
    //
    /////////////////////////////////////
    fun insertContact(contact: Contact)
    suspend fun insertContactRX(contact: Contact): Long
    fun insertAllContacts(contactDetails: List<Contact>)
    fun getContacts(): List<Contact>
    suspend fun getAllContacts(): List<Contact>
    fun clearData()

    /////////////////////////////////////
    //
    // WEATHER
    //
    /////////////////////////////////////
    suspend fun insertWeatherData(isWeatherData: WeatherData): Long
    suspend fun saveCity(city: CityModel): Long
    suspend fun saveCities(dtoCities: List<City>): List<Long>
    suspend fun getWeatherData(): WeatherData?
    suspend fun getCities(): List<CityModel>
    fun getCitiesCursor(query: String): Cursor
    fun deleteAll()
}