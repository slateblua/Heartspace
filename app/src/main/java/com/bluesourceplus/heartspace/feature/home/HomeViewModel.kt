package com.bluesourceplus.heartspace.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluesourceplus.heartspace.data.MoodModel
import com.bluesourceplus.heartspace.feature.home.usecases.GetAllMoodsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    private val moods = getAllMoodsUseCase()

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
