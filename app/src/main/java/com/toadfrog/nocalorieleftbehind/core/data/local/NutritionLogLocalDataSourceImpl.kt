package com.toadfrog.nocalorieleftbehind.core.data.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.toadfrog.nocalorieleftbehind.core.data.local.dao.FoodDao
import com.toadfrog.nocalorieleftbehind.core.data.local.dao.PreferenceDao
import com.toadfrog.nocalorieleftbehind.core.data.local.entity.FoodEntity
import com.toadfrog.nocalorieleftbehind.core.data.local.entity.PreferenceEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class NutritionLogLocalDataSourceImpl(
    private val preferenceDao: PreferenceDao,
    private val foodDao: FoodDao,
) : NutritionLogLocalDataSource {
    override suspend fun upsertPreferenceEntity(preferenceEntity: PreferenceEntity) {
        withContext(Dispatchers.IO) {
            preferenceDao.upsertPreferenceEntity(preferenceEntity = preferenceEntity)
        }
    }

    override fun readPreferenceEntity(): Flow<PreferenceEntity?> =
        preferenceDao.readPreferenceEntity().flowOn(Dispatchers.IO)

    override suspend fun upsertFoodEntity(foodEntity: FoodEntity) {
        withContext(Dispatchers.IO) {
            foodDao.upsertFoodEntity(foodEntity = foodEntity)
        }
    }

    override suspend fun deleteFoodEntityById(id: Long) {
        withContext(Dispatchers.IO) {
            foodDao.deleteFoodEntityById(id = id)
        }
    }

    override suspend fun readFoodEntityById(id: Long): FoodEntity {
        return withContext(Dispatchers.IO) {
            foodDao.readFoodEntityById(foodId = id)
        }
    }

    override fun readFoodEntitiesByTime(timeStampEpochSec: Long): Flow<List<FoodEntity>?> =
        foodDao.readFoodEntitiesByTime(timeStampEpochSec = timeStampEpochSec)
            .flowOn(
                context = Dispatchers.IO
            )

    override fun readRecentFoodEntitiesPager() = Pager(
        config = PagingConfig(
            pageSize = 8
        ),
        pagingSourceFactory = { foodDao.readRecentFoodPagingSource() }
    )
}