package com.bluesourceplus.heartspace.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
    @Query("SELECT * FROM moodentry ORDER BY timestamp DESC")
    fun getAll(): Flow<List<MoodEntry>>

    @Query("SELECT * FROM moodentry WHERE id = :id")
    fun getById(id: Int): Flow<MoodEntry>

    @Update
    fun update(moodEntry: MoodEntry)

    @Delete
    fun delete(moodEntry: MoodEntry)

    @Insert
    fun add(moodEntry: MoodEntry)
}