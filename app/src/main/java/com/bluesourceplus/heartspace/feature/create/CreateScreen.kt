package com.bluesourceplus.heartspace.feature.create

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.bluesourceplus.heartspace.components.formatTimestamp
import com.bluesourceplus.heartspace.data.Mood
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CreateScreenRoute(mode: CreateMoodMode, back: () -> Unit) {
    val createViewModel: CreateViewModel = koinViewModel { parametersOf(mode) }
    CreateScreen(
        createViewModel,
        { createViewModel.handleEvent(it) },
        back
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(
    createViewModel: CreateViewModel,
    onCreateMoodIntent: (CreateMoodIntent) -> Unit,
    back: () -> Unit
) {
    val state by createViewModel.state.collectAsStateWithLifecycle()

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent() // Or PickVisualMedia()
    ) { uri ->
        uri?.let {
            // You might need to handle content URI persistence across app restarts
            // by taking persistable URI permissions if using GetContent()
            // For PickVisualMedia, this is generally handled better.
            onCreateMoodIntent(CreateMoodIntent.OnImageSelected(it.toString()))
        }
    }

    LaunchedEffect(Unit) {
        createViewModel.sideEffect.collect { effect ->
            when (effect) {
                CreateMoodEffect.MoodSaved -> {
                    // empty for now
                }

                CreateMoodEffect.NavigateUp -> {
                    back()
                }
            }
        }
    }

    Column {
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
                IconButton(onClick = { onCreateMoodIntent(CreateMoodIntent.OnSaveClicked) }) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = "Save")
                }
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (state is State.Content) {
                val contentState = state as State.Content

                if (contentState.imageUri == null) {
                    OutlinedCard(modifier = Modifier.size(128.dp), onClick = {
                        imagePickerLauncher.launch("image/*")
                    }) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Add Image",
                                modifier = Modifier.size(20.dp),
                            )
                        }
                    }
                } else {
                    Image(
                        painter = rememberAsyncImagePainter(contentState.imageUri),
                        contentDescription = "Selected Mood Image",
                        modifier = Modifier.size(128.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Absolute.Center
                ) {
                    Text(
                        text = formatTimestamp(System.currentTimeMillis()),
                        fontSize = 16.sp,
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    val moods = Mood.entries
                    moods.forEachIndexed { index, mood ->
                        MoodButton(
                            mood = mood,
                            onClick = {
                                onCreateMoodIntent(CreateMoodIntent.OnMoodChanged(Mood.entries[index]))
                            },
                            isSelected = (mood == contentState.mood),
                        )
                    }
                }
                OutlinedTextField(
                    value = contentState.note,
                    onValueChange = {
                        createViewModel.handleEvent(CreateMoodIntent.OnNoteChanged(it))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    placeholder = { Text("How are you feeling?") }
                )
            }
        }
    }
}


@Composable
fun MoodButton(mood: Mood, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.size(60.dp),
        shape = CircleShape,
        contentPadding = PaddingValues(2.dp),
        colors = ButtonDefaults.buttonColors(containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent)
    ) {
        Icon(
            imageVector = mood.icon,
            contentDescription = mood.displayName,
            modifier = Modifier.size(35.dp),
            tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        )
    }
}