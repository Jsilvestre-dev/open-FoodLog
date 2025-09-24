package com.peep.nocalorieleftbehind.onboarding.di

import com.peep.nocalorieleftbehind.onboarding.OnboardingViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val OnboardingModule = module {
    viewModelOf(::OnboardingViewModel)
}