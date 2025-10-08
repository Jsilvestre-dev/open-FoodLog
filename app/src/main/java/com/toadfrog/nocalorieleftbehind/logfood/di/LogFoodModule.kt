package com.toadfrog.nocalorieleftbehind.logfood.di

import com.toadfrog.nocalorieleftbehind.logfood.ui.LogFoodViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val LogFoodModule = module {
    viewModelOf(::LogFoodViewModel)
}