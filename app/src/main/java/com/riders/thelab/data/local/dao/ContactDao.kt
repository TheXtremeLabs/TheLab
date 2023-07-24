package com.riders.thelab.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.riders.thelab.data.local.model.Contact


@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contact: Contact)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRX(contact: Contact): Long

    /* Method to insert contacts fetched from api to room */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contacts: List<Contact>)

    @Query("SELECT * FROM Contact")
    fun getAllContacts(): List<Contact>

    /* Method to fetch contacts stored locally */
    @Query("SELECT * FROM Contact")
    fun getContacts(): List<Contact>

    @Query("DELETE FROM Contact")
    fun deleteAll()
}