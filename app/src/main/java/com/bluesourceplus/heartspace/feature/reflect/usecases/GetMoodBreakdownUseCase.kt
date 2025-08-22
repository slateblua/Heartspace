package com.bluesourceplus.heartspace.feature.reflect.usecases

import com.bluesourceplus.heartspace.data.MoodBreakdown

// Use Cases
interface GetMoodBreakdownUseCase {
    suspend operator fun invoke(): List<MoodBreakdown>
}