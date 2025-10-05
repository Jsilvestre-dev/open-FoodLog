package com.toadfrog.nocalorieleftbehind.preference.ui

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
import androidx.compose.foundation.layout.safeDrawing
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.toadfrog.nocalorieleftbehind.R
import com.toadfrog.nocalorieleftbehind.core.domain.Nutrient
import com.toadfrog.nocalorieleftbehind.core.ui.model.NutrientDto
import com.toadfrog.nocalorieleftbehind.core.ui.model.NutrientUiState
import com.toadfrog.nocalorieleftbehind.core.ui.model.NutritionUiState
import com.toadfrog.nocalorieleftbehind.core.ui.theme.NoCalorieLeftBehindTheme
import com.toadfrog.nocalorieleftbehind.core.ui.theme.montserratFamily
import com.toadfrog.nocalorieleftbehind.core.ui.theme.notoSansFamily
import com.toadfrog.nocalorieleftbehind.core.util.State
import com.toadfrog.nocalorieleftbehind.onboarding.ui.components.NutrientSelectionUi
import com.toadfrog.nocalorieleftbehind.preference.ui.components.NutrientDialog
import com.toadfrog.nocalorieleftbehind.preference.ui.components.PreferenceCard
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PreferenceScreen() {

    val viewModel = koinViewModel<PreferenceViewModel>()
    val preferenceUiState = viewModel.preferenceUiState.collectAsStateWithLifecycle()
    val selectedNutrientUiState = viewModel.selectedNutrientUiState.collectAsStateWithLifecycle()

    AnimatedContent(
        targetState = remember { derivedStateOf { preferenceUiState.value.state } }.value
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
                SuccessUi(
                    nutritionUi = { preferenceUiState.value.nutritionUiState },
                    selectedNutrientUiState = { selectedNutrientUiState.value },
                    onTrackNutrients = viewModel::onTrackNutrients,
                    onInput = viewModel::onInput,
                    onEditNutrient = viewModel::onEditNutrient,
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
    nutritionUi: () -> NutritionUiState,
    selectedNutrientUiState: () -> NutrientUiState?,
    onTrackNutrients: (List<Nutrient>) -> Unit,
    onInput: (NutrientDto) -> Unit,
    onEditNutrient: (Nutrient?) -> Unit,
    onSave: () -> Unit,
    onRemove: (Nutrient) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val showBottomSheet = remember { mutableStateOf(false) }

    SharedTransitionLayout {
        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
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
                modifier = Modifier.padding(horizontal = 16.dp),
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
                        selectedNutrient = { selectedNutrientUiState()?.nutrient },
                        nutrientUiState = nutritionUi().getNutrientUi(nutrient)!!,
                        onRemove = onRemove,
                        onEdit = {
                            onEditNutrient(nutrient)
                        }
                    )
                }
            }
        }

        NutrientDialog(
            nutrientUiState = selectedNutrientUiState,
            onInput = onInput,
            onDismiss = {
                onEditNutrient(null)
            },
            onSave = {
                onSave()
                onEditNutrient(null)
            }
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
                    trackedNutrients = { nutrientSelected.value },
                    onTrackNutrient = { nutrient ->
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
                            onTrackNutrients(nutrientSelected.value)
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
                    NutritionUiState()
                },
                selectedNutrientUiState = {
                    null
                },
                onTrackNutrients = {},
                onInput = {},
                onEditNutrient = {},
                onSave = {},
                onRemove = {}
            )
        }
    }
}