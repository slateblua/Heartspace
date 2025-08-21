package com.bluesourceplus.heartspace.feature.aboutmoodentry.usecases

import com.bluesourceplus.heartspace.data.MoodModel
import com.bluesourceplus.heartspace.data.MoodRepo
import com.bluesourceplus.heartspace.data.database.toEntry

class DeleteMoodUseCaseImpl(private val moodRepo: MoodRepo) : DeleteMoodUseCase {
    override suspend fun invoke(mood: MoodModel) {
        moodRepo.delete(mood.toEntry())
    }
}