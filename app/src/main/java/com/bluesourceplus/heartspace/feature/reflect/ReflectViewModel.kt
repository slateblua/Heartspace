package com.bluesourceplus.heartspace.feature.reflect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluesourceplus.heartspace.data.MoodBreakdown
import com.bluesourceplus.heartspace.data.MoodFrequency
import com.bluesourceplus.heartspace.data.database.MoodEntry
import com.bluesourceplus.heartspace.feature.reflect.usecases.GetMoodBreakdownUseCase
import com.bluesourceplus.heartspace.feature.reflect.usecases.GetMostCommonMoodsUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

class ReflectViewModel : ViewModel(), KoinComponent {
    private val getMoodBreakdownUseCase: GetMoodBreakdownUseCase by inject()
    private val getMostCommonMoodsUseCase: GetMostCommonMoodsUseCase by inject()

    private val _state = MutableStateFlow<AnalyticsState>(AnalyticsState.Loading)
    val state: StateFlow<AnalyticsState> = _state.asStateFlow()

    private val _sideEffects = Channel<AnalyticsSideEffect>()
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        handleIntent(AnalyticsIntent.LoadAnalytics)
    }

    fun handleIntent(intent: AnalyticsIntent) {
        when (intent) {
            is AnalyticsIntent.LoadAnalytics -> loadAnalytics()
            is AnalyticsIntent.OnMoodClicked -> onMoodClicked(intent.moodEntry)
        }
    }

    private fun loadAnalytics() {
        viewModelScope.launch {
            try {
                _state.value = AnalyticsState.Loading

                val breakdown = async { getMoodBreakdownUseCase() }
                val commonMoods = async { getMostCommonMoodsUseCase() }

                _state.value = AnalyticsState.Content(
                    moodBreakdown = breakdown.await(),
                    mostCommonMoods = commonMoods.await()
                )
            } catch (e: Exception) {
                _state.value = AnalyticsState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    private fun onMoodClicked(moodEntry: MoodEntry) {
        _sideEffects.trySend(AnalyticsSideEffect.NavigateToMoodDetails)
    }
}

sealed interface AnalyticsState {
    data object Loading : AnalyticsState
    data class Content(
        val moodBreakdown: List<MoodBreakdown> = emptyList(),
        val recentMoods: List<MoodEntry> = emptyList(),
        val mostCommonMoods: List<MoodFrequency> = emptyList(),
        val isRefreshing: Boolean = false
    ) : AnalyticsState

    data class Error(val message: String) : AnalyticsState
}

sealed class AnalyticsSideEffect {
    data object NavigateToMoodDetails : AnalyticsSideEffect()
}

sealed interface AnalyticsIntent {
    data object LoadAnalytics : AnalyticsIntent
    data class OnMoodClicked(val moodEntry: MoodEntry) : AnalyticsIntent
}