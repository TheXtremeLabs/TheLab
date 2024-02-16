package com.riders.thelab.core.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.riders.thelab.core.data.local.LabDatabase
import com.riders.thelab.core.data.local.dao.UserDao
import com.riders.thelab.core.data.local.model.User
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.util.Locale

class SimpleEntityReadWriteTest {

    private lateinit var userDao: UserDao
    private lateinit var db: LabDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room
            .inMemoryDatabaseBuilder(context, LabDatabase::class.java)
            .build()
        userDao = db.getUserDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
        val user: User = User.mockUserForTests[0]
        userDao.insertUser(user)
        val userByName = userDao.getUserByName("Jane".lowercase(Locale.getDefault()))
        MatcherAssert.assertThat(userByName, CoreMatchers.equalTo(user))
    }
}