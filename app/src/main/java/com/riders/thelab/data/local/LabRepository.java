package com.riders.thelab.data.local;

import android.database.Cursor;

import com.riders.thelab.data.local.dao.ContactDao;
import com.riders.thelab.data.local.dao.WeatherDao;
import com.riders.thelab.data.local.model.Contact;
import com.riders.thelab.data.local.model.weather.CityMapper;
import com.riders.thelab.data.local.model.weather.CityModel;
import com.riders.thelab.data.local.model.weather.WeatherData;
import com.riders.thelab.data.remote.dto.weather.City;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class LabRepository {

    private final ContactDao contactDao;
    private final WeatherDao weatherDao;


    @Inject
    public LabRepository(ContactDao contactDao, WeatherDao weatherDao) {
        this.contactDao = contactDao;
        this.weatherDao = weatherDao;
    }


    /////////////////////////////////////
    //
    // INSERT
    //
    /////////////////////////////////////
    public void insertContact(final Contact contact) {
        contactDao.insert(contact);
    }

    public Maybe<Long> insertContactRX(final Contact contact) {
        return contactDao.insertRX(contact);
    }

    public void insertAllContacts(final List<Contact> contactDetails) {

        List<Contact> contactListToDatabase = new ArrayList<>();
        for (Contact contactDetail : contactDetails) {
            contactListToDatabase.add(contactDetail);
        }
        contactDao.insert(contactListToDatabase);
    }

    public Maybe<Long> insertWeatherData(WeatherData isWeatherData) {
        return weatherDao.insert(isWeatherData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Maybe<List<Long>> insertAllCities(final List<City> dtoCities) {

        List<CityModel> citiesToDatabase = new ArrayList<>(CityMapper.getCityList(dtoCities));
        return weatherDao
                .insertAllRX(citiesToDatabase)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /////////////////////////////////////
    //
    // SELECT
    //
    /////////////////////////////////////
    public List<Contact> getContacts() {
        return contactDao.getAllContacts();
    }

    public Single<List<Contact>> getAllContacts() {
        return contactDao.getContacts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<WeatherData> getWeatherData() {
        return weatherDao.getWeatherData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<CityModel>> getAllCities() {
        return weatherDao.getCities()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Cursor getCitiesCursor(String query) {
        return weatherDao.getCitiesCursor(query);
    }

    /////////////////////////////////////
    //
    // DELETE
    //
    /////////////////////////////////////

    /**
     * Clear All Local Data
     */
    public void clearData() {
        contactDao.deleteAll();
    }
}
