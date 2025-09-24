package com.peep.nocalorieleftbehind.summary.di

import com.peep.nocalorieleftbehind.summary.ui.SummaryViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val SummaryModule = module {

    viewModelOf(::SummaryViewModel)
}