package com.toadfrog.nocalorieleftbehind.summary.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices.PHONE
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.toadfrog.nocalorieleftbehind.R
import com.toadfrog.nocalorieleftbehind.core.domain.model.Nutrition
import com.toadfrog.nocalorieleftbehind.core.ui.theme.NoCalorieLeftBehindTheme
import com.toadfrog.nocalorieleftbehind.core.ui.theme.montserratFamily
import com.toadfrog.nocalorieleftbehind.core.util.State
import com.toadfrog.nocalorieleftbehind.summary.ui.compnents.FoodEatenCard
import com.toadfrog.nocalorieleftbehind.summary.ui.compnents.Header
import com.toadfrog.nocalorieleftbehind.summary.ui.compnents.NutrientSummaryElement
import com.toadfrog.nocalorieleftbehind.summary.ui.model.FoodUiState
import com.toadfrog.nocalorieleftbehind.summary.ui.model.SummaryUiState
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.compose.KoinApplicationPreview
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SummaryScreen(
    onEditFood: (foodId: Long) -> Unit,
    onLogFood: () -> Unit,
    onNavigateToPreference: () -> Unit,
) {
    val viewModel = koinViewModel<SummaryViewModel>()
    val summaryUiState = viewModel.summaryUiState.collectAsStateWithLifecycle()
    val recentFoodsLazyPagingItems = viewModel.recentFoodsPagingData.collectAsLazyPagingItems()

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
                    lazyPagingItems = { recentFoodsLazyPagingItems },
                    summaryUiState = { summaryUiState.value },
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
    lazyPagingItems: () -> LazyPagingItems<FoodUiState>,
    summaryUiState: () -> SummaryUiState,
    onDeleteFood: (id: Long) -> Unit,
    onEditFood: (foodId: Long) -> Unit,
    onLogFood: () -> Unit,
    onPreference: () -> Unit
) {

    val lazyListState = rememberLazyListState()
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(topAppBarState)

    Scaffold(
        Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            CenterAlignedTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        text = summaryUiState().date
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
            AnimatedVisibility(
                visible = remember { derivedStateOf { scrollBehavior.state.collapsedFraction < .5f } }.value,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
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
                        Text("Log Food")
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            state = lazyListState
        ) {
            item(key = "header") {
                Header(
                    caloriesSummary = { summaryUiState().calories }
                )
            }

            item("nutrient Summary") {

                val isNutrientListEmpty = remember { derivedStateOf { summaryUiState().nutrientSummaryList.isEmpty() } }

                if (isNutrientListEmpty.value) return@item

                Text(
                    text = "Nutrient Summary",
                    fontFamily = montserratFamily,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                )


                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillParentMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    maxLines = 1,
                    maxItemsInEachRow = 3,
                ) {
                    summaryUiState().nutrientSummaryList.map { nutrientSummaryUiState ->
                        NutrientSummaryElement(
                            modifier = Modifier,
                            nutrientSummaryUiState = nutrientSummaryUiState
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Eaten Foods",
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
                    println("food ${foodUi.name} has id ${foodUi.id}")
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
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
@Preview(name = "Phone", device = PHONE, showSystemUi = true)
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
                onLogFood = {},
                summaryUiState = { SummaryUiState.default },
                onDeleteFood = {},
                onEditFood = {},
                onPreference = {},
                lazyPagingItems = { lazyPagingItems }
            )
        }
    }
}