package com.toadfrog.nocalorieleftbehind.core.di

import androidx.room.ExperimentalRoomApi
import androidx.room.Room
import com.toadfrog.nocalorieleftbehind.core.data.local.NutritionLogDatabase
import com.toadfrog.nocalorieleftbehind.core.data.local.NutritionLogLocalDataSource
import com.toadfrog.nocalorieleftbehind.core.data.local.NutritionLogLocalDataSourceImpl
import com.toadfrog.nocalorieleftbehind.core.data.repository.FoodRepository
import com.toadfrog.nocalorieleftbehind.core.data.repository.FoodRepositoryImpl
import com.toadfrog.nocalorieleftbehind.core.data.repository.PreferenceRepository
import com.toadfrog.nocalorieleftbehind.core.data.repository.PreferenceRepositoryImpl
import com.toadfrog.nocalorieleftbehind.core.domain.ValidateFoodNameUseCase
import com.toadfrog.nocalorieleftbehind.core.domain.ValidateNutrientAmountUseCase
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

    factoryOf(::FoodRepositoryImpl){
        bind<FoodRepository>()
    }
    factoryOf(::PreferenceRepositoryImpl) {
        bind<PreferenceRepository>()
    }

    factoryOf(::ValidateNutrientAmountUseCase)
    factoryOf(::ValidateFoodNameUseCase)
}