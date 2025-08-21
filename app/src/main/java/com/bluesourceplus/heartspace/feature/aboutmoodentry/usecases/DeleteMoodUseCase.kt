package com.bluesourceplus.heartspace.feature.aboutmoodentry.usecases

import com.bluesourceplus.heartspace.data.MoodModel

interface DeleteMoodUseCase {
    suspend operator fun invoke(mood: MoodModel)
}