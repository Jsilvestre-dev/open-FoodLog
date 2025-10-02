package com.peep.nocalorieleftbehind.summary.data

import com.peep.nocalorieleftbehind.core.domain.model.Food
import com.peep.nocalorieleftbehind.core.domain.Nutrient
import com.peep.nocalorieleftbehind.core.domain.model.Preference
import com.peep.nocalorieleftbehind.core.util.State
import com.peep.nocalorieleftbehind.summary.ui.model.FoodUi
import com.peep.nocalorieleftbehind.summary.ui.model.NutrientSummaryUiState
import com.peep.nocalorieleftbehind.summary.ui.model.SummaryUiState
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, ExperimentalTime::class, FormatStringsInDatetimeFormats::class)
fun toSummaryUi(foodsEaten: List<Food>, preference: Preference): SummaryUiState {

    val caloriesEaten = foodsEaten.sumOf { food -> food.nutrition.calories }
    val caloriesSummary = NutrientSummaryUiState(
        nutrient = Nutrient.CALORIES,
        eaten = caloriesEaten,
        left = preference.nutrition.calories - caloriesEaten,
        total = preference.nutrition.calories
    )

    val proteinSummary = preference.nutrition.protein?.let {
        val proteinEaten = foodsEaten.sumOf { food -> food.nutrition.protein ?: 0 }
        NutrientSummaryUiState(
            nutrient = Nutrient.PROTEIN,
            eaten = proteinEaten,
            left = preference.nutrition.protein - proteinEaten,
            total = preference.nutrition.protein
        )
    }

    val carbsSummary = preference.nutrition.carbs?.let {
        val carbsEaten = foodsEaten.sumOf { food -> food.nutrition.carbs ?: 0 }
        NutrientSummaryUiState(
            nutrient = Nutrient.CARBS,
            eaten = carbsEaten,
            left = preference.nutrition.carbs - carbsEaten,
            total = preference.nutrition.carbs
        )
    }

    val fatsSummary = preference.nutrition.fats?.let {
        val fatsEaten = foodsEaten.sumOf { food -> food.nutrition.fats ?: 0 }
        NutrientSummaryUiState(
            nutrient = Nutrient.FATS,
            eaten = fatsEaten,
            left = preference.nutrition.fats - fatsEaten,
            total = preference.nutrition.fats
        )
    }

    val dateFormat = LocalDate.Format {
        monthName(MonthNames.ENGLISH_ABBREVIATED)
        chars(" ")
        day()

    }

    val formattedDate = Clock.System.todayIn(TimeZone.currentSystemDefault()).format(dateFormat)

    return SummaryUiState(
        state = State.Success,
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
