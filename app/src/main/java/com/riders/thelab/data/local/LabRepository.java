package com.riders.thelab.data.local;

import com.riders.thelab.data.local.dao.ContactDao;
import com.riders.thelab.data.local.model.Contact;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LabRepository {

    private final ContactDao contactDao;


    @Inject
    public LabRepository(ContactDao contactDao) {
        this.contactDao = contactDao;
    }


    /////////////////////////////////////
    //
    // INSERT
    //
    /////////////////////////////////////
    public void insertContact(final Contact contact) {
        contactDao.insert(contact);
    }

    public void insertAllContacts(final List<Contact> contactDetails) {

        List<Contact> contactListToDatabase = new ArrayList<>();
        for (Contact contactDetail : contactDetails) {
            contactListToDatabase.add(contactDetail);
        }
        contactDao.insert(contactListToDatabase);
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
