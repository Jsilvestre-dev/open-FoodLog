package com.peep.nocalorieleftbehind.logfood.di

import com.peep.nocalorieleftbehind.core.data.repository.FoodRepository
import com.peep.nocalorieleftbehind.core.data.repository.FoodRepositoryImpl
import com.peep.nocalorieleftbehind.logfood.ui.LogFoodViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val LogFoodModule = module {

    factoryOf(::FoodRepositoryImpl){
        bind<FoodRepository>()
    }
    viewModelOf(::LogFoodViewModel)
}