package com.riders.thelab.core.data.local

import android.database.Cursor
import com.riders.thelab.core.data.local.dao.ContactDao
import com.riders.thelab.core.data.local.dao.UserDao
import com.riders.thelab.core.data.local.dao.WeatherDao
import com.riders.thelab.core.data.local.model.Contact
import com.riders.thelab.core.data.local.model.User
import com.riders.thelab.core.data.local.model.weather.CityMapper
import com.riders.thelab.core.data.local.model.weather.CityModel
import com.riders.thelab.core.data.local.model.weather.WeatherData
import com.riders.thelab.core.data.remote.dto.weather.City
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DbImpl @Inject constructor(
    userDao: UserDao,
    contactDao: ContactDao,
    weatherDao: WeatherDao
) : IDb {

    private var mUserDao: UserDao = userDao
    private var mContactDao: ContactDao = contactDao
    private var mWeatherDao: WeatherDao = weatherDao
    override fun insertUser(user: User): Long {
        val maybeAdminUser: User = user

        if (
            "admin" == maybeAdminUser.username ||
            "michaelSth" == maybeAdminUser.username ||
            "theLabAdmin" == maybeAdminUser.username
        ) {
            maybeAdminUser.isAdmin = true
        }

        return mUserDao.insertUser(maybeAdminUser)
    }

    override fun insertAllUsers(users: List<User>) = mUserDao.insertAllUsers(users)

    override fun getUsers(): Flow<List<User>> = mUserDao.getUsers()

    override fun getUsersSync(): List<User> = mUserDao.getUsersSync()

    override fun getUserByID(userId: Int): User = mUserDao.getUserByID(userId)

    override fun getUserByName(username: String): User = mUserDao.getUserByName(username)

    override fun getUserByEmail(email: String): User = mUserDao.getUserByEmail(email)
    override fun logUser(usernameOrMail: String, passwordEncoded: String): User? {
        return runCatching {
            return mUserDao.logInWithUsername(usernameOrMail, passwordEncoded)
                ?: mUserDao.logInWithEmail(usernameOrMail, passwordEncoded)
        }
            .onFailure {
                it.printStackTrace()
            }
            .getOrNull()
    }

    override fun deleteUser(userId: Int) = mUserDao.deleteUser(userId)

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