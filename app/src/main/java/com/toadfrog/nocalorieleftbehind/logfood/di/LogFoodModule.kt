package com.toadfrog.nocalorieleftbehind.logfood.di

import com.toadfrog.nocalorieleftbehind.core.data.repository.FoodRepository
import com.toadfrog.nocalorieleftbehind.core.data.repository.FoodRepositoryImpl
import com.toadfrog.nocalorieleftbehind.logfood.ui.LogFoodViewModel
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