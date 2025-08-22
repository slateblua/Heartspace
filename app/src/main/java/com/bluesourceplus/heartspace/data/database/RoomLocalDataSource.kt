package com.bluesourceplus.heartspace.data.database

import com.bluesourceplus.heartspace.data.MoodBreakdown
import com.bluesourceplus.heartspace.data.MoodFrequency
import com.bluesourceplus.heartspace.data.MoodModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomLocalDataSource (private val dao: MoodDao): LocalDataSource {
    override fun getAll(): Flow<List<MoodModel>> {
        return dao.getAll().map { moodFlow ->
            moodFlow.map { moodEnt ->
                moodEnt.toModel()
            }
        }
    }

    override fun getById(id: Int): Flow<MoodModel> {
        return dao.getById(id).map { moodEnt ->
            moodEnt.toModel()
        }
    }

    override suspend fun update(moodModel: MoodModel) {
        dao.update(moodEntry = moodModel.toEntry())
    }

    override suspend fun delete(moodEntry: MoodEntry) {
        dao.delete(moodEntry)
    }

    override suspend fun add(moodModel: MoodModel) {
        dao.add(moodEntry = moodModel.toEntry())
    }

    override suspend fun getMoodBreakdown(): List<MoodBreakdown> {
        return dao.getMoodBreakdown()
    }

    override suspend fun getMostCommonMoods(limit: Int): List<MoodFrequency> {
        return dao.getMostCommonMoods()
    }
}