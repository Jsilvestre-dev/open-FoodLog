package com.toadfrog.nocalorieleftbehind.summary.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9_PRO_XL
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.toadfrog.nocalorieleftbehind.R
import com.toadfrog.nocalorieleftbehind.core.domain.Nutrient
import com.toadfrog.nocalorieleftbehind.core.domain.model.Nutrition
import com.toadfrog.nocalorieleftbehind.core.ui.theme.NoCalorieLeftBehindTheme
import com.toadfrog.nocalorieleftbehind.core.ui.theme.montserratFamily
import com.toadfrog.nocalorieleftbehind.core.ui.theme.notoSansFamily
import com.toadfrog.nocalorieleftbehind.core.util.State
import com.toadfrog.nocalorieleftbehind.summary.ui.compnents.FoodEatenCard
import com.toadfrog.nocalorieleftbehind.summary.ui.compnents.Header
import com.toadfrog.nocalorieleftbehind.summary.ui.compnents.NutritionSummaryCard
import com.toadfrog.nocalorieleftbehind.summary.ui.model.FoodUiState
import com.toadfrog.nocalorieleftbehind.summary.ui.model.NutrientSummaryUiState
import com.toadfrog.nocalorieleftbehind.summary.ui.model.SummaryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.compose.KoinApplicationPreview
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(
    onEditFood: (foodId: Long) -> Unit,
    onLogFood: () -> Unit,
    onNavigateToPreference: () -> Unit,
) {
    val viewModel = koinViewModel<SummaryViewModel>()
    val summaryUiState = viewModel.summaryUiState.collectAsStateWithLifecycle()
    val recentFoodsLazyPagingItems = viewModel.recentFoodsPagingData.collectAsLazyPagingItems()
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            CenterAlignedTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        text = stringResource(R.string.summary),
                        fontFamily = montserratFamily,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                actions = {
                    FilledIconButton(
                        onClick = onNavigateToPreference,
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
            AnimatedVisibility(
                visible = remember { derivedStateOf { scrollBehavior.state.collapsedFraction < .5f } }.value,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                MediumExtendedFloatingActionButton(
                    onClick = onLogFood,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
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
                        Text(
                            text = stringResource(R.string.log_food),
                            fontFamily = notoSansFamily
                        )
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        AnimatedContent(
            targetState = remember { derivedStateOf { summaryUiState.value.state } }.value
        ) { targetState ->
            when (targetState) {
                is State.Error -> {}
                is State.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator()
                    }
                }

                is State.Success -> {
                    Ui(
                        paddingValues = paddingValues,
                        lazyPagingItems = { recentFoodsLazyPagingItems },
                        summaryUiState = { summaryUiState.value },
                        onDeleteFood = viewModel::deleteFood,
                        onEditFood = onEditFood
                    )
                }
            }
        }
    }
}

@Composable
private fun Ui(
    paddingValues: PaddingValues,
    lazyPagingItems: () -> LazyPagingItems<FoodUiState>,
    summaryUiState: () -> SummaryUiState,
    onDeleteFood: (id: Long) -> Unit,
    onEditFood: (foodId: Long) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.padding(horizontal = 16.dp),
        contentPadding = paddingValues,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item(key = "header") {
            Header(
                caloriesSummary = { summaryUiState().calories }
            )
        }

        item("nutrition_summary") {
            NutritionSummaryCard(
                modifier = Modifier.fillMaxWidth(),
                nutrition = { summaryUiState().nutritionSummary }
            )
        }

        item {
            Text(
                text = stringResource(R.string.todays_food_log),
                fontFamily = montserratFamily,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleLarge
            )
        }

        items(
            count = lazyPagingItems().itemCount,
            key = lazyPagingItems().itemKey { it.id },
            contentType = lazyPagingItems().itemContentType { item -> item }
        ) { index ->
            lazyPagingItems()[index]?.let { foodUi ->
                FoodEatenCard(
                    modifier = Modifier
                        .animateItem()
                        .fillMaxWidth(),
                    foodUiState = foodUi,
                    onDeleteFood = onDeleteFood,
                    onEditFood = onEditFood
                )
            }
        }
    }

}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
@Preview(name = "medium_phone", device = PIXEL_9, showSystemUi = true)
@Preview(name = "big_phone", device = PIXEL_9_PRO_XL, showSystemUi = true)
private fun Private() {
    KoinApplicationPreview(
        application = {}
    ) {
        val foods = mutableListOf<FoodUiState>()
        repeat(4) { index ->
            foods.add(
                FoodUiState(
                    id = index.toLong(),
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
        val lazyPagingItems = MutableStateFlow(PagingData.from(foods)).collectAsLazyPagingItems()

        NoCalorieLeftBehindTheme {
            Ui(
                paddingValues = PaddingValues(),
                summaryUiState = {
                    SummaryUiState(
                        calories = NutrientSummaryUiState(
                            nutrient = Nutrient.CALORIES,
                            eaten = 700,
                            left = 800,
                            total = 1500
                        ),
                        nutritionSummary = buildList {
                            add(
                                NutrientSummaryUiState(
                                    nutrient = Nutrient.PROTEIN,
                                    eaten = 72,
                                    left = 28,
                                    total = 100
                                )
                            )
                            add(
                                NutrientSummaryUiState(
                                    nutrient = Nutrient.CARBS,
                                    eaten = 40,
                                    left = 0,
                                    total = 40
                                )
                            )
                            add(
                                NutrientSummaryUiState(
                                    nutrient = Nutrient.FATS,
                                    eaten = 44,
                                    left = 16,
                                    total = 60
                                )
                            )
                        },
                        date = "Apr 1"
                    )
                },
                onDeleteFood = {},
                onEditFood = {},
                lazyPagingItems = { lazyPagingItems }
            )
        }
    }
}