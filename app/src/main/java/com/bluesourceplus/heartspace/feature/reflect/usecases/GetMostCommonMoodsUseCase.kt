package com.bluesourceplus.heartspace.feature.reflect.usecases

import com.bluesourceplus.heartspace.data.MoodFrequency

interface GetMostCommonMoodsUseCase {
    suspend operator fun invoke(limit: Int = 5): List<MoodFrequency>
}