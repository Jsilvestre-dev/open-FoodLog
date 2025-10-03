package com.toadfrog.nocalorieleftbehind.onboarding.di

import com.toadfrog.nocalorieleftbehind.onboarding.ui.OnboardingViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val OnboardingModule = module {
    viewModelOf(::OnboardingViewModel)
}