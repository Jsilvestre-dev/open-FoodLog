package com.peep.nocalorieleftbehind.summary.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import com.peep.nocalorieleftbehind.R
import com.peep.nocalorieleftbehind.core.domain.Nutrient
import com.peep.nocalorieleftbehind.core.domain.model.Nutrition
import com.peep.nocalorieleftbehind.core.ui.theme.NoCalorieLeftBehindTheme
import com.peep.nocalorieleftbehind.core.util.UiState
import com.peep.nocalorieleftbehind.summary.ui.compnents.FoodEatenCard
import com.peep.nocalorieleftbehind.summary.ui.compnents.Header
import com.peep.nocalorieleftbehind.summary.ui.compnents.MacroSummaryItem
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.compose.KoinApplicationPreview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SummaryScreen(
    onEditFood: (foodId: Long) -> Unit,
    onLogFood: () -> Unit,
    onNavigateToPreference: () -> Unit,
) {
    val viewModel = koinViewModel<SummaryViewModel>()
    val screenUiState = viewModel.screenUiFlow.collectAsStateWithLifecycle()
    val summaryUiState = viewModel.summaryUiFlow.collectAsStateWithLifecycle()
    val foodsEatenLazyPagingItems = viewModel.foodsEatenFlow.collectAsLazyPagingItems()

    AnimatedContent(
        targetState = screenUiState
    ) { uiState ->
        when (uiState.value) {
            is UiState.Error -> {}
            is UiState.Loading -> {}
            is UiState.Success<*> -> {
                Ui(
                    lazyPagingItems = foodsEatenLazyPagingItems,
                    summaryUi = { summaryUiState.value },
                    onDeleteFood = viewModel::deleteFood,
                    onEditFood = onEditFood,
                    onLogFood = onLogFood,
                    onPreference = onNavigateToPreference
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun Ui(
    lazyPagingItems: LazyPagingItems<FoodUi>,
    summaryUi: () -> SummaryUi,
    onDeleteFood: (id: Long) -> Unit,
    onEditFood: (foodId: Long) -> Unit,
    onLogFood: () -> Unit,
    onPreference: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = summaryUi().date
                    )
                },
                actions = {
                    FilledIconButton(
                        onClick = onPreference,
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.outline_tune_24),
                            contentDescription = ""
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            MediumExtendedFloatingActionButton(
                onClick = onLogFood
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(FloatingActionButtonDefaults.MediumIconSize),
                        imageVector = ImageVector.vectorResource(R.drawable.outline_fork_spoon_24),
                        contentDescription = null
                    )
                    Text("Ate Food")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item(key = "header") {
                Header(
                    caloriesSummary = { summaryUi().calories }
                )
            }

            item("protein") {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    maxLines = 3
                ) {
                    summaryUi().protein?.let { proteinSummary ->
                        MacroSummaryItem(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .weight(.33f),
                            nutrientSummary = proteinSummary
                        )
                    }

                    summaryUi().carbs?.let { carbsSummary ->
                        MacroSummaryItem(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .weight(.33f),
                            nutrientSummary = carbsSummary
                        )
                    }

                    summaryUi().fats?.let { fatsSummary ->
                        MacroSummaryItem(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .weight(.33f),
                            nutrientSummary = fatsSummary
                        )
                    }
                }
            }
            items(
                count = lazyPagingItems.itemCount,
                key = { it },
                contentType = lazyPagingItems.itemContentType { item -> item }
            ) { index ->
                lazyPagingItems[index]?.let { foodUi ->
                    FoodEatenCard(
                        modifier = Modifier.fillMaxWidth(),
                        foodUi = foodUi,
                        onDeleteFood = onDeleteFood,
                        onEditFood = onEditFood
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
@PreviewLightDark
private fun Private() {
    KoinApplicationPreview(
        application = {}
    ) {
        val foods = mutableListOf<FoodUi>()
        repeat(4) {
            foods.add(
                FoodUi(
                    id = 0,
                    name = "Watermelon",
                    Nutrition(
                        calories = 100,
                        protein = 4,
                        carbs = 16,
                        fats = 0,
                    ),
                    timeStampEpochSec = 1000L
                )
            )
        }

        NoCalorieLeftBehindTheme {
            Ui(
                onLogFood = {},
                summaryUi = {
                    SummaryUi(
                        calories = NutrientSummary(
                            nutrient = Nutrient.CALORIES,
                            eaten = 1000,
                            left = 500,
                            total = 1500
                        ),

                        protein = NutrientSummary(
                            nutrient = Nutrient.PROTEIN,
                            eaten = 82,
                            left = 68,
                            total = 150
                        ),
                        carbs = NutrientSummary(
                            nutrient = Nutrient.CARBS,
                            eaten = 49,
                            left = 31,
                            total = 80
                        ),
                        fats = NutrientSummary(
                            nutrient = Nutrient.FATS,
                            eaten = 37,
                            left = 13,
                            total = 50

                        ),
                        date = "Aug 18, 2025"
                    )
                },
                onDeleteFood = {},
                onEditFood = {},
                onPreference = {},
                lazyPagingItems = MutableStateFlow(PagingData.from(foods)).collectAsLazyPagingItems()
            )
        }
    }
}