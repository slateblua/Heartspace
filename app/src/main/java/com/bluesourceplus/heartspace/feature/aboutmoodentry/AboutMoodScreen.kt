package com.bluesourceplus.heartspace.feature.aboutmoodentry

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.bluesourceplus.heartspace.components.DeleteMoodAlert
import org.koin.androidx.compose.koinViewModel

@Composable
fun AboutMoodRoute(
    aboutMoodViewModel: AboutMoodViewModel = koinViewModel(),
    moodId: Int,
    back: () -> Unit,
    onUpdateMoodPressed: (Int) -> Unit
) {
    val state by aboutMoodViewModel.state.collectAsStateWithLifecycle()

    AboutMoodScreen(
        moodId = moodId,
        state = state,
        aboutMoodViewModel = aboutMoodViewModel,
        back = back,
        onUpdateMoodPressed = onUpdateMoodPressed,
        onAboutMoodIntent = { aboutMoodViewModel.handleEvent(it) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutMoodScreen(
    moodId: Int,
    state: AboutMoodState,
    aboutMoodViewModel: AboutMoodViewModel,
    back: () -> Unit,
    onUpdateMoodPressed: (Int) -> Unit,
    onAboutMoodIntent: (AboutMoodIntent) -> Unit
) {
    var openAlertDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        CenterAlignedTopAppBar(
            title = { Text(text = "Heartspace") },
            navigationIcon = {
                IconButton(onClick = back) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                IconButton(onClick = { onUpdateMoodPressed(moodId) }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit mood"
                    )
                }
                IconButton(onClick = { openAlertDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete mood"
                    )
                }
            }
        )


        when (state) {
            is AboutMoodState.Content -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = state.note,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = state.mood.displayName,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Image(
                            painter = rememberAsyncImagePainter(state.imageUri),
                            contentDescription = "Selected Mood Image",
                            modifier = Modifier
                                .size(180.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

        if (openAlertDialog) {
            AlertDialog(
                onDismissRequest = { openAlertDialog = false },
                title = { Text(text = "Delete mood?") },
                text = { Text(text = "Are you sure you want to delete this mood?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onAboutMoodIntent(AboutMoodIntent.DeleteMood(moodId))
                            openAlertDialog = false
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { openAlertDialog = false }
                    ) {
                        Text("Dismiss")
                    }
                }
            )
            DeleteMoodAlert(
                onDelete = {
                    onAboutMoodIntent(AboutMoodIntent.DeleteMood(moodId))
                },
                onDismissRequest = { openAlertDialog = false }
            )
        }
        LaunchedEffect(Unit) {
            onAboutMoodIntent(AboutMoodIntent.LoadMood(moodId))
        }

        LaunchedEffect(Unit) {
            aboutMoodViewModel.sideEffect.collect { effect ->
                when (effect) {
                    AboutMoodEffect.MoodDeleted -> {
                        openAlertDialog = false
                        back()
                    }
                }
            }
        }
    }
}