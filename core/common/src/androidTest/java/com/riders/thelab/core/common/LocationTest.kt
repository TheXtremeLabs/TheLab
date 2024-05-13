package com.riders.thelab.core.common

import android.location.Location
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.riders.thelab.core.common.utils.toLocation
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LocationTest {

    @Test
    fun test_location_pair() {
        try {
            val latitude = 150.0
            val longitude = 210.0
            val location1: Location = (latitude to longitude).toLocation()

            println(location1.toString())

        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}