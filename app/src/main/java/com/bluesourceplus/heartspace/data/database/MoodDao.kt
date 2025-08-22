package com.bluesourceplus.heartspace.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.bluesourceplus.heartspace.data.MoodBreakdown
import com.bluesourceplus.heartspace.data.MoodFrequency
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

    @Query("SELECT mood, COUNT(*) as count, COUNT(*) * 100.0 / (SELECT COUNT(*) FROM MoodEntry) as percentage FROM MoodEntry GROUP BY mood ORDER BY count DESC")
    suspend fun getMoodBreakdown(): List<MoodBreakdown>

    @Query("SELECT mood, COUNT(*) as frequency FROM MoodEntry GROUP BY mood ORDER BY frequency DESC LIMIT :limit")
    suspend fun getMostCommonMoods(limit: Int = 5): List<MoodFrequency>
}