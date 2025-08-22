package com.bluesourceplus.heartspace.feature.reflect.usecases

import com.bluesourceplus.heartspace.data.MoodBreakdown
import com.bluesourceplus.heartspace.data.MoodRepo

class GetMoodBreakdownUseCaseImpl(
    private val moodRepo: MoodRepo
) : GetMoodBreakdownUseCase {
    override suspend fun invoke(): List<MoodBreakdown> {
        return moodRepo.getMoodBreakdown()
    }
}