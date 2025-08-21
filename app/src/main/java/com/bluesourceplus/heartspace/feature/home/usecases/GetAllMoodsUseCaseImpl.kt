package com.bluesourceplus.heartspace.feature.home.usecases

import com.bluesourceplus.heartspace.data.MoodModel
import com.bluesourceplus.heartspace.data.MoodRepo
import kotlinx.coroutines.flow.Flow

class GetAllMoodsUseCaseImpl(private val moodRepo: MoodRepo) : GetAllMoodsUseCase {
    override fun invoke(): Flow<List<MoodModel>> {
        return moodRepo.getAll()
    }
}