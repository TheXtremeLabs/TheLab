package com.riders.thelab.core.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.riders.thelab.core.data.local.model.music.ArtistModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(artist: ArtistModel) : Long

    /* Method to insert artists fetched from api to room */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(artists: List<ArtistModel>)

    /* Method to fetch contacts stored locally */
    @Query("SELECT * FROM artist")
    fun getAllArtists(): Flow<List<ArtistModel>>

    @Query("SELECT * FROM artist")
    fun getAllArtistsPaged(): PagingSource<Int, ArtistModel>

    @Query("SELECT * FROM artist WHERE _id = :id")
    fun getArtistById(id: Byte): ArtistModel

    @Transaction
    @Update
    fun updateArtist(artistToUpdate: ArtistModel): Int

    @Query("DELETE FROM artist")
    fun deleteAll()
}