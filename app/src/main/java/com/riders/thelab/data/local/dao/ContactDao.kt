package com.riders.thelab.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.riders.thelab.data.local.model.Contact
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contact: Contact)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRX(contact: Contact): Maybe<Long>

    /* Method to insert contacts fetched from api to room */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contacts: List<Contact>)

    @Query("SELECT * FROM Contact")
    fun getAllContacts(): List<Contact>

    /* Method to fetch contacts stored locally */
    @Query("SELECT * FROM Contact")
    fun getContacts(): Single<List<Contact>>


    @Query("DELETE FROM Contact")
    fun deleteAll()
}