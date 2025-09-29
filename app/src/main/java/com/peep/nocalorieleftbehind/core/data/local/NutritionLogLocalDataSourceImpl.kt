package com.peep.nocalorieleftbehind.core.data.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.peep.nocalorieleftbehind.core.data.local.dao.FoodDao
import com.peep.nocalorieleftbehind.core.data.local.dao.PreferenceDao
import com.peep.nocalorieleftbehind.core.data.local.entity.FoodEntity
import com.peep.nocalorieleftbehind.core.data.local.entity.PreferencesEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class NutritionLogLocalDataSourceImpl(
    private val preferenceDao: PreferenceDao,
    private val foodDao: FoodDao,
) : NutritionLogLocalDataSource {
    override suspend fun upsertPreference(preferencesEntity: PreferencesEntity) {
        withContext(Dispatchers.IO) {
            preferenceDao.upsertPreference(preferencesEntity)
        }
    }

    override fun queryPreference(): Flow<PreferencesEntity?> = preferenceDao.getPreference().flowOn(Dispatchers.IO)

    override suspend fun upsertFood(foodEntity: FoodEntity) {
        withContext(Dispatchers.IO) {
            foodDao.upsertFood(foodEntity)
        }
    }

    override suspend fun deleteFoodEntityById(id: Long) {
        withContext(Dispatchers.IO) {
            foodDao.deleteFoodEntityById(id)
        }
    }

    override suspend fun findFoodById(id: Long): FoodEntity {
        return withContext(Dispatchers.IO) {
            foodDao.findFoodById(id)
        }
    }

    override fun getFoodByTimestamp(timeStampEpochSec: Long): Flow<List<FoodEntity>?> =
        foodDao.getFoodByTimeStamp(timeStampEpochSec)
            .flowOn(
                context = Dispatchers.IO
            )


    override fun recentFoodsPager() = Pager(
        config = PagingConfig(
            pageSize = 8
        ),
        pagingSourceFactory = { foodDao.recentFoodPagingSource() }
    )
}