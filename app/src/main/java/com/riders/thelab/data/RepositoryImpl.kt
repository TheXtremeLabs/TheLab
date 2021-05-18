package com.riders.thelab.data

import android.app.Activity
import android.database.Cursor
import android.location.Location
import com.google.firebase.storage.StorageReference
import com.riders.thelab.data.local.DbImpl
import com.riders.thelab.data.local.model.Contact
import com.riders.thelab.data.local.model.Video
import com.riders.thelab.data.local.model.weather.CityModel
import com.riders.thelab.data.local.model.weather.WeatherData
import com.riders.thelab.data.remote.ApiImpl
import com.riders.thelab.data.remote.dto.artist.Artist
import com.riders.thelab.data.remote.dto.weather.City
import com.riders.thelab.data.remote.dto.weather.OneCallWeatherResponse
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import okhttp3.ResponseBody
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
        dbImpl: DbImpl,
        apiImpl: ApiImpl
) : IRepository {

    private var mDbImpl: DbImpl = dbImpl
    private var mApiImpl: ApiImpl = apiImpl


    override fun insertContact(contact: Contact) {
        mDbImpl.insertContact(contact)
    }

    override fun insertContactRX(contact: Contact): Maybe<Long> {
        return mDbImpl.insertContactRX(contact)
    }

    override fun insertAllContacts(contactDetails: List<Contact>) {
        mDbImpl.insertAllContacts(contactDetails)
    }

    override fun getContacts(): List<Contact> {
        return mDbImpl.getContacts()
    }

    override fun getAllContacts(): Single<List<Contact>> {
        return mDbImpl.getAllContacts()
    }

    override fun clearData() {
        mDbImpl.clearData()
    }

    override fun insertWeatherData(isWeatherData: WeatherData): Maybe<Long> {
        return mDbImpl.insertWeatherData(isWeatherData)
    }

    override fun saveCity(city: CityModel): Maybe<Long> {
        return mDbImpl.saveCity(city)
    }

    override fun saveCities(dtoCities: List<City>): Maybe<List<Long>> {
        return mDbImpl.saveCities(dtoCities)
    }

    override fun getWeatherData(): Single<WeatherData> {
        return mDbImpl.getWeatherData()
    }

    override fun getCities(): Single<List<CityModel>> {
        return mDbImpl.getCities()
    }

    override fun getCitiesCursor(query: String): Cursor {
        return mDbImpl.getCitiesCursor(query)
    }

    override fun deleteAll() {
        mDbImpl.deleteAll()
    }

    override fun getStorageReference(activity: Activity): Single<StorageReference> {
        return mApiImpl.getStorageReference(activity)
    }

    override fun getArtists(url: String): Single<List<Artist>> {
        return mApiImpl.getArtists(url)
    }

    override fun getVideos(): Single<List<Video>> {
        return mApiImpl.getVideos()
    }

    override fun getWeatherOneCallAPI(location: Location): Single<OneCallWeatherResponse>? {
        return mApiImpl.getWeatherOneCallAPI(location)
    }

    override fun getBulkWeatherCitiesFile(): Single<ResponseBody> {
        return mApiImpl.getBulkWeatherCitiesFile()
    }
}