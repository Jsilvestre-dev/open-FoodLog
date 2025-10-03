package com.toadfrog.nocalorieleftbehind.preference.di

import com.toadfrog.nocalorieleftbehind.core.data.repository.PreferenceRepository
import com.toadfrog.nocalorieleftbehind.core.data.repository.PreferenceRepositoryImpl
import com.toadfrog.nocalorieleftbehind.preference.ui.PreferenceViewModel
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