package com.riders.thelab.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.riders.thelab.data.local.dao.ContactDao;
import com.riders.thelab.data.local.model.Contact;

@Database(entities = {Contact.class},
        version = 2,
        exportSchema = false)
public abstract class LabDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "lab";

    public abstract ContactDao getContactDao();
}
