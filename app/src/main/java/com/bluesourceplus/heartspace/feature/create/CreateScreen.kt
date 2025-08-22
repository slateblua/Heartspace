package com.bluesourceplus.heartspace.feature.create

import android.icu.text.DateFormat
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.rememberAsyncImagePainter
import com.bluesourceplus.heartspace.data.Mood
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.util.Date
import java.util.Locale

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
            title = { Text(text = "Create") },
            navigationIcon = {
                IconButton(onClick = back) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            actions = {
                IconButton(onClick = {
                    onCreateMoodIntent(CreateMoodIntent.OnSaveClicked)
                }) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = "Save")
                }
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Absolute.Center
            ) {
                Text(
                    text = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault())
                        .format(Date()),
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
                        isSelected = (mood == (state as State.Content).mood),
                    )
                }
            }
            OutlinedTextField(
                value = (state as State.Content).note,
                onValueChange = {
                    createViewModel.handleEvent(CreateMoodIntent.OnNoteChanged(it))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                placeholder = { Text("How are you feeling?") }
            )


            Spacer(modifier = Modifier.height(16.dp))

            if (state is State.Content) {
                val contentState = state as State.Content
                contentState.imageUri?.let { uriString ->
                    Image(
                        painter = rememberAsyncImagePainter(uriString),
                        contentDescription = "Selected Mood Image",
                        modifier = Modifier.size(128.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        // Option to remove the image
                        onCreateMoodIntent(CreateMoodIntent.OnImageSelected(null))
                    }) {
                        Text("Remove Image")
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                imagePickerLauncher.launch("image/*") // Mime type for images
            }) {
                Text("Select Image")
            }

            Button(
                onClick = { createViewModel.handleEvent(CreateMoodIntent.OnSaveClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Save", fontSize = 18.sp)
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