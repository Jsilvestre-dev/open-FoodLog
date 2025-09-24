package com.peep.nocalorieleftbehind.core.data.local.converter

import androidx.room.TypeConverter
import com.peep.nocalorieleftbehind.core.domain.Nutrient
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter

class MapNutrientIntConverter {
    @OptIn(ExperimentalStdlibApi::class)
    @TypeConverter
    fun convertToString(trackedNutrientLimits: Map<Nutrient, Int>): String? {
        return try {
            val moshi = Moshi.Builder().build()
            val jsonAdapter: JsonAdapter<Map<Nutrient, Int>> = moshi.adapter<Map<Nutrient, Int>>()
            return jsonAdapter.toJson(trackedNutrientLimits)
        } catch (e: Exception) {
            null
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    @TypeConverter
    fun convertToMapNutrientInt(json: String): Map<Nutrient, Int>? {
        return try {
            val moshi = Moshi.Builder().build()
            val jsonAdapter: JsonAdapter<Map<Nutrient, Int>> = moshi.adapter<Map<Nutrient, Int>>()
            jsonAdapter.fromJson(json)
        } catch (e: Exception) {
            null
        }
    }
}