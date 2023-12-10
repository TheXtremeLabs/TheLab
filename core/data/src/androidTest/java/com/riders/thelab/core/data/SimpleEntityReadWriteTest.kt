package com.riders.thelab.core.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.riders.thelab.core.data.local.LabDatabase
import com.riders.thelab.core.data.local.dao.UserDao
import com.riders.thelab.core.data.local.model.User
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
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
        println("writeUserAndReadInList()")

        val user: User = User.mockUserForTests[0]
        userDao.insertUser(user)
        println("inserted user: $user")

        val userByUsername = userDao.getUserByUsername("JaneDoe345")
        println("user found by userByUsername: $userByUsername")

        assertThat(userByUsername).isEqualTo(user)
    }
}