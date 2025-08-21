package com.bluesourceplus.heartspace.feature.focus

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
fun FocusScreenRoute() {
    // This is the entry point for the FocusScreen.
    // You can add any necessary ViewModel or state management here.
    FocusScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        CenterAlignedTopAppBar(
            title = { Text(text = "Focus") },
        )
    }
}