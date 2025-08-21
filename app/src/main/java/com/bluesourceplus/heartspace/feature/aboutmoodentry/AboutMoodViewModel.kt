package com.bluesourceplus.heartspace.feature.aboutmoodentry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluesourceplus.heartspace.data.Mood
import com.bluesourceplus.heartspace.feature.aboutmoodentry.usecases.DeleteMoodUseCase
import com.bluesourceplus.heartspace.feature.aboutmoodentry.usecases.GetMoodByIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed interface AboutMoodState {
    data class Content(
        val id: Int = 0,
        val note: String = "",
        val mood: Mood = Mood.SAD,
    ) : AboutMoodState
}

sealed class AboutMoodEffect {
    data object MoodDeleted : AboutMoodEffect()
}

sealed interface AboutMoodIntent {
    data class LoadMood(
        val moodId: Int,
    ) : AboutMoodIntent

    data class DeleteMood(
        val moodId: Int,
    ) : AboutMoodIntent
}


class AboutMoodViewModel : ViewModel(), KoinComponent {
    private val getMoodByIdUseCase: GetMoodByIdUseCase by inject()
    private val deleteMoodUseCase: DeleteMoodUseCase by inject()

    private val _state =
        MutableStateFlow<AboutMoodState>(
            AboutMoodState.Content()
        )
    val state = _state.asStateFlow()

    private val _sideEffect = Channel<AboutMoodEffect>()
    val sideEffect = _sideEffect.receiveAsFlow()

    fun handleEvent(event: AboutMoodIntent) {
        when (event) {
            is AboutMoodIntent.LoadMood -> loadMood(event.moodId)
            is AboutMoodIntent.DeleteMood -> deleteMood(moodId = event.moodId)
        }
    }

    private fun deleteMood(moodId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val mood = getMoodByIdUseCase(moodId)
            deleteMoodUseCase(mood.first())
            _sideEffect.send(AboutMoodEffect.MoodDeleted)
        }
    }

    private fun loadMood(moodId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val moodModel = getMoodByIdUseCase(moodId).first()
            _state.update {
                AboutMoodState.Content(
                    id = moodId,
                    note = moodModel.note,
                    mood = moodModel.mood,
                )
            }
        }
    }
}