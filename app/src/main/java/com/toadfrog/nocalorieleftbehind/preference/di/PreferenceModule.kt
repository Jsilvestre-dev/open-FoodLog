package com.toadfrog.nocalorieleftbehind.preference.di

import com.toadfrog.nocalorieleftbehind.preference.ui.PreferenceViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val PreferenceModule = module {
    viewModelOf(::PreferenceViewModel)
}