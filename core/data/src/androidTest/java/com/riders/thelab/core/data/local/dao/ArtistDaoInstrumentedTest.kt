package com.riders.thelab.core.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.test
import com.riders.thelab.core.data.local.LabDatabase
import com.riders.thelab.core.data.local.model.music.ArtistModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@SmallTest
@RunWith(AndroidJUnit4::class)
class ArtistDaoInstrumentedTest {

    private lateinit var context: Context
    private lateinit var artistDao: ArtistDao
    private lateinit var db: LabDatabase

    @Before
    fun createDb() {
        context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        db = Room
            .inMemoryDatabaseBuilder(context, LabDatabase::class.java)
            .build()
        artistDao = db.getArtistDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetArtist() = runTest {
        val artist = ArtistModel(
            id = 1,
            sceneName = "Daft Punk",
            firstName = "Thomas",
            lastName = "Bangalter",
            dateOfBirth = "03/01/1975",
            origin = "France",
            debutes = "1993",
            activities = "1993-2021",
            urlThumbnail = "https://example.com/daftpunk.jpg",
            description = "Daft Punk were a French electronic music duo formed in 1993 in Paris by Guy-Manuel de Homem-Christo and Thomas Bangalter."
        )
        val insertedId = artistDao.insert(artist)

        // The returned ID should be the one we set
        assertEquals(1L, insertedId)

        // Verify that the artist can be retrieved
        val retrievedArtist = artistDao.getArtistById(1)
        assertNotNull(retrievedArtist)
        assertEquals("Daft Punk", retrievedArtist.sceneName)
    }

    @Test
    fun insertMultipleArtistsAndGetAll() = runTest {
        val artists = listOf(
            ArtistModel(
                id = 1,
                sceneName = "Daft Punk",
                firstName = "Thomas",
                lastName = "Bangalter",
                dateOfBirth = "03/01/1975",
                origin = "France",
                debutes = "1993",
                activities = "1993-2021",
                urlThumbnail = "https://example.com/daftpunk.jpg",
                description = "Daft Punk were a French electronic music duo formed in 1993 in Paris by Guy-Manuel de Homem-Christo and Thomas Bangalter."
            ),
            ArtistModel(
                id = 2,
                sceneName = "Justice",
                firstName = "Gaspard",
                lastName = "Augé",
                dateOfBirth = "21/05/1979",
                origin = "France",
                debutes = "2003",
                activities = "2003-present",
                urlThumbnail = "https://example.com/justice.jpg",
                description = "Justice is a French electronic music duo consisting of Gaspard Augé and Xavier de Rosnay."
            )
        )
        artistDao.insert(artists)

        val allArtists = artistDao.getAllArtists().first()
        assertEquals(2, allArtists.size)
    }

    @Test
    fun getAllArtistsAsFlow() = runTest {
        val artists = listOf(
            ArtistModel(
                id = 1,
                sceneName = "Daft Punk",
                firstName = "Thomas",
                lastName = "Bangalter",
                dateOfBirth = "03/01/1975",
                origin = "France",
                debutes = "1993",
                activities = "1993-2021",
                urlThumbnail = "https://example.com/daftpunk.jpg",
                description = "Daft Punk were a French electronic music duo formed in 1993 in Paris by Guy-Manuel de Homem-Christo and Thomas Bangalter."
            ),
            ArtistModel(
                id = 2,
                sceneName = "Justice",
                firstName = "Gaspard",
                lastName = "Augé",
                dateOfBirth = "21/05/1979",
                origin = "France",
                debutes = "2003",
                activities = "2003-present",
                urlThumbnail = "https://example.com/justice.jpg",
                description = "Justice is a French electronic music duo consisting of Gaspard Augé and Xavier de Rosnay."
            )
        )

        artistDao.getAllArtists().test {
            // Initial state should be an empty list
            assertTrue(awaitItem().isEmpty())

            // Insert artists and expect a new emission with the list
            artistDao.insert(artists)
            val emittedList = awaitItem()
            assertEquals(2, emittedList.size)
            assertEquals("Daft Punk", emittedList[0].sceneName)

            // No more emissions
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun updateArtist() = runTest {
        val artist = ArtistModel(
            id = 1,
            sceneName = "Old Name",
            firstName = "Old First Name",
            lastName = "Old Last Name",
            dateOfBirth = "Old DOB",
            origin = "Old Origin",
            debutes = "Old Debutes",
            activities = "Old Activities",
            urlThumbnail = "old_url",
            description = "Old description"
        )
        artistDao.insert(artist)

        val updatedArtist = ArtistModel(
            id = 1,
            sceneName = "Daft Punk",
            firstName = "Thomas",
            lastName = "Bangalter",
            dateOfBirth = "03/01/1975",
            origin = "France",
            debutes = "1993",
            activities = "1993-2021",
            urlThumbnail = "new_url",
            description = "Daft Punk were a French electronic music duo formed in 1993 in Paris by Guy-Manuel de Homem-Christo and Thomas Bangalter."
        )
        val updatedRows = artistDao.updateArtist(updatedArtist)

        assertEquals(1, updatedRows)

        val retrievedArtist = artistDao.getArtistById(1)
        assertEquals("Daft Punk", retrievedArtist.sceneName)
        assertEquals("France", retrievedArtist.origin)
    }

    @Test
    fun deleteAllArtists() = runTest {
        val artists = listOf(
            ArtistModel(
                id = 1,
                sceneName = "Daft Punk",
                firstName = "Thomas",
                lastName = "Bangalter",
                dateOfBirth = "03/01/1975",
                origin = "France",
                debutes = "1993",
                activities = "1993-2021",
                urlThumbnail = "https://example.com/daftpunk.jpg",
                description = "Daft Punk were a French electronic music duo formed in 1993 in Paris by Guy-Manuel de Homem-Christo and Thomas Bangalter."
            ),
            ArtistModel(
                id = 2,
                sceneName = "Justice",
                firstName = "Gaspard",
                lastName = "Augé",
                dateOfBirth = "21/05/1979",
                origin = "France",
                debutes = "2003",
                activities = "2003-present",
                urlThumbnail = "https://example.com/justice.jpg",
                description = "Justice is a French electronic music duo consisting of Gaspard Augé and Xavier de Rosnay."
            )
        )
        artistDao.insert(artists)

        var allArtists = artistDao.getAllArtists().first()
        assertEquals(2, allArtists.size)

        artistDao.deleteAll()
        allArtists = artistDao.getAllArtists().first()
        assertTrue(allArtists.isEmpty())
    }
}