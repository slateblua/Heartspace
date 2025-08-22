package com.bluesourceplus.heartspace.feature.reflect.usecases

import com.bluesourceplus.heartspace.data.MoodFrequency
import com.bluesourceplus.heartspace.data.MoodRepo

class GetMostCommonMoodsUseCaseImpl(
    private val moodRepo: MoodRepo
) : GetMostCommonMoodsUseCase {
    override suspend fun invoke(limit: Int): List<MoodFrequency> {
        return moodRepo.getMostCommonMoods(limit)
    }
}