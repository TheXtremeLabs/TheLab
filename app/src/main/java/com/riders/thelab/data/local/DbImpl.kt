package com.riders.thelab.data.local

import android.database.Cursor
import com.riders.thelab.data.local.dao.ContactDao
import com.riders.thelab.data.local.dao.WeatherDao
import com.riders.thelab.data.local.model.Contact
import com.riders.thelab.data.local.model.weather.CityMapper
import com.riders.thelab.data.local.model.weather.CityModel
import com.riders.thelab.data.local.model.weather.WeatherData
import com.riders.thelab.data.remote.dto.weather.City
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class DbImpl @Inject constructor(
    contactDao: ContactDao,
    weatherDao: WeatherDao
) : IDb {

    private var mContactDao: ContactDao = contactDao
    private var mWeatherDao: WeatherDao = weatherDao

    override fun insertContact(contact: Contact) {
        mContactDao.insert(contact)
    }

    override fun insertContactRX(contact: Contact): Maybe<Long> {
        return mContactDao.insertRX(contact)
    }

    override fun insertAllContacts(contactDetails: List<Contact>) {
        val contactListToDatabase: MutableList<Contact> = java.util.ArrayList()
        for (contactDetail in contactDetails) {
            contactListToDatabase.add(contactDetail)
        }
        mContactDao.insert(contactListToDatabase)
    }

    override fun getContacts(): List<Contact> {
        return mContactDao.getAllContacts()
    }

    override fun getAllContacts(): Single<List<Contact>> {
        return mContactDao.getContacts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun clearData() {
        mContactDao.deleteAll()
    }

    override fun insertWeatherData(isWeatherData: WeatherData): Maybe<Long> {
        return mWeatherDao.insert(isWeatherData)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun saveCity(city: CityModel): Maybe<Long> {
        return mWeatherDao
            .insertRX(city)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun saveCities(dtoCities: List<City>): Maybe<List<Long>> {
        val citiesToDatabase: List<CityModel> = ArrayList(CityMapper.getCityList(dtoCities))

        return mWeatherDao
            .insertAllRX(citiesToDatabase)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getWeatherData(): Single<WeatherData> {
        return mWeatherDao.getWeatherData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getCities(): Single<List<CityModel>> {
        return mWeatherDao.getCities()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getCitiesCursor(query: String): Cursor {
        return mWeatherDao.getCitiesCursor(query)
    }

    override fun deleteAll() {
        mWeatherDao.deleteAll()
    }
}