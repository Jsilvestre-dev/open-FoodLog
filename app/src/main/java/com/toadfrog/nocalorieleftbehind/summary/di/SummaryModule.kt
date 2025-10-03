package com.toadfrog.nocalorieleftbehind.summary.di

import com.toadfrog.nocalorieleftbehind.summary.ui.SummaryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val SummaryModule = module {

    viewModelOf(::SummaryViewModel)
}