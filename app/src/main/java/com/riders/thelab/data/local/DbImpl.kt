package com.riders.thelab.data.local


import android.database.Cursor
import com.riders.thelab.data.local.dao.ContactDao
import com.riders.thelab.data.local.dao.WeatherDao
import com.riders.thelab.data.local.model.Contact
import com.riders.thelab.data.local.model.weather.CityMapper
import com.riders.thelab.data.local.model.weather.CityModel
import com.riders.thelab.data.local.model.weather.WeatherData
import com.riders.thelab.data.remote.dto.weather.City
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

    override suspend fun insertContactRX(contact: Contact): Long {
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

    override suspend fun getAllContacts(): List<Contact> {
        return mContactDao.getContacts()
    }

    override fun clearData() {
        mContactDao.deleteAll()
    }

    override suspend fun insertWeatherData(isWeatherData: WeatherData): Long {
        return mWeatherDao.insert(isWeatherData)
    }

    override suspend fun saveCity(city: CityModel): Long {
        return mWeatherDao.insertRX(city)
    }

    override suspend fun saveCities(dtoCities: List<City>): List<Long> {
        val citiesToDatabase: List<CityModel> = ArrayList(CityMapper.getCityList(dtoCities))

        return mWeatherDao.insertAllRX(citiesToDatabase)
    }

    override suspend fun getWeatherData(): WeatherData? {
        return mWeatherDao.getWeatherData()
    }

    override suspend fun getCities(): List<CityModel> {
        return mWeatherDao.getCities()
    }

    override fun getCitiesCursor(query: String): Cursor {
        return mWeatherDao.getCitiesCursor("%$query%")
    }

    override fun deleteAll() {
        mWeatherDao.deleteAll()
    }
}