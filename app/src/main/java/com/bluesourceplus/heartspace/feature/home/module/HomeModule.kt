package com.bluesourceplus.heartspace.feature.home.module

import com.bluesourceplus.heartspace.feature.home.HomeViewModel
import com.bluesourceplus.heartspace.feature.home.usecases.GetAllMoodsUseCase
import com.bluesourceplus.heartspace.feature.home.usecases.GetAllMoodsUseCaseImpl
import com.bluesourceplus.heartspace.feature.home.usecases.UpdateMoodUseCase
import com.bluesourceplus.heartspace.feature.home.usecases.UpdateMoodUseCaseImpl
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val homeModule = module {
    viewModel { HomeViewModel() }

    single { GetAllMoodsUseCaseImpl(get()) } bind GetAllMoodsUseCase::class

    single { UpdateMoodUseCaseImpl(get()) } bind UpdateMoodUseCase::class
}