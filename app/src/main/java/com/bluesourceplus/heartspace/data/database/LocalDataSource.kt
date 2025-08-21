package com.bluesourceplus.heartspace.data.database

import com.bluesourceplus.heartspace.data.MoodModel
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getAll(): Flow<List<MoodModel>>

    fun getById(id: Int): Flow<MoodModel>

    suspend fun update(moodModel: MoodModel)

    suspend fun delete(moodEntry: MoodEntry)

    suspend fun add(moodModel: MoodModel)
}