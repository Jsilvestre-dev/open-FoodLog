package com.peep.nocalorieleftbehind.core.data.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.peep.nocalorieleftbehind.core.data.local.dao.FoodDao
import com.peep.nocalorieleftbehind.core.data.local.dao.PreferenceDao
import com.peep.nocalorieleftbehind.core.data.local.entity.FoodEntity
import com.peep.nocalorieleftbehind.core.data.local.entity.PreferencesEntity
import com.peep.nocalorieleftbehind.core.util.Result
import com.peep.nocalorieleftbehind.core.util.getRoomResult
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

    override suspend fun upsertFood(foodEntity: FoodEntity): Result =
        withContext(Dispatchers.IO) {
            foodDao.upsertFood(foodEntity)
        }.getRoomResult()

    override suspend fun deleteFoodEntityById(id: Long): Result {
        val columnsDeleted = withContext(Dispatchers.IO) {
            foodDao.deleteFoodEntityById(id)
        }
        return getRoomDeleteResult(
            columnsDeleted = columnsDeleted,
            amountOfColumnsToBeDeleted = 1
        )
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

    companion object {
        fun getRoomDeleteResult(columnsDeleted: Int, amountOfColumnsToBeDeleted: Int): Result =
            if (columnsDeleted == amountOfColumnsToBeDeleted) Result.Successful else Result.Failure
    }

}