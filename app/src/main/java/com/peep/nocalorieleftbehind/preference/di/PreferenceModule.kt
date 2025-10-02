package com.peep.nocalorieleftbehind.preference.di

import com.peep.nocalorieleftbehind.core.data.repository.PreferenceRepository
import com.peep.nocalorieleftbehind.core.data.repository.PreferenceRepositoryImpl
import com.peep.nocalorieleftbehind.preference.ui.PreferenceViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val PreferenceModule = module {
    factoryOf(::PreferenceRepositoryImpl) {
        bind<PreferenceRepository>()
    }

    viewModelOf(::PreferenceViewModel)
}