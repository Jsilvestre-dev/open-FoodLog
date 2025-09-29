package com.peep.nocalorieleftbehind.summary.data

import com.peep.nocalorieleftbehind.core.domain.model.Food
import com.peep.nocalorieleftbehind.core.domain.Nutrient
import com.peep.nocalorieleftbehind.core.domain.model.Preferences
import com.peep.nocalorieleftbehind.summary.ui.FoodUi
import com.peep.nocalorieleftbehind.summary.ui.NutrientSummaryUiState
import com.peep.nocalorieleftbehind.summary.ui.SummaryUiState
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, ExperimentalTime::class, FormatStringsInDatetimeFormats::class)
fun toSummaryUi(foodsEaten: List<Food>, preferences: Preferences): SummaryUiState {

    val caloriesEaten = foodsEaten.sumOf { food -> food.nutrition.calories }
    val caloriesSummary = NutrientSummaryUiState(
        nutrient = Nutrient.CALORIES,
        eaten = caloriesEaten,
        left = preferences.nutrition.calories - caloriesEaten,
        total = preferences.nutrition.calories
    )

    val proteinSummary = preferences.nutrition.protein?.let {
        val proteinEaten = foodsEaten.sumOf { food -> food.nutrition.protein ?: 0 }
        NutrientSummaryUiState(
            nutrient = Nutrient.PROTEIN,
            eaten = proteinEaten,
            left = preferences.nutrition.protein - proteinEaten,
            total = preferences.nutrition.protein
        )
    }

    val carbsSummary = preferences.nutrition.carbs?.let {
        val carbsEaten = foodsEaten.sumOf { food -> food.nutrition.carbs ?: 0 }
        NutrientSummaryUiState(
            nutrient = Nutrient.CARBS,
            eaten = carbsEaten,
            left = preferences.nutrition.carbs - carbsEaten,
            total = preferences.nutrition.carbs
        )
    }

    val fatsSummary = preferences.nutrition.fats?.let {
        val fatsEaten = foodsEaten.sumOf { food -> food.nutrition.fats ?: 0 }
        NutrientSummaryUiState(
            nutrient = Nutrient.FATS,
            eaten = fatsEaten,
            left = preferences.nutrition.fats - fatsEaten,
            total = preferences.nutrition.fats
        )
    }

    val dateFormat = LocalDate.Format {
        monthName(MonthNames.ENGLISH_ABBREVIATED)
        chars(" ")
        day()

    }

    val formattedDate = Clock.System.todayIn(TimeZone.currentSystemDefault()).format(dateFormat)

    return SummaryUiState(
        calories = caloriesSummary,
        nutrientSummaryList = buildList {
            proteinSummary?.let { add(it) }
            carbsSummary?.let { add(it) }
            fatsSummary?.let { add(it) }
        },
        date = formattedDate
    )
}

fun Food.toFoodUi() = FoodUi(
    id = id,
    name = name,
    nutrition = nutrition,
    timeStampEpochSec = timeStampEpochSec
)
