package com.riders.thelab.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.riders.thelab.core.data.local.model.Song
import com.riders.thelab.core.data.local.model.music.MusicRecognitionModel
import kotlinx.coroutines.flow.Flow

@Dao
interface MusicRecognitionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(song: MusicRecognitionModel): Long

    /* Method to fetch songs stored locally */
    @Query("SELECT * FROM music_recognition")
    fun getAllMusicRecognitionItems(): Flow<List<MusicRecognitionModel>>

    @Query("DELETE FROM music_recognition")
    fun deleteAll()
}