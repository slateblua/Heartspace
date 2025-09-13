package com.bluesourceplus.heartspace.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bluesourceplus.heartspace.components.MoodCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreenRoute(
    viewModel: HomeViewModel = koinViewModel(),
    onAddButton: () -> Unit,
    onMoodCardPressed: (Int) -> Unit,
    onPrefsPressed: () -> Unit = {  }, // Placeholder for preferences action,
    onUpdateMoodPressed: (Int) -> Unit = { } // Placeholder for update mood action
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        onAddButton = onAddButton,
        onMoodCardPressed = onMoodCardPressed,
        state = state,
        onHomeScreenState = { it -> viewModel.handleEvent(it) },
        onPrefsPressed = onPrefsPressed,
        onUpdateMoodPressed = onUpdateMoodPressed
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddButton: () -> Unit,
    onMoodCardPressed: (Int) -> Unit,
    state: HomeScreenState,
    onHomeScreenState: (HomeScreenIntent) -> Unit,
    onUpdateMoodPressed: (Int) -> Unit = {},
    onPrefsPressed: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CenterAlignedTopAppBar(
            title = { Text(text = "Heartspace", color = MaterialTheme.colorScheme.primary, fontSize = 22.sp) },
            actions = {
//                IconButton(onClick = onPrefsPressed) {
//                    Icon(
//                        imageVector = Icons.Filled.Settings,
//                        contentDescription = "Preferences",
//                        tint = MaterialTheme.colorScheme.onSurface,
//                        modifier = Modifier.size(24.dp)
//                    )
//                }
            }
        )
        when (state) {
            is HomeScreenState.Content -> {
                Text(
                    text = "How are you feeling today?",
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                AddNewEntryCard {
                    onAddButton()
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn {
                    items(state.moods, key = { mood -> mood.id }) { mood ->
                        Spacer(modifier = Modifier.height(10.dp))
                        MoodCard(
                            moodModel = mood,
                            onMoodPressed = { onMoodCardPressed(mood.id) },
                            onDelete = { onHomeScreenState(HomeScreenIntent.DeleteMood(mood.id)) },
                            onUpdatePressed = { onUpdateMoodPressed(it) },
                        )
                    }
                }
            }

            HomeScreenState.Empty -> {
                HomeEmptyContent(onCreateButtonClicked = onAddButton)
            }
        }
    }
}

@Composable
fun AddNewEntryCard(onAddButton: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        onClick = onAddButton,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.FavoriteBorder,
                    contentDescription = "Add Mood",
                    tint = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Tap to add new mood",
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun HomeEmptyContent(
    modifier: Modifier = Modifier,
    onCreateButtonClicked: () -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "How are you feeling today?",
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            AddNewEntryCard {
                onCreateButtonClicked()
            }
        }
    }
}
