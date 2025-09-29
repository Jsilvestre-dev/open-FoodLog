package com.peep.nocalorieleftbehind.preference.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peep.nocalorieleftbehind.R
import com.peep.nocalorieleftbehind.core.domain.Nutrient
import com.peep.nocalorieleftbehind.core.ui.NutritionUi
import com.peep.nocalorieleftbehind.core.ui.theme.NoCalorieLeftBehindTheme
import com.peep.nocalorieleftbehind.core.ui.theme.montserratFamily
import com.peep.nocalorieleftbehind.core.ui.theme.notoSansFamily
import com.peep.nocalorieleftbehind.core.util.UiElement
import com.peep.nocalorieleftbehind.onboarding.components.NutrientSelectionUi
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PreferenceScreen() {

    val viewModel = koinViewModel<PreferenceViewModel>()
    val screenUiState = viewModel.screenUiFlow.collectAsStateWithLifecycle()
    val nutritionUiState = viewModel.nutritionUi.collectAsStateWithLifecycle()
    val updatedNutrientUiState = viewModel.updatedNutrientUiFlow.collectAsStateWithLifecycle()

    AnimatedContent(
        targetState = screenUiState
    ) { screenUi ->
        when (screenUi.value) {
            is UiElement.Error -> {}
            is UiElement.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    LoadingIndicator()
                }
            }

            is UiElement.Success<*> -> {
                SuccessUi(
                    nutritionUi = { nutritionUiState.value },
                    updatedNutrientUi = { updatedNutrientUiState.value },
                    onNutrientsSelected = viewModel::onSelected,
                    onInput = viewModel::onInput,
                    onUpdateNutrient = viewModel::onUpdateNutrientUi,
                    onSave = viewModel::savePreference,
                    onRemove = viewModel::onRemove
                )
            }
        }
    }

}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
private fun SuccessUi(
    nutritionUi: () -> NutritionUi,
    updatedNutrientUi: () -> NutrientUi?,
    onNutrientsSelected: (List<Nutrient>) -> Unit,
    onInput: (NutrientData) -> Unit,
    onUpdateNutrient: (Nutrient?) -> Unit,
    onSave: () -> Unit,
    onRemove: (Nutrient) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val showBottomSheet = remember { mutableStateOf(false) }

    SharedTransitionLayout {
        Scaffold(
            contentWindowInsets = WindowInsets(left = 16.dp, right = 16.dp),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.preferences),
                            fontFamily = montserratFamily,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            },
            floatingActionButtonPosition = FabPosition.Center,
            floatingActionButton = {
                SmallExtendedFloatingActionButton(
                    onClick = {
                        showBottomSheet.value = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = ""
                    )
                }
            }
        ) { paddingValues ->
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = paddingValues
            ) {
                items(
                    items = nutritionUi().trackedNutrients(true),
                    key = { it.name },
                    contentType = { nutritionUi().getNutrientUi(it) }
                ) { nutrient ->
                    PreferenceCard(
                        modifier = Modifier.animateItem(),
                        nutrientBeingUpdated = { updatedNutrientUi()?.nutrient },
                        nutrient = nutrient,
                        nutrientUiState = { nutritionUi().getNutrientUi(nutrient)!! },
                        onRemove = onRemove,
                        onEdit = {
                            onUpdateNutrient(nutrient)
                        }
                    )
                }
            }
        }

        NutrientDialog(
            nutrient = { updatedNutrientUi()?.nutrient },
            nutrientUiState = { updatedNutrientUi()?.ui },
            onInput = onInput,
            onDismiss = {
                onUpdateNutrient(null)
            },
            onSave = onSave
        )
    }

    if (showBottomSheet.value) {
        ModalBottomSheet(
            modifier = Modifier.fillMaxWidth(),
            sheetState = sheetState,
            onDismissRequest = {
                showBottomSheet.value = false
            }
        ) {
            Column {

                val nutrientSelected = remember { mutableStateOf(listOf<Nutrient>()) }

                NutrientSelectionUi(
                    modifier = Modifier.padding(16.dp),
                    selectableNutrients = nutritionUi().untrackedNutrients(),
                    selectedNutrients = { nutrientSelected.value },
                    onNutrientSelected = { nutrient ->
                        nutrientSelected.value.let { nutrientList ->
                            nutrientSelected.value = if (nutrientList.contains(nutrient)) {
                                nutrientList.minus(nutrient)
                            } else {
                                nutrientList.plus(nutrient)
                            }
                        }
                    }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            onNutrientsSelected(nutrientSelected.value)
                            showBottomSheet.value = false
                        }
                    ) {
                        Text(
                            text = "Confirm",
                            style = MaterialTheme.typography.bodyLarge,
                            fontFamily = notoSansFamily
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun Preview() {
    NoCalorieLeftBehindTheme {
        AnimatedVisibility(visible = true) {
            SuccessUi(
                nutritionUi = {
                    NutritionUi()
                },
                updatedNutrientUi = {
                    null
                },
                onNutrientsSelected = {},
                onInput = {},
                onUpdateNutrient = {},
                onSave = {},
                onRemove = {}
            )
        }
    }
}