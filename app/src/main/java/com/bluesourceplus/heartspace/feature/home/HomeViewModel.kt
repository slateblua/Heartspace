package com.bluesourceplus.heartspace.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluesourceplus.heartspace.data.MoodModel
import com.bluesourceplus.heartspace.feature.aboutmoodentry.usecases.DeleteMoodUseCase
import com.bluesourceplus.heartspace.feature.aboutmoodentry.usecases.GetMoodByIdUseCase
import com.bluesourceplus.heartspace.feature.home.usecases.GetAllMoodsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

sealed interface HomeScreenState {
    data object Empty : HomeScreenState

    data class Content(
        val moods: List<MoodModel>,
    ) : HomeScreenState
}

class HomeViewModel : ViewModel(), KoinComponent {
    private val getAllMoodsUseCase: GetAllMoodsUseCase by inject()
    private val deleteMoodUseCase: DeleteMoodUseCase by inject()
    private val getMoodByIdUseCase: GetMoodByIdUseCase by inject()

    private val moods = getAllMoodsUseCase()

    fun handleEvent(event: HomeScreenIntent) {
        when (event) {
            is HomeScreenIntent.DeleteMood -> deleteMood(event.moodId)
        }
    }

    private fun deleteMood(moodId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val mood = getMoodByIdUseCase(moodId)
            deleteMoodUseCase(mood.first())
        }
    }

    val state: StateFlow<HomeScreenState> =
        moods
            .map { moods ->
                if (moods.isNotEmpty()) {
                    HomeScreenState.Content(moods)
                } else {
                    HomeScreenState.Empty
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = HomeScreenState.Empty,
            )

}

sealed interface HomeScreenIntent {
    data class DeleteMood(
        val moodId: Int,
    ) : HomeScreenIntent
}