package com.bluesourceplus.heartspace.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MoodEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val mood: String,
    val timestamp: Long = System.currentTimeMillis(),
    val note: String = "",
)