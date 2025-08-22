package com.bluesourceplus.heartspace.feature.reflect

import com.bluesourceplus.heartspace.feature.reflect.usecases.GetMoodBreakdownUseCase
import com.bluesourceplus.heartspace.feature.reflect.usecases.GetMoodBreakdownUseCaseImpl
import com.bluesourceplus.heartspace.feature.reflect.usecases.GetMostCommonMoodsUseCase
import com.bluesourceplus.heartspace.feature.reflect.usecases.GetMostCommonMoodsUseCaseImpl
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val reflectModule = module {
    viewModel { ReflectViewModel() }

    single<GetMoodBreakdownUseCase> { GetMoodBreakdownUseCaseImpl(get()) }

    single<GetMostCommonMoodsUseCase> { GetMostCommonMoodsUseCaseImpl(get()) }
}