package com.bluesourceplus.heartspace.feature.home.usecases

import com.bluesourceplus.heartspace.data.MoodModel
import kotlinx.coroutines.flow.Flow

interface GetAllMoodsUseCase {
    operator fun invoke(): Flow<List<MoodModel>>
}