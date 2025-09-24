package com.peep.nocalorieleftbehind.core.di

import androidx.room.ExperimentalRoomApi
import androidx.room.Room
import com.peep.nocalorieleftbehind.core.data.local.NutritionLogDatabase
import com.peep.nocalorieleftbehind.core.data.local.NutritionLogLocalDataSource
import com.peep.nocalorieleftbehind.core.data.local.NutritionLogLocalDataSourceImpl
import com.peep.nocalorieleftbehind.core.domain.ValidateFoodNameUseCase
import com.peep.nocalorieleftbehind.core.domain.ValidateNutrientUseCase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalRoomApi::class)
val CoreModule = module {

    single {
        Room
            .databaseBuilder(
                context = androidContext().applicationContext,
                klass = NutritionLogDatabase::class.java,
                name = "food-intake-db"
            )
            .setAutoCloseTimeout(10L, TimeUnit.SECONDS)
            .build()
    }
    single { get<NutritionLogDatabase>().preferenceDao() }
    single { get<NutritionLogDatabase>().foodDao() }

    factoryOf(::NutritionLogLocalDataSourceImpl) {
        bind<NutritionLogLocalDataSource>()
    }

    factoryOf(::ValidateNutrientUseCase)
    factoryOf(::ValidateFoodNameUseCase)
}