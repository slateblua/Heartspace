package com.bluesourceplus.heartspace.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mood
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.SentimentVerySatisfied as SentimentVeryDissatisfiedIcon

@Immutable
data class MoodModel(
    val id: Int = 0,
    val mood: Mood,
    val note: String,
    val timestamp: Long = System.currentTimeMillis(),
)

enum class Mood(val icon: ImageVector, val displayName: String) {
    SAD(Icons.Filled.SentimentVeryDissatisfied, "Sad"),
    TIRED(Icons.Filled.SentimentVeryDissatisfiedIcon, "Tired"),
    STRESSED(Icons.Filled.SentimentDissatisfied, "Stressed"),
    HAPPY(Icons.Filled.Mood, "Happy"),
    EXCITED(Icons.Filled.Mood, "Excited"),
}
