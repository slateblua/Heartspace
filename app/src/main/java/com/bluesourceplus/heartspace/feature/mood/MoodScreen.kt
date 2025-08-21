package com.bluesourceplus.heartspace.feature.mood

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MoodScreenRoute() {
    // This is the entry point for the MoodScreen.
    // You can add any necessary ViewModel or state management here.
    MoodScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoodScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        CenterAlignedTopAppBar(
            title = { Text(text = "Mood") },
        )
    }
}