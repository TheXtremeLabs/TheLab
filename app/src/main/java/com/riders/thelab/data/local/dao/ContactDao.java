package com.riders.thelab.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.riders.thelab.data.local.model.Contact;

import java.util.List;

import io.reactivex.rxjava3.core.Single;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ContactDao {

    @Insert(onConflict = REPLACE)
    void insert(Contact contact);

    /* Method to insert contacts fetched from api to room */
    @Insert(onConflict = REPLACE)
    void insert(List<Contact> contacts);

    @Query("SELECT * FROM Contact")
    List<Contact> getAllContacts();

    /* Method to fetch contacts stored locally */
    @Query("SELECT * FROM Contact")
    Single<List<Contact>> getContacts();


    @Query("DELETE FROM Contact")
    void deleteAll();
}
