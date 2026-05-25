package com.example.echoes.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ConcertDao {
    @Query("SELECT * FROM concerts ORDER BY date DESC")
    fun getAllConcerts(): Flow<List<ConcertEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConcert(concert: ConcertEntity)

    @Update
    suspend fun updateConcert(concert: ConcertEntity)

    @Delete
    suspend fun deleteConcert(concert: ConcertEntity)
}